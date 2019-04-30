package Auction.AuctionHouse;

import Auction.Messages.*;

import java.io.*;
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

    private final String houseHost;
    //three items for sale
    private BidTracker tracker1;
    private BidTracker tracker2;
    private BidTracker tracker3;
    //three item bid winner timers
    private ItemWonTimer itemWonTimer1;
    private ItemWonTimer itemWonTimer2;
    private ItemWonTimer itemWonTimer3;
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
    //private ShutDownTimer shutDownTimer;
    private boolean isRunning;


    /**
     * this constructor instantiates a new AuctionHouse that connects to the bank host and bank port.
     * the third argumat is the port that this Auction House will start its server socket on
     * @param bankHost hostname of the bank server that we will connect to
     * @param bankPort port of the bank server we will connect to
     * @param houseHost  this is the hostname of the house
     * @param housePort this is the port that the Auction house server will be listening on
     */
    public AuctionHouse(String houseName, String bankHost, int bankPort, String houseHost, int housePort){
        //initialize some data
        messageQueue = new LinkedBlockingQueue<>();
        clientOuts = new HashMap<>();
        //shutDownTimer = new ShutDownTimer(messageQueue);
        isRunning = true;
        this.houseName = houseName;
        this.houseHost = houseHost;


        //register with bank first before anything!
        registerWithBank(bankHost,bankPort);

        itemGenerator = new ItemGenerator();
        tracker1 = new BidTracker(itemGenerator.getItem(),myID,2);
        tracker2 = new BidTracker(itemGenerator.getItem(),myID,2);
        tracker3 =new BidTracker(itemGenerator.getItem(),myID,2);
        itemWonTimer1 = new ItemWonTimer(messageQueue,tracker1);
        itemWonTimer2 = new ItemWonTimer(messageQueue,tracker2);
        itemWonTimer3 = new ItemWonTimer(messageQueue,tracker3);

        try {
            //this will set up the server
            serverSocket = new ServerSocket(housePort);
            Message m = new MHouseServerInfo(myID,houseHost,housePort);
            bankConnection.sendMessage(m);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //this literally just listens on the house socket and brokers new connections
        socketListener = new AuctionHouseListener(serverSocket,messageQueue, clientOuts);
        socketListener.start();

    }

    /**
     * this registers a connection to the bank server
     * @param bankHost
     * @param bankPort
     */
    private void registerWithBank(String bankHost, int bankPort) {

        try {
            //first lets connect a socket to the bank
            System.out.println(bankHost + " " + bankPort);
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
                //System.out.println(isRunning);
                m = messageQueue.take();
                processMessage(m);

            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Int");

            }
        }
        System.out.println("Leaving the main auction house thread");
        //TODO Shutdown code here
    }

    /**
     * This method process the Message m type and performs the appropriate tasks
     * @param m
     */
    private void processMessage(Message m) {
        System.out.println("processing message from main House thread. M request type is: " + m);
        if(m instanceof MRequestItems){
            System.out.println("before the restart");
            //shutDownTimer.restart();
            System.out.println("passed the restart?");
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
        else if(m instanceof MAuctionHouses){
            return;
        }
        else if (m instanceof MShutDown) {
            MShutDown msd = (MShutDown)m;
            if(msd.getID()<0){
                //the bank has shut down shut down everything else
                isRunning = false;
                socketListener.shutDown();
                //shutDownTimer.shutdown();
            }
            else if(msd.getID() ==myID){
                System.out.println("Mshutdown id is myID is this right?");
            }
            else{
                socketListener.shutDownClient(msd.getID());
            }
        }
        else if(m instanceof MBid){
            //shutDownTimer.restart();
            MBid m2 = (MBid) m;
            MBlockFunds mbf = new MBlockFunds(myID,m2.getAgentID(),m2.getItemID(),m2.getBidAmount(),houseName);
            bankConnection.sendMessage(mbf);
        }
        else if(m instanceof MBlockAccepted){
            //shutDownTimer.restart();
            MBlockAccepted m2 = (MBlockAccepted) m;
            BidTracker t = null;
            ItemWonTimer bidTimer = null;
            //find the correct tracker
            if(m2.getItemID() == tracker1.getItem().getID()){
                t = tracker1;
                bidTimer = itemWonTimer1;
            }
            else if(m2.getItemID() == tracker2.getItem().getID()){
                t = tracker2;
                bidTimer = itemWonTimer2;
            }
            else if(m2.getItemID() == tracker3.getItem().getID()){
                t = tracker3;
                bidTimer = itemWonTimer3;
            } else {
                //if we get here the item was won during the time this message processed unblock funds and send bid rejected message
                bankConnection.sendMessage(new MUnblockFunds(myID,m2.getAgentID(),m2.getAmount()));
                try {
                    clientOuts.get(m2.getAgentID()).writeObject(new MBidRejected(myID, new BidTracker(new Item("SOLD",m2.getItemID()),myID,2)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
            //store the old winner
            int oldWinner = t.getBidOwnerID();
            System.out.println("bidowner before setting: " + t.getBidOwnerID());
            if(t.setBid(m2.getAmount(),m2.getAgentID())){
                try {
                    clientOuts.get(m2.getAgentID()).writeObject(new MBidAccepted(myID,t));
                    if(oldWinner >= 0) {
                        bidTimer.restart();
                        clientOuts.get(oldWinner).writeObject(new MBidOutbid(myID, t));
                    }
                    else{
                        bidTimer.start();
                    }
                    ArrayList<BidTracker> list = new ArrayList<>();
                    list.add(tracker1.clone());
                    list.add(tracker2.clone());
                    list.add(tracker3.clone());
                    for(ObjectOutputStream out:clientOuts.values()){
                        out.writeObject(new MItemList(myID,list));
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
            System.out.println("bidowner after setting: " + t.getBidOwnerID());
        }
        else if(m instanceof MBlockRejected){
            //shutDownTimer.restart();
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
            bankConnection.sendMessage(new MShutDown(myID, houseName));
            bankConnection.shutDown();
            socketListener.shutDown();
            //shutDownTimer.shutdown();

        }
        else if(m instanceof MHouseWonTimer){
            //send winner a message, generate a new item and blast our new itemlist messages
            MHouseWonTimer m2 = (MHouseWonTimer)m;
            //send a message to the agent that won
            MBidWon wonM = new MBidWon(myID, m2.getItemWon());
            try {
                clientOuts.get(m2.getAgentID()).writeObject(wonM);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //get a new tracker for the won that won
            if(tracker1.getItem().getID() == m2.getItemWon().getItem().getID()){
                tracker1 = new BidTracker(itemGenerator.getItem(),myID,2);
                itemWonTimer1 = new ItemWonTimer(messageQueue,tracker1);
            }
            if(tracker2.getItem().getID() == m2.getItemWon().getItem().getID()){
                tracker2 = new BidTracker(itemGenerator.getItem(),myID,2);
                itemWonTimer2 = new ItemWonTimer(messageQueue,tracker2);
            }
            if(tracker3.getItem().getID() == m2.getItemWon().getItem().getID()){
                tracker3 =new BidTracker(itemGenerator.getItem(),myID,2);
                itemWonTimer3 = new ItemWonTimer(messageQueue,tracker3);
            }
            //last thing I need to do is blast out a new message to all the agents about the new item list
            ArrayList<BidTracker> items = new ArrayList<>();
            items.add(tracker1.clone());
            items.add(tracker2.clone());
            items.add(tracker3.clone());
            MItemList Mil = new MItemList(myID,items);
            for(ObjectOutputStream out: clientOuts.values()){
                try {
                    out.writeObject(Mil);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        else{
            System.out.println("dont know how to process this message: " + m);
        }
    }

    /**
     * this method just tries to shutdown the house.
     * it first checks to see if there are any items
     * @return
     */
    public boolean shutDown() {
        return true;
    }

    /**
     * this application will take  4 parameters to start
     * 1. the name of the auction house
     * 2. the host name of the bank
     * 3. the Bank's port number
     * 4. the House's Hostname
     * 5. the House's port number
     *
     * @param args
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void main(String args[]) throws IOException, ClassNotFoundException {

        if(args.length != 5){
            System.out.println("USAGE: auctionHouse.jar [House Name] [Bank Host Name] [Bank Port #] [House Host name] [House Port #]");
            return;
        }

        AuctionHouse auctionHouse = new AuctionHouse(args[0],args[1],Integer.parseInt(args[2]),args[3],Integer.parseInt(args[4]));

        auctionHouse.start();

        boolean running = true;
        //Enter data using BufferReader
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while(running){
            // Reading data using readLine
            String cmd = reader.readLine();
            if(cmd == "exit"){
                running = auctionHouse.shutDown();
            }
        }

    }


}
