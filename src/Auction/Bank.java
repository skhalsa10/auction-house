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
                }
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
