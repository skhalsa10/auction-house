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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BankServer extends Thread {
    private final ServerSocket serverSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private LinkedBlockingQueue<Message> bankQ;
    private HashMap<String, ObjectOutputStream> bankConnections;

    public BankServer(int portNum, LinkedBlockingQueue<Message> bankQ, HashMap<String, ObjectOutputStream> bankConnections) throws IOException {
        this.serverSocket = new ServerSocket(portNum);
        this.bankQ = bankQ;
        this.bankConnections = bankConnections;
    }

    @Override
    public void run() {
        System.out.println("Bank server running.");
        // If a client tries to connect, open a socket for it
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                ClientConnection clientConnection = new ClientConnection(clientSocket, bankQ, bankConnections);
                Thread thread = new Thread(clientConnection);
                thread.start();
                System.out.println("Client connected!");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
