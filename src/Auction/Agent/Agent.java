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



    public Agent(String bankHost, int bankPortNum, String name, int initialBalance, GUI gui) {
        this.bankHost = bankHost;
        this.bankPortNum = bankPortNum;
        this.name = name;
        this.balance = initialBalance;
        this.gui = gui;
        new Thread(this).start();
    }
    private void openBankAccount(){
        MCreateAccount m = new MCreateAccount(name,balance);
        bankConnection.sendMessage(m);
    }

    public void sendHouseList() {
        ArrayList<Integer> houseIds = new ArrayList<>();
        houseIds.addAll(auctionHouses.keySet());
        GUIMessageLoaded loadedM = new GUIMessageLoaded(houseIds);
        gui.sendMessage(loadedM);
    }

    public void setAuctionHouse(int houseId, String hostname, int portNum) {
        try {
            Socket socket = new Socket(hostname, portNum);
            AuctionHouseConnection connection = new AuctionHouseConnection(socket, messages);
            auctionHouses.put(houseId, connection);
        }
        catch (Exception e) {
            System.err.println(e);
        }

    }

    public LinkedBlockingQueue<Message> getMessages(){
        return messages;
    }

    public void setBankAccount() {
        //send bank account # to gui

    }

    private void setBalance() {

    }

    private void setAvailableFunds() {}

    private void chooseAuctionHouse(int houseID) {
        boolean alreadyConnected = connectedHouses.get(houseID);
        Socket socket;
        AuctionHouseConnection connection;
        if(!alreadyConnected) {
            connection = auctionHouses.get(houseID);
            connectToAuctionHouse(connection);
        }

    }

    private void connectToAuctionHouse(AuctionHouseConnection connection){
        //AuctionHouseConnection c = new AuctionHouseConnection(socket,messages);
        new Thread(connection).start();
        MRequestItems m = new MRequestItems(agentID);
        connection.sendMessage(m);
    }

    private void connectToBank(String bankHost, int bankPort) {
        bankConnection = new BankConnection(bankHost, bankPort, messages);
        new Thread(bankConnection).start();

    }

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


    private void updateBalance() {
        // create message to get balance from bank
    }

    private void bidOnItem(int amount, Item bidItem) {

    }

    private void transferFunds(int amount, int houseAccountNum){

    }


    private void processStatusMessages() {

    }


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
