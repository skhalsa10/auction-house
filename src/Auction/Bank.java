/***********************************
 * Alexandra Valdez
 * CS 351-002
 * April 15, 2019
 ***********************************/
package Auction;

import Auction.Messages.Message;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class Bank implements Runnable {
    private List<Account> clientAccounts;
    private LinkedBlockingQueue<Message> blockQ;

    public void run() {
        while(!Thread.interrupted()) {
            try {
                Message msg;
                msg = blockQ.take();

                if(msg.getRequestType() == Message.RequestType.CREATE_ACCOUNT) {
                    //Create an account!
                    Account newAccount = new Account();
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
}
