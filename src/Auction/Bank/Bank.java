/***********************************
 * Alexandra Valdez
 * CS 351-002
 * April 24, 2019
 ***********************************/
package Auction.Bank;

import Auction.Messages.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
            System.out.println("Runnin'");
            /*
            try {
                Message msg = null;
                //msg = blockQ.take();

                if (msg instanceof MCreateAccount) {
                    MCreateAccount m = ((MCreateAccount) msg);
                    // Create new account and add to our list of accounts
                    Account newAccount;
                    newAccount = new Account(m.getName(), m.getStartingBalance());
                    clientAccounts.add(newAccount);

                    // Then send MAccountCreated message to the requesting agent or house
                    MAccountCreated outgoingMsg = new MAccountCreated(newAccount.getAccountID());

                }
                else if (msg instanceof MTransferFunds) {
                    MTransferFunds m = ((MTransferFunds) msg);
                    //Transfer funds, then send MFundsTransferred to the requesting agent or house
                }
                else if (msg instanceof MBlockFunds) {
                    MBlockFunds m = ((MBlockFunds) msg);
                    //Block funds on given account, then send the house MBlockAccepted or MBlockRejected message
                }
                else if (msg instanceof MUnblockFunds) {
                    MUnblockFunds m = ((MUnblockFunds) msg);
                }
                else if (msg instanceof MHouseServerInfo) {
                    MHouseServerInfo m = ((MHouseServerInfo) msg);
                    //Send MAuctionHouses message to the requesting agent
                }
                else if (msg instanceof MShutDown) {
                    MShutDown m = ((MShutDown) msg);
                    //Agent or house requesting to shut down and stop being tracked by the bank
                }
                else {
                    System.out.println("Ran into message type not intended for bank use.");
                }
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }*/
        }
    }

    public static int getAccountCounter() {
        return accountCounter;
    }

    public static void incrementAccountCounter() {
        accountCounter++;
    }

    public static void main(String args[]) throws IOException {
        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));

    }
}
