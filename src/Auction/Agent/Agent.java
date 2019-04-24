package Auction.Agent;

import Auction.AuctionHouse.Item;
import Auction.GUI.GUI;
import Auction.GUI.GUIMessages.GUIMessageLoaded;
import Auction.Messages.MCreateAccount;
import Auction.Messages.MRequestItems;
import Auction.Messages.Message;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class for Agent
 */
public class Agent implements Runnable {
    private String name;
    private int balance;
    private int agentID;
    private int availFunds;
    private LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<>();
    private String bankHost;
    private int bankPortNum;
    private BankConnection bankConnection;
    private HashMap<Integer, Boolean> connectedHouses = new HashMap<>();
    private HashMap<Integer, AuctionHouseConnection> auctionHouses = new HashMap<>();
    private GUI gui;

    /**
     * Constructs an Agent
     * @param bankHost bank host name
     * @param bankPortNum bank port number
     * @param name agent name
     * @param initialBalance agent initial balance
     * @param gui gui
     */
    public Agent(String bankHost, int bankPortNum, String name, int initialBalance, GUI gui) {
        this.bankHost = bankHost;
        this.bankPortNum = bankPortNum;
        this.name = name;
        this.balance = initialBalance;
        this.gui = gui;
        new Thread(this).start();
    }

    /**
     * Opens a bank account by sending a message to bank
     */
    private void openBankAccount(){
        MCreateAccount m = new MCreateAccount(name,balance);
        bankConnection.sendMessage(m);
    }

    /**
     * Sends a list of houses to gui
     */
    public void sendHouseList() {
        ArrayList<Integer> houseIds = new ArrayList<>();
        houseIds.addAll(auctionHouses.keySet());
        GUIMessageLoaded loadedM = new GUIMessageLoaded(houseIds);
        gui.sendMessage(loadedM);
    }

    /**
     * Sets an auction house with a connection
     * @param houseId id of house
     * @param hostname house host name
     * @param portNum house port number
     */
    private void setAuctionHouse(int houseId, String hostname, int portNum) {
        try {
            Socket socket = new Socket(hostname, portNum);
            AuctionHouseConnection connection = new AuctionHouseConnection(socket, messages);
            auctionHouses.put(houseId, connection);
        }
        catch (Exception e) {
            System.err.println(e);
        }

    }

    /**
     * Gets the message queue
     * @return message queue
     */
    public LinkedBlockingQueue<Message> getMessages(){
        return messages;
    }

    /**
     * Sends bank account number to gui
     */
    public void sendBankAccount() {
        //send bank account # to gui

    }

    /**
     * Sends updated balance to gui
     */
    private void sendBalance() {

    }

    /**
     * Sends available funds to gui
     */
    private void sendAvailableFunds() {}

    /**
     * Choose an auction house to connect to
     * @param houseID id of auction house to be connected to
     */
    private void chooseAuctionHouse(int houseID) {
        boolean alreadyConnected = connectedHouses.get(houseID);
        AuctionHouseConnection connection;
        if(!alreadyConnected) {
            connection = auctionHouses.get(houseID);
            connectToAuctionHouse(connection);
        }

    }

    /**
     * Connect to auction house
     * @param connection connection of auction house
     */
    private void connectToAuctionHouse(AuctionHouseConnection connection){
        new Thread(connection).start();
        MRequestItems m = new MRequestItems(agentID);
        connection.sendMessage(m);
    }

    /**
     * Connect to bank
     * @param bankHost bank host name
     * @param bankPort bank port number
     */
    private void connectToBank(String bankHost, int bankPort) {
        bankConnection = new BankConnection(bankHost, bankPort, messages);
        new Thread(bankConnection).start();

    }

    /**
     * Processes a message
     */
    private void processMessage() {
        Message receivedMessage;
        try {
            receivedMessage = messages.take();
            System.out.println(receivedMessage.toString());
        }
        catch(Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Sends message to house to bid on an item
     * @param amount bid amount
     * @param bidItem item to bid on
     */
    // maybe use item id
    private void bidOnItem(int amount, Item bidItem) {

    }

    /**
     * Send message to bank to transfer funds
     * @param amount transfer amount
     * @param houseAccountNum house bank account number
     */
    private void transferFunds(int amount, int houseAccountNum){

    }

    /**
     * Sends message to gui about bid status message
     */
    private void sendStatusMessage() {

    }


    /**
     * Runs agent thread that processes messages
     */
    @Override
    public void run() {
        connectToBank(bankHost, bankPortNum);
        openBankAccount();
        while(true) {
            processMessage();
        }

    }

    public static void main(String[] args) {
        String bankHost;
        int bankPortNum;
        String name;
        int initialBalance;

        if(args.length == 4) {
            bankHost = args[0];
            bankPortNum = Integer.parseInt(args[1]);
            name = args[2];
            initialBalance = Integer.parseInt(args[3]);

            //Agent a = new Agent(bankHost, bankPortNum, name, initialBalance);

        }

    }
}
