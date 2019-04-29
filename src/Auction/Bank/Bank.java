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

    private static final int bankID = -1;
    private HashMap<Integer, Account> clientAccounts;
    private LinkedBlockingQueue<Message> blockQ;
    private BankServer bankServer;
    private HashMap<String, ObjectOutputStream> clientConnections;

    public Bank() {
        clientAccounts = new HashMap<>();
        blockQ = new LinkedBlockingQueue<>();
        clientConnections = new HashMap<>();

        try {
            bankServer = new BankServer(7878, blockQ, clientConnections);
            bankServer.start();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true) {
            System.out.println("Bank itself running.");
            try {
                Message msg = null;
                msg = blockQ.take();
                System.out.println("Taking msgs from the Q");

                if (msg instanceof MCreateAccount) {
                    System.out.println("Rec'd msg to create an account");
                    // Create new account and add to our list of accounts
                    MCreateAccount m = ((MCreateAccount) msg);
                    Account newAccount;
                    newAccount = new Account(m.getName(), m.getStartingBalance());
                    clientAccounts.put(newAccount.getAccountID(), newAccount);

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
                    // Get the "to" and "from" accounts and the amount to transfer
                    MTransferFunds m = ((MTransferFunds) msg);
                    int transferAmount = m.getAmount();
                    Account fromAccount = clientAccounts.get(m.getAmount());
                    Account toAccount = clientAccounts.get(m.getToAccount());

                    // Do the transfer
                    fromAccount.deductFunds(transferAmount);
                    toAccount.addFunds(transferAmount);

                    // Tell the requesting agent or house the transfer is complete
                    MFundsTransferred outgoingMsg = new MFundsTransferred
                            (m.getFromAccount(), m.getToAccount(), fromAccount.getTotalBalance());
                    try {
                        clientConnections.get(m.getName()).writeObject(outgoingMsg);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (msg instanceof MBlockFunds) {
                    MBlockFunds m = ((MBlockFunds) msg);
                    //Block funds on given account, then send the house MBlockAccepted or MBlockRejected message
                }
                else if (msg instanceof MUnblockFunds) {
                    MUnblockFunds m = ((MUnblockFunds) msg);
                }
                else if (msg instanceof MHouseServerInfo) {
                    //Add to list of house server info
                    MHouseServerInfo m = ((MHouseServerInfo) msg);
                    //Send MAuctionHouses message to all clients so they know a new house exists
                }
                else if (msg instanceof MShutDown) {
                    MShutDown m = ((MShutDown) msg);
                    //Agent or house requesting to shut down and stop being tracked by the bank
                }
                else if (msg instanceof MRequestHouses) {

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

    public static int getAccountID() {
        return bankID;
    }

    public static void main(String args[]) throws IOException {
        Bank daBank = new Bank();
        daBank.start();
    }
}
