package Auction.Fake;

import Auction.Messages.Message;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

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
 *         CREATE_ACCOUNT, CHECK_BALANCE, TRANSFER_FUNDS, ACCEPT_BID, REJECT_BID,
 *         SHUT_DOWN, FUNDS_AVAIL, FUNDS_NOT_AVAIL, FUNDS_TRANSFERRED, ITEM_WON;
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
                System.out.println(m.getRequestType());
                String userLine = stdIn.readLine();
                switch(userLine){
                    case "CREATE_ACCOUNT":{
                        out.writeObject(new Message(Message.RequestType.CREATE_ACCOUNT));
                        break;
                    }
                    case "ACCOUNT_CREATED":{
                        Message message = new Message(Message.RequestType.ACCOUNT_CREATED);
                        m.setID(7);
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
