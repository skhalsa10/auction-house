package Auction.Agent;

import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AuctionHouseConnection implements Runnable {

    private String hostName;
    private int portNumber;
    private Socket socket;
    private boolean connected = false;
    private LinkedBlockingQueue<Messages> messages = new LinkedBlockingQueue<>();

    public AuctionHouseConnection(String hostName, int portNumber){
        this.hostName = hostName;
        this.portNumber = portNumber;
        connect();
    }

    public void connect() {
        try {
            this.socket = new Socket(hostName, portNumber);
            connected = true;
        }
        catch(Exception e) {
            System.err.println(e);
        }
    }
    @Override
    public void run () {
        while(connected) {

        }
    }

}
