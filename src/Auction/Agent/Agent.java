package Auction.Agent;

import Auction.Item;

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
        // create message with name and initial balance
    }

    private void chooseAuctionHouse() {
        // create message with host and port info
    }

    private void connectToAuctionHouse(String auctionHost, int auctionPort){

    }

    private void refreshBalance() {
        // create message to get balance from bank
    }

    private void bidOnItem(int amount, Item bidItem) {

    }

    private void transferFunds(int amount, int houseAccountNum){}

    public static void main() {}
}
