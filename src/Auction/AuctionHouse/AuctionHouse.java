package Auction.AuctionHouse;

import Auction.Messages.Message;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * this class defines an auction house. the action house will act as a server for agents.
 * and it will be a client of the bank. It will listen
 */
public class AuctionHouse  extends Thread{
    private Item item1;
    private Item item2;
    private Item item3;
    private ItemGenerator itemGenerator;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private LinkedBlockingQueue messageQueue;

    /**
     * this constructor instantiates a new AuctionHouse that connects to the bank host and bank port.
     *
     * the third argumat is the port that this Auction House will start its server socket on
     * @param bankHost hostname of the bank server that we will connect to
     * @param bankPort port of the bank server we will connect to
     * @param housePort this is the port that the Auction house server will be listening on
     */
    public AuctionHouse(String bankHost, int bankPort, int housePort){
        try {
            //this will set up the server
            serverSocket = new ServerSocket(housePort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        itemGenerator = new ItemGenerator();
        item1 = itemGenerator.getItem();
        item2 = itemGenerator.getItem();
        item3 = itemGenerator.getItem();

    }

    @Override
    public void run() {
        Message m = null;
        while(!(m instanceof MessageClose))
    }

    public static void main(String args[]) throws IOException {
        AuctionHouse ah = new AuctionHouse();
        ah.start();
        System.out.println(InetAddress.getLocalHost());
        Socket test = new Socket(InetAddress.getLocalHost().getHostAddress(), 7777);
        System.out.println(test.isConnected());
        Socket test2 = new Socket(InetAddress.getLocalHost().getHostAddress(), 7777);
        System.out.println(test2.isConnected());
    }

}
