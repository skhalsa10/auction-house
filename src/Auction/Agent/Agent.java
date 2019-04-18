package Auction.Agent;

import Auction.AuctionHouse.Item;
import Auction.Messages.Message;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Agent {
    private String name;
    private int balance;
    private ConcurrentLinkedQueue messages;
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
        AuctionHouseConnection c = new AuctionHouseConnection(auctionHost,auctionPort);
        Thread a1 = new Thread(c);
        a1.start();
    }

    private void connectToBank(String bankHost, int bankPort) {
        bankConnection = new BankConnection(bankHost, bankPort);
        openBankAccount();
        //Thread bank = new Thread(bankConnection);
        //bank.start();
    }

    private void updateBalance() {
        // create message to get balance from bank
    }

    private void bidOnItem(int amount, Item bidItem) {

    }

    private void transferFunds(int amount, int houseAccountNum){

    }

    public static void main(String[] args) {
        if(args.length == 4) {
            String bankHost = args[0];
            int bankPortNum = Integer.parseInt(args[1]);
            String name = args[2];
            //TODO check that balance is not negative
            int initialBalance = Integer.parseInt(args[3]);
            Agent a = new Agent(name, initialBalance);
            a.connectToBank(bankHost, bankPortNum);
            //a.openBankAccount();

            //a.connectToAuctionHouse("localhost",4444);

        }

    }
}
