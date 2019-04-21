package Auction.Agent;

import Auction.AuctionHouse.Item;
import Auction.Messages.Message;

import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class Agent implements Runnable {
    private String name;
    private int balance;
    private int bankAccount;
    private LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<>();
    private String bankHost;
    private int bankPortNum;
    private BankConnection bankConnection;
    private HashMap<Integer, Boolean> connectedHouses = new HashMap<>();
    private HashMap<Integer, Socket> auctionHouses = new HashMap<>();



    public Agent(String bankHost, int bankPortNum, String name, int initialBalance) {
        this.bankHost = bankHost;
        this.bankPortNum = bankPortNum;
        this.name = name;
        this.balance = initialBalance;
        new Thread(this).start();
    }
    private void openBankAccount(){
        Message m = new Message(Message.RequestType.CREATE_ACCOUNT);
        m.setAgentName(name);
        m.setAgentBalance(balance);
        bankConnection.sendMessage(m);
    }

    private void setAuctionHouses(Message m) {
        //send auction house id's to gui
    }

    private void setBankAccount(Message m) {
        //send bank account # to gui
    }

    private void chooseAuctionHouse(int houseID) {
        boolean alreadyConnected = connectedHouses.get(houseID);
        Socket socket;
        if(!alreadyConnected) {
            socket = auctionHouses.get(houseID);
            connectToAuctionHouse(socket);
        }
    }

    private void connectToAuctionHouse(Socket socket){
        AuctionHouseConnection c = new AuctionHouseConnection(socket,messages);
        new Thread(c).start();
    }

    private void connectToBank(String bankHost, int bankPort) {
        bankConnection = new BankConnection(bankHost, bankPort, messages);
        new Thread(bankConnection).start();

    }

    private void processMessage() {
        Message receivedMessage;
        try {
            receivedMessage = messages.take();
            receivedMessage.printMessage();
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
            Agent a = new Agent(bankHost, bankPortNum, name, initialBalance);

        }

    }
}
