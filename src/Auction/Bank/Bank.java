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
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

public class Bank extends Thread{
    public static int accountCounter = 0;
    private static final int bankID = -1;
    private HashMap<Integer, Account> clientAccounts;
    private LinkedBlockingQueue<Message> blockQ;
    private BankServer bankServer;
    private HashMap<String, ObjectOutputStream> clientConnections;
    private ArrayList<MHouseServerInfo> auctionHouses;

    public Bank(int portNum) {
        clientAccounts = new HashMap<>();
        blockQ = new LinkedBlockingQueue<>();
        clientConnections = new HashMap<>();
        auctionHouses = new ArrayList<>();

        try {
            bankServer = new BankServer(portNum, blockQ, clientConnections);
            bankServer.start();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true) {
            //System.out.println("Bank itself running.");

            try {
                Message msg = null;
                msg = blockQ.take();

                if (msg instanceof MCreateAccount) {
                    // Create new account and add to our list of accounts
                    MCreateAccount m = ((MCreateAccount) msg);
                    Account newAccount;
                    newAccount = new Account(m.getAgentName(), m.getStartingBalance());
                    clientAccounts.put(newAccount.getAccountID(), newAccount);

                    // Create MAccountCreated message with new account's ID attached
                    // Then send the message to the requesting agent or house
                    MAccountCreated outgoingMsg = new MAccountCreated(newAccount.getAccountID());
                    try {
                    System.out.println(clientConnections.get(m.getAgentName()));
                        clientConnections.get(m.getAgentName()).writeObject(outgoingMsg);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                else if (msg instanceof MTransferFunds) {
                    // Get the "to" and "from" accounts and the amount to transfer
                    MTransferFunds m = ((MTransferFunds) msg);
                    int transferAmount = m.getAmount();
                    Account fromAccount = clientAccounts.get(m.getFromAccount());
                    Account toAccount = clientAccounts.get(m.getToAccount());

                    //Double check that the funds are available in total balance
                    if (transferAmount <= fromAccount.getTotalBalance()) {
                        // Do the transfer
                        fromAccount.deductFunds(transferAmount);
                        toAccount.addFunds(transferAmount);
                    }
                    else {
                        System.out.println("INSUFFICIENT FUNDS. TRANSFER FAILED!");

                        return;
                    }

                    // Tell the requesting agent or house the transfer is complete
                    MFundsTransferred outgoingMsg = new MFundsTransferred
                            (m.getFromAccount(), m.getToAccount(), fromAccount.getTotalBalance());
                    try {
                        clientConnections.get(m.getAgentName()).writeObject(outgoingMsg);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (msg instanceof MRequestAvailFunds) {
                    MRequestAvailFunds m = (MRequestAvailFunds) msg;
                    Account currentAccount = clientAccounts.get(m.getAgentId());
                    MAvailableFunds outgoingMsg = new MAvailableFunds(currentAccount.getAvailableBalance());

                    try {
                        clientConnections.get(m.getAgentName()).writeObject(outgoingMsg);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (msg instanceof MRequestBalance) {
                    MRequestBalance m = (MRequestBalance) msg;
                    Account currentAccount = clientAccounts.get(m.getAgentId());
                    MBalance outgoingMsg = new MBalance(currentAccount.getTotalBalance());

                    try {
                        clientConnections.get(m.getAgentName()).writeObject(outgoingMsg);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (msg instanceof MBlockFunds) {
                    MBlockFunds m = ((MBlockFunds) msg);
                    Account currentAccount = clientAccounts.get(m.getAgentID());
                    int blockAmt = m.getAmount();

                    //See if funds are avail - MBlockAccepted or MBlockRejected
                    if (currentAccount.getAvailableBalance() < blockAmt) {
                        System.out.println("INSUFFICIENT FUNDS. BLOCK FUNDS FAILED!");
                        MBlockRejected outgoingMsg = new MBlockRejected(m.getAgentID(), m.getAmount(), m.getItemID());
                        try {
                            clientConnections.get(m.getHouseName()).writeObject(outgoingMsg);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        System.out.println("BLOCKING FUNDS FOR A BID!");
                        MBlockAccepted outgoingMsg = new MBlockAccepted(m.getAgentID(), m.getAmount(), m.getItemID());
                        try {
                            // Deduct block amount from available balance, leave in total balance
                            currentAccount.blockFunds(blockAmt);
                            clientConnections.get(m.getHouseName()).writeObject(outgoingMsg);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else if (msg instanceof MUnblockFunds) {
                    MUnblockFunds m = ((MUnblockFunds) msg);
                    Account currentAccount = clientAccounts.get(m.getAgentID());
                    int unblockAmt = m.getAmount();
                    System.out.println("unblock amount: " + unblockAmt);

                    //Add funds back into available balance -- no need to send message
                    currentAccount.unblockFunds(unblockAmt);
                    System.out.println("Unblocking funds for agent "+ m.getAgentID());

                }
                else if (msg instanceof MHouseServerInfo) {
                    //Add to list of house server info
                    MHouseServerInfo m = ((MHouseServerInfo) msg);

                    auctionHouses.add(m);

                    //Send MAuctionHouses message to all clients so they know a new house exists
                    for (ObjectOutputStream value : clientConnections.values()) {
                        MAuctionHouses outgoingMsg = new MAuctionHouses(auctionHouses);
                        try {
                            value.writeObject(outgoingMsg);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else if (msg instanceof MShutDown) {
                    MShutDown m = ((MShutDown) msg);
                    try {
                        clientConnections.get(m.getName()).close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    //remove the mapping
                    clientConnections.remove(m.getName());
                    //remove the house server associated with the house shutting down if it is a house
                    Iterator<MHouseServerInfo> i = auctionHouses.iterator();
                    while (i.hasNext()){
                        MHouseServerInfo m2 = i.next();
                        if(m2.getHouseID() == m.getID()){
                            i.remove();
                            //since we remove we blast out the change to the clients
                            //Send MAuctionHouses message to all clients so they know a new house exists
                            for (ObjectOutputStream value : clientConnections.values()) {
                                MAuctionHouses outgoingMsg = new MAuctionHouses(auctionHouses);
                                try {
                                    value.writeObject(outgoingMsg);
                                }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                else if (msg instanceof MRequestHouses) {
                    //Send info to all clients
                    MRequestHouses m = (MRequestHouses) msg;
                    MAuctionHouses outgoingMsg = new MAuctionHouses(auctionHouses);
                    try {
                        clientConnections.get(m.getAgentName()).writeObject(outgoingMsg);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
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
        int portNum = Integer.parseInt(args[0]);
        Bank daBank = new Bank(portNum);
        daBank.start();
    }
}
