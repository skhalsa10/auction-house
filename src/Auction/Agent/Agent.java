package Auction.Agent;

import Auction.AuctionHouse.BidTracker;
import Auction.AuctionHouse.Item;
import Auction.GUI.GUI;
import Auction.GUI.GUIMessages.*;
import Auction.Messages.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
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
    private HashMap<Integer, AuctionHouseConnection> auctionHouses = new HashMap<>();
    private GUI gui;
    private int ongoingBids = 0;

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
        GUIMessageAccount accountM = new GUIMessageAccount(agentID);
        gui.sendMessage(accountM);
    }

    /**
     * Sends updated balance to gui
     */
    private void sendBalance() {
        GUIMessageBalance balanceM = new GUIMessageBalance(balance);
        gui.sendMessage(balanceM);
    }

    /**
     * Sends available funds to gui
     */
    private void sendAvailableFunds() {
        GUIMessageAvailableFunds fundsM = new GUIMessageAvailableFunds(availFunds);
        gui.sendMessage(fundsM);
    }

    /**
     * Connect to bank
     * @param bankHost bank host name
     * @param bankPort bank port number
     */
    private void connectToBank(String bankHost, int bankPort) {
        bankConnection = new BankConnection(bankHost, bankPort, messages);
    }

    /**
     * Processes a message
     */
    private void processMessage() {
        Message receivedMessage;
        try {
            receivedMessage = messages.take();
            checkMessage(receivedMessage);
            System.out.println(receivedMessage);
        }
        catch(Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Sets the bank account number
     * sends a message to gui to update account number
     * @param accountNum bank account number
     */
    private void setBankAccount(int accountNum) {
        agentID = accountNum;
        sendBankAccount();
    }

    /**
     * Sets available funds
     * Sends message to gui to update available funds
     * @param availFunds
     */
    private void setAvailFunds(int availFunds) {
        this.availFunds = availFunds;
        sendAvailableFunds();
    }


    /**
     * Sets house list
     * Sends house list to gui
     * @param m message for auction house list
     */
    private void setHouseList(Message m) {
        if(((MAuctionHouses) m).getHouses().isEmpty()) {
            return;
        }
        ArrayList<MHouseServerInfo> houses = ((MAuctionHouses) m).getHouses();
        for(MHouseServerInfo h: houses) {
            if(!auctionHouses.containsKey(h.getHouseID())) {
                setAuctionHouse(h.getHouseID(),h.getHouseHostName(),h.getHousePort());
            }
        }
        sendHouseList();
    }

    /**
     * Notifies bank and auction house that agent is shutting down
     */
    private void sendShutDown() {
        MShutDown m = new MShutDown(agentID, name);
        AuctionHouseConnection connection;
        bankConnection.sendMessage(m);
        for(int h: auctionHouses.keySet()) {
            connection = auctionHouses.get(h);
            connection.sendMessage(m);
        }
    }

    /**
     * Shut down agent
     * Sends shutdown message to all servers
     */
    private void shutDown() {
        sendShutDown();
    }

    /**
     * Closes auction house connection
     * @param houseId house id
     */
    private void closeConnection(int houseId) {
        AuctionHouseConnection connection = auctionHouses.get(houseId);
        connection.closeConnection();
    }

    /**
     * Process shut down message
     * @param m message for shutdown
     */
    private void processShutDown(Message m) {
        int id = ((MShutDown) m).getID();
        if(id == agentID) {
            if(ongoingBids == 0) {
                shutDown();
            }
        }
        else {
            closeConnection(id);
        }
    }

    /**
     * Checks messages in message queue
     * @param m message taken from queue
     */
    private void checkMessage(Message m){
        if(m instanceof MAccountCreated) {
            setBankAccount(((MAccountCreated) m).getAccountID());
            requestHouses();
        }
        else if(m instanceof MFundsTransferred) {
            MRequestBalance balanceM = new MRequestBalance(agentID, name);
            bankConnection.sendMessage(balanceM);
            MRequestAvailFunds fundsM = new MRequestAvailFunds(agentID, name);
            bankConnection.sendMessage(fundsM);
        }
        else if(m instanceof MAuctionHouses) {
            setHouseList(m);
        }
        else if(m instanceof MAvailableFunds) {
            setAvailFunds(((MAvailableFunds) m).getAvailableFunds());
        }
        else if(m instanceof MItemList) {
            sendItems(m);
        }
        else if(m instanceof MShutDown) {
            processShutDown(m);
        }
        else if(m instanceof MBidRejected) {
            sendStatusMessage(m);
            ongoingBids--;
        }
        else if(m instanceof MBidOutbid) {
            sendStatusMessage(m);
            ongoingBids--;
            MRequestAvailFunds fundsM = new MRequestAvailFunds(agentID, name);
            bankConnection.sendMessage(fundsM);
        }
        else if(m instanceof MBidWon) {
            int amount = ((MBidWon)m).getItemInfo().getCurrentBid();
            int houseAccountNum = ((MBidWon) m).getHouseID();
            sendStatusMessage(m);
            transferFunds(amount, houseAccountNum);
            ongoingBids--;

        }
        else if(m instanceof MBidAccepted) {
            sendStatusMessage(m);
            MRequestAvailFunds fundsM = new MRequestAvailFunds(agentID, name);
            bankConnection.sendMessage(fundsM);
        }
        else if(m instanceof MSelectHouse) {
            requestItems(((MSelectHouse) m).getHouseId());
        }
        else if(m instanceof MBid) {
            MBid bidM = new MBid(agentID,((MBid) m).getItemID(), ((MBid) m).getBidAmount());
            AuctionHouseConnection connection = auctionHouses.get(((MBid) m).getAgentID());
            connection.sendMessage(bidM);
            ongoingBids++;
        }
        else if(m instanceof MBalance) {
            this.balance = ((MBalance) m).getBalance();
            sendBalance();
        }
    }


    /**
     * Send message to bank to transfer funds
     * @param amount transfer amount
     * @param houseAccountNum house bank account number
     */
    private void transferFunds(int amount, int houseAccountNum){
        MTransferFunds m = new MTransferFunds(agentID, houseAccountNum, amount);
        bankConnection.sendMessage(m);
    }

    /**
     * Sends message to gui about bid status message
     */
    private void sendStatusMessage(Message m) {
        GUIMessageStatus statusM = new GUIMessageStatus(m);
        gui.sendMessage(statusM);
    }


    /**
     * Send items to gui
     * @param m message with items
     */
    private void sendItems(Message m) {
        ArrayList<Item> items = new ArrayList<>();
        ArrayList<BidTracker> trackers = ((MItemList)m).getBidTrackers();
        for(BidTracker t: trackers) {
            items.add(t.getItem());
        }
        GUIMessageItems itemsM = new GUIMessageItems(trackers);
        gui.sendMessage(itemsM);
    }

    /**
     * Requests a list of items from auction house
     * @param houseId id of auction house
     */
    private void requestItems(int houseId) {
        AuctionHouseConnection connection = auctionHouses.get(houseId);
        MRequestItems m = new MRequestItems(agentID);
        connection.sendMessage(m);
    }

    /**
     * Requests a list of houses from bank
     */
    private void requestHouses() {
        MRequestHouses m = new MRequestHouses(agentID, name);
        bankConnection.sendMessage(m);
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


}
