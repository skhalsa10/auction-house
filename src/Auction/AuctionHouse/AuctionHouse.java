package Auction.AuctionHouse;

import Auction.Messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * this class defines an auction house. the action house will act as a server for agents.
 * and it will be a client of the bank. when this class gets initiated it starts a server socket.
 * It spawns a new thread where the whole goal is to listen and accept connections. this happens on a spperate thread.
 * This listener will broker socket connections and in turn start a new thread that works on this socket and reads in Message Objects
 * it then stores the messages in the auction houses messageQueue.
 *
 * The action House's main thread will just keep processing messages  accordingly until it gets a MessageClose in this case it will shut down the house.
 *
 */
public class AuctionHouse  extends Thread{

    //three items for sale
    private Item item1;
    private Item item2;
    private Item item3;
    //Item generator
    private ItemGenerator itemGenerator;
    //Auction House server socket
    private ServerSocket serverSocket;
    //client socket of the bank
    private Socket bankSocket;
    // queue for messages to process
    private LinkedBlockingQueue<Message> messageQueue;
    private AuctionHouseListener socketListener;
    private AuctionToBankConnection bankConnection;
    private int bankID;

    /**
     * this constructor instantiates a new AuctionHouse that connects to the bank host and bank port.
     * the third argumat is the port that this Auction House will start its server socket on
     * @param bankHost hostname of the bank server that we will connect to
     * @param bankPort port of the bank server we will connect to
     * @param housePort this is the port that the Auction house server will be listening on
     */
    public AuctionHouse(String bankHost, int bankPort, int housePort){
        //initialize some data
        itemGenerator = new ItemGenerator();
        item1 = itemGenerator.getItem();
        item2 = itemGenerator.getItem();
        item3 = itemGenerator.getItem();
        messageQueue = new LinkedBlockingQueue<>();

        //register with bank first before anything!
        registerWithBank(bankHost,bankPort);
        System.out.println(bankID);
        bankConnection.sendMessage(new Message(Message.RequestType.ACCEPT_BID));
        try {
            //this will set up the server
            serverSocket = new ServerSocket(housePort);
            //TODO build the client socket here
        } catch (IOException e) {
            e.printStackTrace();
        }

        socketListener = new AuctionHouseListener(serverSocket,messageQueue);
        socketListener.start();

    }

    /**
     * this registers a connection to the bank server
     * @param bankHost
     * @param bankPort
     */
    private void registerWithBank(String bankHost, int bankPort) {

        try {
            bankSocket= new Socket(bankHost, bankPort);
            bankConnection = new AuctionToBankConnection(bankSocket, messageQueue);
            bankID = bankConnection.register();
            bankConnection.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this thread will just process messages and stuff
     */
    @Override
    public void run() {
        Message m = null;
        do{
            try {
                m = messageQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while(m.getRequestType() != Message.RequestType.SHUT_DOWN);
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        FakeBank bank = new FakeBank();
        bank.start();
        AuctionHouse auctionHouse = new AuctionHouse(InetAddress.getLocalHost().getHostName(),7788,7777);
        auctionHouse.start();
        //Socket s1 = serve1.accept();
        //System.out.println("hi");

    }

}
