package Auction.AuctionHouse;

import Auction.Messages.Message;
import Auction.Messages.MessageClose;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * the purpose of this class is encapsulate a stream to this server over a socket.
 * It will literally just read objects and put them into the messageQueue for the Auction house
 */
public class AuctionHouseThread extends Thread {
    private Socket socket;
    private LinkedBlockingQueue<Message> messageQueue;

    public AuctionHouseThread(Socket socket, LinkedBlockingQueue<Message> messageQueue){
        super("AuctionHouseThread");
        this.socket = socket;
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {

        try {
            ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream());
            System.out.println("hio");
            while(true){
                System.out.println("blocking?");
                Object o = objectIn.readObject();
                if(!(o instanceof Message)){
                    System.out.println("not of type message");
                    throw new IOException();
                }
                Message m = (Message) o;
                if(m instanceof MessageClose){
                    break;
                }
                messageQueue.put(m);
            }
            objectIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
