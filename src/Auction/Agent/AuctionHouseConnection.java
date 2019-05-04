package Auction.Agent;

import Auction.Messages.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Connection between Agent and AuctionHouse
 */
public class AuctionHouseConnection implements Runnable {
    private Socket socket;
    private boolean connected = false;
    private LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<>();
    private ObjectInputStream in;
    private ObjectOutputStream out;

    /**
     * Constructs an AuctionHouseConnection
     * @param socket socket
     * @param messages agent message queue
     */
    public AuctionHouseConnection(Socket socket, LinkedBlockingQueue messages){
        this.messages = messages;
        try {
            this.socket = socket;
            connected = true;
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            new Thread(this).start();

        }
        catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Send message to auction house
     * @param m message to be sent
     */
    public void sendMessage(Message m) {
        try {
            out.writeObject(m);
        }
        catch(Exception e) {
            System.err.println(e);
        }

    }

    /**
     * Close this connection
     */
    public void closeConnection() {
        connected = false;
        try {
            closeAll();
        }
        catch (IOException e) {
            System.out.println("Connection to Auction House closed");
        }
    }

    /**
     * Close input, output and socket
     * @throws IOException
     */
    public void closeAll() throws IOException {
        //connected = false;
        try {
            out.close();
        }
        finally {
            try {
                in.close();
            }
            finally {
                socket.close();
            }
        }

    }


    /**
     * Runs connection
     */
    @Override
    public void run () {
        System.out.println("connected to house");
        Message receivedMessage;
        while(connected) {
            try {
                receivedMessage = (Message) in.readObject();
                if(receivedMessage != null) {
                    messages.put(receivedMessage);
                }
            }
            catch (IOException e) {
                System.out.println("Closed auction house connection");
                connected = false;
            }
            catch (Exception e) {
                System.err.println(e);
            }
        }
    }

}
