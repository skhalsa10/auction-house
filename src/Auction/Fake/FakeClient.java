package Auction.Fake;

import Auction.Messages.Message;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

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
 *         CREATE_ACCOUNT, CHECK_BALANCE, TRANSFER_FUNDS, ACCEPT_BID, REJECT_BID,
 *         SHUT_DOWN, FUNDS_AVAIL, FUNDS_NOT_AVAIL, FUNDS_TRANSFERRED, ITEM_WON;
 *
 * It will convert the string into a message and send it out it will just loop and do this
 *
 */
public class FakeClient {


    public static void main(String args[]) {

        int fakeID = 80;

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
            out.writeObject(new Message(Message.RequestType.CREATE_ACCOUNT));
            Object o = in.readObject();
            while(o != null){
                if(!(o instanceof Message)){
                    System.out.println("wrong object type not Message");
                    throw new IOException();
                }
                Message m = (Message) o;
                System.out.println(m.getRequestType());
                String userLine = stdIn.readLine();
                switch(userLine){
                    case "CREATE_ACCOUNT":{
                        Message message = new Message(Message.RequestType.CREATE_ACCOUNT);
                        message.setID(fakeID);
                        out.writeObject(message);
                        break;
                    }
                    case "CHECK_BALANCE": {
                        out.writeObject(new Message(Message.RequestType.CHECK_BALANCE));
                        break;
                    }
                    case "TRANSFER_FUNDS":{
                        out.writeObject(new Message(Message.RequestType.TRANSFER_FUNDS));
                        break;
                    }
                    case "ACCEPT_BID":{
                        out.writeObject(new Message(Message.RequestType.ACCEPT_BID));
                        break;
                    }
                    case "REJECT_BID":{
                        out.writeObject(new Message(Message.RequestType.REJECT_BID));
                        break;
                    }
                    case "SHUT_DOWN":{
                        out.writeObject(new Message(Message.RequestType.SHUT_DOWN));
                        break;
                    }
                    case "FUNDS_AVAIL":{
                        out.writeObject(new Message(Message.RequestType.FUNDS_AVAIL));
                        break;
                    }
                    case "FUNDS_NOT_AVAIL":{
                        out.writeObject(new Message(Message.RequestType.FUNDS_NOT_AVAIL));
                        break;
                    }
                    case "FUNDS_TRANSFERRED":{
                        out.writeObject(new Message(Message.RequestType.FUNDS_TRANSFERRED));
                        break;
                    }
                    case "ITEM_WON":{
                        out.writeObject(new Message(Message.RequestType.ITEM_WON));
                        break;
                    }
                    case "BID_ITEM":{
                        break;
                    }
                    case "REQUEST_ITEMS":{
                        Message message = new Message(Message.RequestType.REQUEST_ITEMS);
                        message.setID(fakeID);
                        out.writeObject(message);
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
