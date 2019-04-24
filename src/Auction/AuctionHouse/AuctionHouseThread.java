package Auction.AuctionHouse;

import Auction.Messages.MShutDown;
import Auction.Messages.Message;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
    private Boolean isRegistered;
    private int agentID;

    public AuctionHouseThread(Socket socket, LinkedBlockingQueue<Message> messageQueue,HashMap<Integer, ObjectOutputStream> clientOuts){
        super("AuctionHouseThread");
        this.socket = socket;
        this.messageQueue = messageQueue;
        this.clientOuts = clientOuts;
        isRegistered = false;
    }

    /**
     * this thread will listen for data on the socket it will read in a message object and store it in the auction house queue
     */
    @Override
    public void run() {

        try {
            ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            while(true){
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
                if(m instanceof MShutDown){
                    //socket.close();
                    //TODO should I be placing this message also in the queue for the house to delete?
                    break;
                }
                System.out.println("Message received from agent: " + m);
                messageQueue.put(m);
            }
            objectIn.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
