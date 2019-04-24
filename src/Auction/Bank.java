/***********************************
 * Alexandra Valdez
 * CS 351-002
 * April 15, 2019
 ***********************************/
package Auction;

import Auction.Messages.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class Bank implements Runnable {
    public static int accountCounter = 0;

    private Thread thread;
    private List<Account> clientAccounts;
    private LinkedBlockingQueue<Message> blockQ;

    public Bank() {
        clientAccounts = new ArrayList<>();
        blockQ = new LinkedBlockingQueue<>();
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        while(!Thread.interrupted()) {
            try {
                Message msg;
                msg = blockQ.take();

                if(msg.getRequestType() == Message.RequestType.CREATE_ACCOUNT) {
                    //Create an account!
                    Account newAccount;
                    if(msg.getStartingBalance() != 0.0) {
                        newAccount = new Account(msg.getStartingBalance());
                    }
                    else {
                        newAccount = new Account();
                    }
                    clientAccounts.add(newAccount);
                }
                else if(msg.getRequestType() == Message.RequestType.CHECK_BALANCE) {
                    //Check da balance!
                }
                else if(msg.getRequestType() == Message.RequestType.TRANSFER_FUNDS) {
                    //Transfer them funds!
                }
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public synchronized void receiveMessage(Message msg) {
        try {
            blockQ.put(msg);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public static int getAccountCounter() {
        return accountCounter;
    }

    public static void incrementAccountCounter() {
        accountCounter++;
    }
}
