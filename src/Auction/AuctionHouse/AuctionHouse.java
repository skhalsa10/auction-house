package Auction.AuctionHouse;

import Auction.Messages.Message;
import Auction.Messages.MessageClose;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
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
    private LinkedBlockingQueue<Message> messageQueue;
    private AuctionHouseListener socketListener;

    /**
     * this constructor instantiates a new AuctionHouse that connects to the bank host and bank port.
     *
     * the third argumat is the port that this Auction House will start its server socket on
     * @param //bankHost hostname of the bank server that we will connect to
     * @param //bankPort port of the bank server we will connect to
     *                 //String bankHost, int bankPort,
     * @param housePort this is the port that the Auction house server will be listening on
     */
    public AuctionHouse( int housePort){
        try {
            //this will set up the server
            serverSocket = new ServerSocket(housePort);
            //TODO build the client socket here
        } catch (IOException e) {
            e.printStackTrace();
        }
        itemGenerator = new ItemGenerator();
        item1 = itemGenerator.getItem();
        item2 = itemGenerator.getItem();
        item3 = itemGenerator.getItem();
        messageQueue = new LinkedBlockingQueue<>();
        socketListener = new AuctionHouseListener(serverSocket,messageQueue);
        socketListener.start();

    }

    @Override
    public void run() {
        Message m = null;
        while(!(m instanceof MessageClose)){
            try {
                m = messageQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]) throws IOException {
        AuctionHouse ah = new AuctionHouse(7777);
        ah.start();
        System.out.println(InetAddress.getLocalHost());
        Socket test = new Socket(InetAddress.getLocalHost().getHostAddress(), 7777);
        System.out.println(test.isConnected());
        Socket test2 = new Socket(InetAddress.getLocalHost().getHostAddress(), 7777);
        System.out.println(test2.isConnected());
        ObjectOutputStream out = new ObjectOutputStream(test.getOutputStream());
        Message m = new MessageClose();
        out.writeObject(m);
    }

}
