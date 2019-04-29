package Auction.Fake;

import Auction.AuctionHouse.BidTracker;
import Auction.AuctionHouse.Item;
import Auction.Messages.*;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * this fake server builds a  server socket and accepts 1 connection ONLY.
 *
 * It will print its host name when it starts to use when connecting to it.
 *
 * after it conencts to a 1 client only it will loop while the socket exists
 * and print out the Message.RequestType
 *
 * it will then wait for input. it accepts an input of one fo the following request types:
 *
 *  *      MAccountCreated, MAuctionHouses, MAvailableFunds, MBid, MBidAccepted, MBidOutbid,
 *  *      MBidRejected, MBidWon, MBlockAccepted, MBlockFunds, MBlockRejected, MCreateAccount
 *  *      MFundsTransferred, MHouseServerInfo, MItemList, MRequestItems, MShutDown, MTransferFunds,
 *  *      MUnblockFunds
 *
 * It will then convert this to a message and send it out to the connected client.
 */
public class FakeServer {


    public static void main(String args[]) {

        if (args.length != 1) {
            System.out.println("please enter a port to be used on the server as the parameter");
            return;
        }

        try {
            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
            System.out.println("host name of server: " + serverSocket.getInetAddress().getHostName());
            Socket socket = serverSocket.accept();
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            Object o = in.readObject();
            while(o != null){
                if(!(o instanceof Message)){
                    System.out.println("wrong object type not Message");
                    throw new IOException();
                }
                Message m = (Message) o;
                System.out.println(m);
                String userLine = stdIn.readLine();
                switch(userLine){
                    case "MAccountCreated":{
                        out.writeObject(new MAccountCreated(88));
                        break;
                    }
                    case "MAuctionHouses":{
                        out.writeObject(new MAuctionHouses(new ArrayList<>()));
                        break;
                    }
                    case "MAvailableFunds":{
                        out.writeObject(new MAvailableFunds(30000));
                        break;
                    }
                    case "MBid":{
                        out.writeObject(new MBid(73,2,149));
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
                        out.writeObject(new MBlockAccepted(73,2,1));
                        break;
                    }
                    case "MBlockFunds":{
                        out.writeObject(new MBlockFunds(7,73,1,40, "housename"));
                        break;
                    }
                    case "MBlockRejected":{
                        out.writeObject(new MBlockRejected(73,2,2));
                        break;
                    }
                    case "MCreateAccount":{
                        out.writeObject(new MCreateAccount("client", 8000));
                        break;
                    }
                    case "MFundsTransferred":{
                        out.writeObject(new MFundsTransferred(73,88,200));
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
                        out.writeObject(new MItemList(7,list));
                        break;
                    }
                    case "MRequestItems":{
                        out.writeObject(new MRequestItems(73));
                        break;
                    }
                    case "MShutDown":{
                        out.writeObject(new MShutDown(73));
                        break;
                    }
                    case "MTransferFunds":{
                        out.writeObject(new MTransferFunds(73,88,200));
                        break;
                    }
                    case "MUnblockFunds":{
                        out.writeObject(new MUnblockFunds(1,73,200));
                        break;
                    }
                    case "skip":{
                        break;
                    }
                    default:{
                        System.out.println("error reading input");
                    }
                }

                o = in.readObject();
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
