/***********************************
 * Alexandra Valdez
 * CS 351-002
 * April 25, 2019
 ***********************************/
package Auction.Bank;

import Auction.Messages.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class Bank extends Thread{
    public static int accountCounter = 0;

    private List<Account> clientAccounts;
    private LinkedBlockingQueue<Message> blockQ;
    private BankServer bankServer;
    private HashMap<String, ObjectOutputStream> clientConnections;

    public Bank() {
        clientAccounts = new ArrayList<>();
        blockQ = new LinkedBlockingQueue<>();
        try {
            bankServer = new BankServer(7878, blockQ, clientConnections);
            bankServer.run();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void run() {
        while(!Thread.interrupted()) {
            //System.out.println("Bank itself running.");
            try {
                Message msg = null;
                msg = blockQ.take();

                if (msg instanceof MCreateAccount) {
                    // Create new account and add to our list of accounts
                    MCreateAccount m = ((MCreateAccount) msg);
                    Account newAccount;
                    newAccount = new Account(m.getName(), m.getStartingBalance());
                    clientAccounts.add(newAccount);

                    // Create MAccountCreated message with new account's ID attached
                    // Then send the message to the requesting agent or house
                    MAccountCreated outgoingMsg = new MAccountCreated(newAccount.getAccountID());
                    try {
                        clientConnections.get(m.getName()).writeObject(outgoingMsg);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

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
            }
        }
    }

    public static int getAccountCounter() {
        return accountCounter;
    }

    public static void incrementAccountCounter() {
        accountCounter++;
    }

    public static void main(String args[]) throws IOException {
        Bank daBank = new Bank();
        daBank.start();
    }
}
