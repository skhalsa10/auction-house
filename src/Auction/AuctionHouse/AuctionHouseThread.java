package Auction.AuctionHouse;

import Auction.Messages.MBid;
import Auction.Messages.MRequestItems;
import Auction.Messages.MShutDown;
import Auction.Messages.Message;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * the purpose of this class is encapsulate a stream to this server over a socket.
 * It will literally just read objects and put them into the messageQueue for the Auction house
 *
 * This pretty much handles the input stream from the Clients (agents) after the first message is received it adds the output
 * stream to the clientOuts hash
 *
 * @author Siri Khalsa
 * @version 2 this version converts the message to incorporate Abstract type
 */
public class AuctionHouseThread extends Thread {
    private Socket socket;
    private LinkedBlockingQueue<Message> messageQueue;
    private HashMap<Integer, ObjectOutputStream> clientOuts;
    private boolean isRegistered;
    private boolean isRunning;
    private int clientID;
    ObjectInputStream objectIn = null;
    ObjectOutputStream out = null;

    public AuctionHouseThread(Socket socket, LinkedBlockingQueue<Message> messageQueue,HashMap<Integer, ObjectOutputStream> clientOuts){
        super("AuctionHouseThread");
        this.socket = socket;
        this.messageQueue = messageQueue;
        this.clientOuts = clientOuts;
        isRegistered = false;
        isRunning = true;
        clientID = -1;
    }

    /**
     *
     * @return the client ID associatied with this thread
     */
    public int getClientID() {
        return clientID;
    }

    /**
     * this thread will listen for data on the socket it will read in a
     *  message object and store it in the auction house queue
     */
    @Override
    public void run() {

        try {
            objectIn = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            while(isRunning){
                Object o = objectIn.readObject();
                if(o == null){
                    System.out.println("I think the client socket closed? I got a NULL object");
                    throw new EOFException();
                }
                if(!(o instanceof Message)){
                    System.out.println("not of type message");
                    throw new IOException();
                }
                Message m = (Message) o;
                /*if(m instanceof MShutDown){
                    //socket.close();
                    //TODO should I be placing this message also in the queue for the house to delete?
                    break;
                }*/
                System.out.println("Message received from agent: " + m);
                if(!isRegistered) {
                    addToOuts(m, out);
                }
                messageQueue.put(m);
            }
        }
        catch(EOFException e){
            System.out.println("client closed their stream before I could.");
        }
        catch (SocketException e){
            System.out.println("Client " + clientID + " socket closed!");
        }
        catch (IOException e) {
            if(!isRunning) {
                System.out.println("gracefully cought Client " + clientID + " IOException");
            }else {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("leaving a client thread's run");

    }

    /**
     * this will take a message the clients ID from the message and map that id to the output stream in the clientOuts map.
     * @param m
     * @param out
     */
    private void addToOuts(Message m, ObjectOutputStream out) {
        if(m instanceof MBid){
            MBid m2 = (MBid) m;
            this.clientID = m2.getAgentID();
            clientOuts.put(m2.getAgentID(),out);
        }
        else if(m instanceof MRequestItems){
            MRequestItems m2 = (MRequestItems) m;
            this.clientID = m2.getAgentID();
            clientOuts.put(m2.getAgentID(), out);
        }
        else{
            System.out.println("addToOuts cant handle this message " + m);
            return;
        }
        isRegistered = true;
    }

    /**
     * this method assumes that all input and output streams have been closed gracefully.
     * this will tell the thread to stop running and then close the clients socket
     */
    public void shutDown() {
        isRunning = false;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * this method will close the input stream
     */
    public void shutDownIn() {
        try {
            objectIn.close();
            //TODO will this cause an error to clos twice?
            objectIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
