package Auction.Agent;

import Auction.Item;
import Auction.Messages.Message;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Agent {
    private String name;
    private int balance;
    private ConcurrentLinkedQueue messages;

    public Agent(String name, int initialBalance) {
        this.name = name;
        this.balance = initialBalance;
    }
    private void openBankAccount(){
        Message m = new Message(Message.RequestType.CREATE_AGENT_ACCOUNT);
        m.setAgentName(name);
        m.setAgentBalance(balance);
    }

    private void chooseAuctionHouse() {
        //
    }

    private void connectToAuctionHouse(String auctionHost, int auctionPort){
        AuctionHouseConnection c = new AuctionHouseConnection(auctionHost,auctionPort);

    }

    private void refreshBalance() {
        // create message to get balance from bank
    }

    private void bidOnItem(int amount, Item bidItem) {

    }

    private void transferFunds(int amount, int houseAccountNum){}

    public static void main() {}
}
