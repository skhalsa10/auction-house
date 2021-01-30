/***********************************
 * Alexandra Valdez
 * CS 351-002
 * April 25, 2019
 ***********************************/
package Auction.Bank;

import Auction.Messages.*;

import java.io.EOFException;
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
        try {
            Object o = in.readObject();
            while(o != null) {
                if (o instanceof Message) {
                    //Add message to bank's blockQ
                    Message m = (Message) o;


                    //TODO I think we can take out all of these. we only need to map the name->out once.
                    //TODO with a boolean flag?
                    if (m instanceof MCreateAccount) {
                        bankConnections.put(((MCreateAccount) m).getAgentName(), out);
                    }
                    else if (m instanceof MTransferFunds) {
                        bankConnections.put(((MTransferFunds) m).getAgentName(), out);
                    }
                    else if (m instanceof MRequestAvailFunds) {
                        bankConnections.put(((MRequestAvailFunds)m).getAgentName(), out);
                    }
                    else if (m instanceof MRequestBalance) {
                        bankConnections.put(((MRequestBalance)m).getAgentName(), out);
                    }
                    else if (m instanceof MBlockFunds) {
                        bankConnections.put(((MBlockFunds)m).getHouseName(), out);
                    }
                    else if (m instanceof MShutDown) {
                        //TODO bankConnections.put(((MShutDown)m).getName(), out); <---- check the method for getting name
                    }
                    else if (m instanceof MRequestHouses) {
                        bankConnections.put(((MRequestHouses)m).getAgentName(), out);
                    }
                    //place the put last to make sure the output stream is mapped first.
                    bankQ.put(m);
                }

                    o = in.readObject();


            }
        }
        catch (EOFException e) {
            //TODO we need to delete this client from  the map?a
            System.out.println("A client disconnected.");

        }
        catch (IOException ex) {
            System.out.println("a client disconencted and the socket closed... close handled accordingly!");
        }
        catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
