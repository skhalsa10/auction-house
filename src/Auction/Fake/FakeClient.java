package Auction.Fake;

import Auction.AuctionHouse.BidTracker;
import Auction.AuctionHouse.Item;
import Auction.Messages.*;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * the fake client works pretty much exactly the same as  the server.
 *
 * it takes two parameters though. the server host name first and the server port second
 *
 * It will print connected after it is connected so you know it is working.
 *
 * It automatically sends out a CREATE_ACCOUNT message to start.
 * It will then wait for an object to come in the stream print out the request type and read a line that accepts
 * the following.
 *
 *         MAccountCreated, MAuctionHouses, MAvailableFunds, MBid, MBidAccepted, MBidOutbid,
 *         MBidRejected, MBidWon, MBlockAccepted, MBlockFunds, MBlockRejected, MCreateAccount
 *         MFundsTransferred, MHouseServerInfo, MItemList, MRequestItems, MShutDown, MTransferFunds,
 *         MUnblockFunds
 *
 *
 * It will convert the string into a message and send it out it will just loop and do this
 *
 */
public class FakeClient {

    public static void main(String args[]) {

        if (args.length != 2) {
            System.out.println("please enter a serverhostname and serverport");
            return;
        }

        try (
                Socket houseSocket = new Socket(args[0], Integer.parseInt(args[1]));
                ObjectOutputStream out = new ObjectOutputStream(houseSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(houseSocket.getInputStream());
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));) {

            System.out.println("connected!!");
            Object o = null;
            while(true){
                String userLine = stdIn.readLine();
                switch(userLine){
                    case "MAccountCreated":{
                        out.writeObject(new MAccountCreated(88));
                        break;
                    }
                    case "MAuctionHouses":{
                        out.writeObject(new MAuctionHouses());
                        break;
                    }
                    case "MAvailableFunds":{
                        out.writeObject(new MAvailableFunds(300));
                        break;
                    }
                    case "MBid":{
                        out.writeObject(new MBid(73,9,149));
                        break;
                    }
                    case "MBidAccepted":{
                        out.writeObject(new MBidAccepted(7, new BidTracker(new Item("fakeItem",1),7,2)));
                        break;
                    }
                    case "MBidOutbid":{
                        out.writeObject(new MBidOutbid(7, new BidTracker(new Item("fakeItem",1),7,2)));
                        break;
                    }
                    case "MBidRejected":{
                        out.writeObject(new MBidRejected(7, new BidTracker(new Item("fakeItem",1),7,2)));
                        break;
                    }
                    case "MBidWon":{
                        out.writeObject(new MBidWon(7, new BidTracker(new Item("fakeItem",1),7,2)));
                        break;
                    }
                    case "MBlockAccepted":{
                        out.writeObject(new MBlockAccepted(1,2,200));
                        break;
                    }
                    case "MBlockFunds":{
                        out.writeObject(new MBlockFunds(7,3,4,40));
                        break;
                    }
                    case "MBlockRejected":{
                        out.writeObject(new MBlockRejected(1,2,200));
                        break;
                    }
                    case "MCreateAccount":{
                        out.writeObject(new MCreateAccount("client", 8000));
                        break;
                    }
                    case "MFundsTransferred":{
                        out.writeObject(new MFundsTransferred(1,2,200));
                        break;
                    }
                    case "MHouseServerInfo":{
                        out.writeObject(new MHouseServerInfo(9,"0.0.0.0",7777));
                        break;
                    }
                    case "MItemList":{
                        ArrayList<BidTracker> list = new ArrayList<>();
                        list.add(new BidTracker(new Item("fakeItem1",1),7,2));
                        list.add(new BidTracker(new Item("fakeItem2",2),7,2));
                        list.add(new BidTracker(new Item("fakeItem3",3),7,2));
                        out.writeObject(new MItemList(9,list));
                        break;
                    }
                    case "MRequestItems":{
                        out.writeObject(new MRequestItems(13));
                        break;
                    }
                    case "MShutDown":{
                        out.writeObject(new MShutDown(10));
                        break;
                    }
                    case "MTransferFunds":{
                        out.writeObject(new MTransferFunds(1,2,200));
                        break;
                    }
                    case "MUnblockFunds":{
                        out.writeObject(new MUnblockFunds(1,2,200));
                        break;
                    }
                    default:{
                        System.out.println("error reading input");
                    }
                }
                o = in.readObject();
                if(!(o instanceof Message)){
                    System.out.println("wrong object type not Message");
                    throw new IOException();
                }
                Message m = (Message) o;
                System.out.println("message received on client: " + m);
            }

        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
