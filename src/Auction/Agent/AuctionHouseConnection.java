package Auction.Agent;

import Auction.Messages.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AuctionHouseConnection implements Runnable {

    private String hostName;
    private int portNumber;
    private Socket socket;
    private boolean connected = false;
    private LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<>();
    private ObjectInputStream in;
    private ObjectOutputStream out;
    public AuctionHouseConnection(String hostName, int portNumber){
        this.hostName = hostName;
        this.portNumber = portNumber;
        connect();
    }

    public void sendMessage(Message m) {
        try {
            messages.put(m);
        }
        catch(Exception e) {
            System.err.println(e);
        }
    }

    public void connect() {
        try {
            this.socket = new Socket(hostName, portNumber);
            connected = true;
            in = new ObjectInputStream(this.socket.getInputStream());
            out = new ObjectOutputStream(this.socket.getOutputStream());

        }
        catch(Exception e) {
            System.err.println(e);
        }
    }
    @Override
    public void run () {
        while(connected) {
            try {
                Message m = messages.take();
                out.writeObject(m);
            }
            catch (Exception e) {
                System.err.println(e);
            }
        }
    }

}
