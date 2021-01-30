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

    private boolean shuttingDown;

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
        isRunning = true;
        this.houseName = houseName;
        this.houseHost = houseHost;
        shuttingDown = false;


        //register with bank first before anything!
        //this will get a bank account and initialize a AuctionToBankCommunication object
        registerWithBank(bankHost,bankPort);

        //Binitialize item related stuff
        itemGenerator = new ItemGenerator();
        tracker1 = new BidTracker(itemGenerator.getItem(),myID,2);
        tracker2 = new BidTracker(itemGenerator.getItem(),myID,2);
        tracker3 =new BidTracker(itemGenerator.getItem(),myID,2);
        itemWonTimer1 = new ItemWonTimer(messageQueue,tracker1);
        itemWonTimer2 = new ItemWonTimer(messageQueue,tracker2);
        itemWonTimer3 = new ItemWonTimer(messageQueue,tracker3);

        try {
            //this will set up the server portion of the house
            serverSocket = new ServerSocket(housePort);
            //;et the bank know about my server info
            Message m = new MHouseServerInfo(myID,houseHost,housePort);
            bankConnection.sendMessage(m);
        }catch(BindException e){
            System.out.println("This port is already being used by a different house server socket. Check the port parameter!");
            return;
        }  catch (IOException e) {
            e.printStackTrace();
        }

        //this literally just listens on the house socket and brokers new connections
        socketListener = new AuctionHouseListener(serverSocket,messageQueue, clientOuts);
        socketListener.start();

    }

    /**
     * this registers a connection to the bank server
     * @param bankHost bank hostname to connect to
     * @param bankPort bank port number to connect to
     */
    private void registerWithBank(String bankHost, int bankPort) {

        try {
            //first lets connect a socket to the bank
            System.out.println("Connecting to Bank Hose: " + bankHost + " on port #" + bankPort);
            bankSocket= new Socket(bankHost, bankPort);
            //we will now beild the bank connection which handles all the communication outbound to the bank
            bankConnection = new AuctionToBankConnection(bankSocket, messageQueue);
            //need to register before we start the thread
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
        System.out.println("Leaving the auction house " + myID + " thread");
    }

    /**
     * This method process the Message m type and performs the appropriate tasks
     * This is the meat of the logic for this application
     * @param m message to process
     */
    private void processMessage(Message m) {
        System.out.println("processing message from main House thread. M request type is: " + m);
        if(m instanceof MRequestItems){
            MRequestItems m2 = (MRequestItems) m;
            //if we are in the process of shutting down ignore this message
            if(shuttingDown){ return;}
            //this should ner really run unless output streams are notgetting removed
            if(clientOuts.get(m2.getAgentID()) == null){
                System.out.println("error there is no agentID " + m2.getAgentID() + " in clientouts");
            }

            //build a temporary list of clones to send out
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
            //we dont care about this message
            return;
        }
        else if (m instanceof MShutDown) {
            //time to shut something down!
            MShutDown msd = (MShutDown)m;
            //System.out.println("SHU " + ((MShutDown) m).getID());
            if(msd.getID()<0){
                //the bank has shut down shut down everything else
                isRunning = false;
                socketListener.shutDown();
            }
            else if(msd.getID() ==myID){
                //first we should be in a state of shutting down if I receive this message
                if(!shuttingDown){
                    System.out.println("UM i should not be getting a shut down message from " +
                            "myself if I am not in the process of shutting down!");
                    return;
                }
                else{
                    //this is a shut down of the house
                    isRunning = false;
                    itemWonTimer1.shutdown();
                    itemWonTimer2.shutdown();
                    itemWonTimer3.shutdown();

                    //okay now that we are here we have already processed all messages
                    //that came in during the time we initially started the shutdown we can
                    //close all client outs and the sockets. client ins are assumed to be
                    // shut down in the initial phase of the shutdown
                    for(ObjectOutputStream out: clientOuts.values()){
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    //now that all output streams are closed we can close all the client sockets
                    socketListener.shutDown();
                    //now lets close our communication with the bank
                    bankConnection.shutDown();

                }
            }
            else{
                //if we get here we are only processing the shutdown
                // of a client but we still need to stay alive for others
                socketListener.shutDownClient(msd.getID());
            }
        }
        else if(m instanceof MBid){
            MBid m2 = (MBid) m;
            //first check to see if the house is in the process of shutting down if so reject bid immediately
            if(shuttingDown){
                try {
                    clientOuts.get(m2.getAgentID()).writeObject(new MBidRejected(myID,new BidTracker(new Item("House Shutting Down",m2.getItemID()),myID,2)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
            //if we are not shutting down lets request some funds to be blocked for this agent
            MBlockFunds mbf = new MBlockFunds(myID,m2.getAgentID(),m2.getItemID(),m2.getBidAmount(),houseName);
            bankConnection.sendMessage(mbf);
        }
        else if(m instanceof MBlockAccepted){

            MBlockAccepted m2 = (MBlockAccepted) m;
            //if shutting down we should unblock funds and send bid rejected
            if(shuttingDown){
                bankConnection.sendMessage(new MUnblockFunds(myID,m2.getAgentID(),m2.getAmount()));
                try {
                    clientOuts.get(m2.getAgentID()).writeObject(new MBidRejected(myID,new BidTracker(new Item("House Shutting Down",m2.getItemID()),myID,2)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }

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
            //store the old winner and old amount just in case we need to unblock funds and notify of outbid
            int oldWinner = t.getBidOwnerID();
            int oldAmount = t.getCurrentBid();
            if(t.setBid(m2.getAmount(),m2.getAgentID())){
                //if we were able to set new bid owner
                try {
                    //inform new owner
                    clientOuts.get(m2.getAgentID()).writeObject(new MBidAccepted(myID,t));
                    //and let the old bid owner know they got outbid
                    if(oldWinner >= 0) {
                        bidTimer.restart();
                        bankConnection.sendMessage(new MUnblockFunds(myID, oldWinner, oldAmount));
                        clientOuts.get(oldWinner).writeObject(new MBidOutbid(myID, t));
                    }
                    else{
                        //this is the case if it is the first bid on an item
                        bidTimer.start();
                    }
                    //now that there is a new owner lets sendout a message blast to all clients so they have updated information
                    ArrayList<BidTracker> list = new ArrayList<>();
                    list.add(tracker1.clone());
                    list.add(tracker2.clone());
                    list.add(tracker3.clone());
                    for(Integer i: clientOuts.keySet()){
                        clientOuts.get(i).writeObject(new MItemList(myID,list));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                //if we get here the bid amount was not more than the minimum bid needed to place the bet
                //unblock the funds and send rejected message
                try {
                    clientOuts.get(m2.getAgentID()).writeObject(new MBidRejected(myID,t));
                    bankConnection.sendMessage(new MUnblockFunds(myID,m2.getAgentID(),m2.getAmount()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(m instanceof MBlockRejected){

            //inform the agent of the rejected bid
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

            //first check to see if there are any bids pending if there are ignore message.
            if( itemWonTimer1.isRunning() || itemWonTimer2.isRunning() ||itemWonTimer3.isRunning()){
                System.out.println("tried processing shut down but Bids are pending! try later person");
                return;
            }
            shuttingDown = true;

            //Send a final message that I am shutting down to all clients
            for(ObjectOutputStream out : clientOuts.values()){
                try {
                    out.writeObject(new MShutDown(myID, houseName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //tell the bank I am shutting down
            bankConnection.sendMessage(new MShutDown(myID, houseName));

            //now I need to close all client incoming streams
            socketListener.shutdownClientIns();

            //I still need to process all remaining messages that may have came in after this so I will leave the outs
            //open. but to signal the end of what should be processed i will send myself a a shutdown messsage
            try {
                messageQueue.put(new MShutDown(myID,houseName));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


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
                itemWonTimer1.shutdown();
                itemWonTimer1 = new ItemWonTimer(messageQueue,tracker1);
            }
            if(tracker2.getItem().getID() == m2.getItemWon().getItem().getID()){
                tracker2 = new BidTracker(itemGenerator.getItem(),myID,2);
                itemWonTimer2.shutdown();
                itemWonTimer2 = new ItemWonTimer(messageQueue,tracker2);
            }
            if(tracker3.getItem().getID() == m2.getItemWon().getItem().getID()){
                tracker3 =new BidTracker(itemGenerator.getItem(),myID,2);
                itemWonTimer3.shutdown();
                itemWonTimer3 = new ItemWonTimer(messageQueue,tracker3);
            }
            //last thing I need to do is blast out a new message to all the agents about the new item list
            ArrayList<BidTracker> items = new ArrayList<>();
            items.add(tracker1.clone());
            items.add(tracker2.clone());
            items.add(tracker3.clone());
            MItemList Mil = new MItemList(myID,items);
            for(Integer i: clientOuts.keySet()){
                try {
                    clientOuts.get(i).writeObject(Mil);
                }catch(SocketException e){
                    System.out.println("cant send to this out. the agent probably closed extremely quickly after winning an Item");
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
    public void shutDown() {
        System.out.println("exit typed");
        try {
            messageQueue.put(new MHouseClosedTimer());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isShuttingDown() {
        return shuttingDown;
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

        //Enter data using BufferReader
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String cmd;
        while(!auctionHouse.isShuttingDown()){
            System.out.println("are we blocking?");
            // Reading data using readLine
            cmd = reader.readLine();
            if(cmd.equals("exit")){
               auctionHouse.shutDown();
            }
        }
        reader.close();
        System.out.println("house main exited");

    }



}
