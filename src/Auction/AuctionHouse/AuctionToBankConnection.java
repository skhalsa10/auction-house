package Auction.AuctionHouse;

import Auction.Messages.MAccountCreated;
import Auction.Messages.MCreateAccount;
import Auction.Messages.Message;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * this will be the "proxy..?" connectiong. it handles all of the communication to the bank server.
 * it accepts a socket to connect to. I am passing in the messageQueue of the Auction house. but I guess I could also
 * make give the proxy its own queue and make a method that polls it to get the inbound messages?
 */
public class AuctionToBankConnection extends Thread{

    private Socket bankSocket;
    private LinkedBlockingQueue<Message> houseMessageQueue;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean isRegistered;
    private boolean isRunning;

    /**
     * COnstruct a new AuctionToBankConnection this just communicates with the bank
     * @param bankSocket
     * @param houseMessageQueue
     */
    public AuctionToBankConnection(Socket bankSocket, LinkedBlockingQueue<Message> houseMessageQueue){
        isRunning = true;
        isRegistered = false;
        this.bankSocket = bankSocket;
        this.houseMessageQueue = houseMessageQueue;
        try {
            out = new ObjectOutputStream(this.bankSocket.getOutputStream());
            in =  new ObjectInputStream(this.bankSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this will send a message to the bank...*** can this be called on a thread even though it is already
     * running processing incoming messages?  should a make differe***
     * @param m message to send
     */
    public void sendMessage(Message m){
        //will just print for now but this should be
        try {
            out.writeObject(m);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Sending message to bank from house: " + m);

    }


    /**
     * this will just read objects  from the input stream and put them in the house's message queue
     */
    @Override
    public void run() {
        if(!isRegistered){
            System.out.println("the auction house has not been registered yet. Please start this thread AFTER you register with the back by using the .register() method");
            return;
        }
        Object o = null;
        //does a null get set when the socket or stream is broken on the other side?
        while(isRunning){
            try {
                //System.out.println(isRunning);
                o = in.readObject();
                if (o == null) {
                    break;
                }
                if(!(o instanceof Message)){
                    System.out.println("only accepting objects of Type Message");
                    throw new IOException();
                }
                houseMessageQueue.put((Message) o);
                //System.out.println(((Message) o));

            }
            catch(EOFException e){
                System.out.println("Bank Input stream  has been closed breaking out of the thread reading this data");
                return;
            }
            catch (IOException e) {
                if(!isRunning) {
                    System.out.println("gracefully cought IOException by closing the socket.");
                }else {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.out.println("The bank connection is exiting the thread will now stop running");
    }

    /**
     *This method bassically registers with the bank and opens a bank account
     *
     * @param name this is the name that will be used to open an account with the bank
     * @return this will return the bank account/id that was assigned to me by the bank
     */
    public int register(String name) {

        try {
            int id = -1;
            //request an account
            Message m = new MCreateAccount(name,0);
            sendMessage(m);
            Object o =  in.readObject();
            if(!(o instanceof Message)){
                System.out.println("Object is not of type Message throwing Exception");
                throw new IOException();
            }
            else if(o instanceof MAccountCreated){

                MAccountCreated message = (MAccountCreated) o;
                id = message.getAccountID();
                isRegistered = true;
                return id;
            }
            else{
                return -1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * we  shutdown by closing out and in and the socket.
     */
    public void shutDown() {
        //this will break out of the run.
        isRunning = false;
        try {
            //close the input and output streams
            out.close();
            in.close();
            //close the bank socket.
            bankSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
