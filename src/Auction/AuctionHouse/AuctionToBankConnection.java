package Auction.AuctionHouse;

import Auction.Messages.Message;

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

    public AuctionToBankConnection(Socket bankSocket, LinkedBlockingQueue<Message> houseMessageQueue){
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
     * running processing incoming messages? ***
     * @param m message to send
     */
    public void sendMessage(Message m){
        //will just print for now but this should be
        //try {
        //    out.writeObject(m);
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
        System.out.println(m);
    }

    /**
     * this will just read objects  from the input stream and put them in the house's message queue
     */
    @Override
    public void run() {
        Object o = null;
        //does a null get set when the socket or stream is broken on the other side?
        while(true){
            try {
                
                o = in.readObject();
                if (o == null) {
                    break;
                }
                if(!(o instanceof Message)){
                    System.out.println("only accepting objects of Type Message");
                    throw new IOException();
                }
                houseMessageQueue.put((Message) o);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
