package Auction.Agent;

import Auction.AuctionHouse.Item;
import Auction.Messages.Message;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Agent {
    private String name;
    private int balance;
    private LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<>();
    private BankConnection bankConnection;


    public Agent(String name, int initialBalance) {
        this.name = name;
        this.balance = initialBalance;
    }
    private void openBankAccount(){
        Message m = new Message(Message.RequestType.CREATE_ACCOUNT);
        m.setAgentName(name);
        m.setAgentBalance(balance);
        bankConnection.sendMessage(m);
    }

    private void chooseAuctionHouse() {
        //
    }

    private void connectToAuctionHouse(String auctionHost, int auctionPort){
        AuctionHouseConnection c = new AuctionHouseConnection(auctionHost,auctionPort,messages);
        Thread a1 = new Thread(c);
        a1.start();
    }

    private void connectToBank(String bankHost, int bankPort) {
        bankConnection = new BankConnection(bankHost, bankPort, messages);
        //openBankAccount();
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

    public static void main(String[] args) {
        String bankHost;
        int bankPortNum;
        String name;
        int initialBalance;

        if(args.length == 4) {
            bankHost = args[0];
            bankPortNum = Integer.parseInt(args[1]);
            name = args[2];
            //TODO check that balance is not negative
            initialBalance = Integer.parseInt(args[3]);
            Agent a = new Agent(name, initialBalance);
            a.connectToBank(bankHost, bankPortNum);
            a.openBankAccount();
            while(true) {
                a.processMessage();
            }
            //a.connectToAuctionHouse("localhost",4444);

        }

    }
}
