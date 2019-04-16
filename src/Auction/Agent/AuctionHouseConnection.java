package Auction.Agent;

import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AuctionHouseConnection implements Runnable {

    private String hostName;
    private int portNumber;
    private Socket socket;
    public AuctionHouseConnection(String hostName, int portNumber){
        this.hostName = hostName;
        this.portNumber = portNumber;
        start();
    }

    public void start() {
        try {
            this.socket = new Socket(hostName, portNumber);
        }
        catch(Exception e) {
            System.err.println(e);
        }
    }
    public void run () {}

}
