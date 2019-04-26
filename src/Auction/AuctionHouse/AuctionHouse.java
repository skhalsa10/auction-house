package Auction.AuctionHouse;

import Auction.Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
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
    private BidTracker tracker1;
    private BidTracker tracker2;
    private BidTracker tracker3;
    //Item generator
    private ItemGenerator itemGenerator;
    //Auction House server socket
    private ServerSocket serverSocket;
    //client socket of the bank
    private Socket bankSocket;
    private String houseName;
    // queue for messages to process
    private LinkedBlockingQueue<Message> messageQueue;
    private AuctionHouseListener socketListener;
    private AuctionToBankConnection bankConnection;
    private int myID;
    private HashMap<Integer, ObjectOutputStream> clientOuts;
    private ShutDownTimer shutDownTimer;
    private boolean isRunning;

    /**
     * this constructor instantiates a new AuctionHouse that connects to the bank host and bank port.
     * the third argumat is the port that this Auction House will start its server socket on
     * @param bankHost hostname of the bank server that we will connect to
     * @param bankPort port of the bank server we will connect to
     * @param housePort this is the port that the Auction house server will be listening on
     */
    public AuctionHouse(String houseName, String bankHost, int bankPort, int housePort){
        //initialize some data
        messageQueue = new LinkedBlockingQueue<>();
        clientOuts = new HashMap<>();
        shutDownTimer = new ShutDownTimer(messageQueue);
        isRunning = true;


        //register with bank first before anything!
        registerWithBank(bankHost,bankPort);

        itemGenerator = new ItemGenerator();
        tracker1 = new BidTracker(itemGenerator.getItem(),myID,2);
        tracker2 = new BidTracker(itemGenerator.getItem(),myID,2);
        tracker3 =new BidTracker(itemGenerator.getItem(),myID,2);

        try {
            //this will set up the server
            serverSocket = new ServerSocket(housePort);
            System.out.println(serverSocket.getInetAddress().getHostName());
            // send server info to the bank
            Message m = new MHouseServerInfo(myID, serverSocket.getInetAddress().getHostName(),housePort);
            bankConnection.sendMessage(m);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //this literally just listens on the house socket and brokers new connections
        socketListener = new AuctionHouseListener(serverSocket,messageQueue, clientOuts);
        socketListener.start();
        shutDownTimer.start();

    }

    /**
     * this registers a connection to the bank server
     * @param bankHost
     * @param bankPort
     */
    private void registerWithBank(String bankHost, int bankPort) {

        try {
            //first lets connect a socket to the bank
            bankSocket= new Socket(bankHost, bankPort);
            //we will now beild the bank connection which handles all the communication outbound to the bank
            bankConnection = new AuctionToBankConnection(bankSocket, messageQueue);
            myID = bankConnection.register(houseName);
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
        while(isRunning){
            try {
                System.out.println(isRunning);
                m = messageQueue.take();
                if (m instanceof MShutDown) {
                    break;
                }
                processMessage(m);

            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("ISTHIS HERE AT ALL");
                //e.printStackTrace();
                //return;
            }
        }
        System.out.println("I exited the main thread?");
        //TODO Shutdown code here
    }

    private void processMessage(Message m) {
        System.out.println("processing message from main House thread. M request type is: " + m);

        if(m instanceof MRequestItems){
            MRequestItems m2 = (MRequestItems) m;
            if(clientOuts.get(m2.getAgentID()) == null){
                System.out.println("error there is no agentID " + m2.getAgentID() + " in clientouts");
            }
            ArrayList<BidTracker> list = new ArrayList<>();
            list.add(tracker1.clone());
            list.add(tracker2.clone());
            list.add(tracker3.clone());
            try {
                clientOuts.get(m2.getAgentID()).writeObject(new MItemList(myID,list));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(m instanceof MBid){
            MBid m2 = (MBid) m;
            MBlockFunds mbf = new MBlockFunds(myID,m2.getAgentID(),m2.getItemID(),m2.getBidAmount());
            bankConnection.sendMessage(mbf);
        }
        else if(m instanceof MBlockAccepted){
            MBlockAccepted m2 = (MBlockAccepted) m;
            BidTracker t = null;
            //find the correct tracker
            if(m2.getItemID() == tracker1.getItem().getID()){
                t = tracker1;
            }
            if(m2.getItemID() == tracker2.getItem().getID()){
                t = tracker2;
            }
            if(m2.getItemID() == tracker3.getItem().getID()){
                t = tracker3;
            }
            int oldWinner = t.getBidOwnerID();
            if(t.setBid(m2.getAmount(),m2.getAgentID())){
                try {
                    clientOuts.get(m2.getAgentID()).writeObject(new MBidAccepted(myID,t));
                    if(oldWinner >= 0) {
                        clientOuts.get(oldWinner).writeObject(new MBidOutbid(myID, t));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    clientOuts.get(m2.getAgentID()).writeObject(new MBidRejected(myID,t));
                    bankConnection.sendMessage(new MUnblockFunds(myID,m2.getAgentID(),m2.getAmount()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(m instanceof MBlockRejected){
            MBlockRejected m2 = (MBlockRejected) m;
            BidTracker t = null;
            //find the correct tracker
            if(m2.getItemID() == tracker1.getItem().getID()){
                t = tracker1;
            }
            if(m2.getItemID() == tracker2.getItem().getID()){
                t = tracker2;
            }
            if(m2.getItemID() == tracker3.getItem().getID()){
                t = tracker3;
            }
            try {
                clientOuts.get(m2.getAgentID()).writeObject(new MBidRejected(myID,t));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(m instanceof MHouseClosedTimer){
            //here I will send out messages to everything I am closing and close all connections
            isRunning = false;
            bankConnection.sendMessage(new MShutDown(myID));
            bankConnection.shutDown();
            socketListener.shutDown();

        }
        else{
            System.out.println("dont know how to process this message: " + m);
        }
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException {

        AuctionHouse auctionHouse = new AuctionHouse("Ted's Store","0.0.0.0",7788,7777);
        auctionHouse.start();
        //Socket s1 = serve1.accept();

    }

}
