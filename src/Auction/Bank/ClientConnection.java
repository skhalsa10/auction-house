/***********************************
 * Alexandra Valdez
 * CS 351-002
 * April 25, 2019
 ***********************************/
package Auction.Bank;

import Auction.Messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientConnection implements Runnable {
    private final Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private LinkedBlockingQueue<Message> bankQ;
    private HashMap<String, ObjectOutputStream> bankConnections;

    public ClientConnection(Socket clientSocket, LinkedBlockingQueue<Message> bankQ, HashMap<String, ObjectOutputStream> bankConnections) throws IOException {
        this.clientSocket = clientSocket;
        this.bankQ = bankQ;
        this.bankConnections = bankConnections;
        out = new ObjectOutputStream(clientSocket.getOutputStream()); // Bank to client
        in = new ObjectInputStream(clientSocket.getInputStream()); // From client to bank
    }

    public void run() {
        //Read incoming stuff?
        //Put in bank message queue?
        try {
            Object o = in.readObject();
            while(o != null) {
                if (o instanceof Message) {
                    //Add message to bank's blockQ
                    Message m = (Message) o;
                    bankQ.add(m);
                    bankConnections.put("name", out);
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }
}
