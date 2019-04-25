package Auction.Bank;

import Auction.Messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BankServer implements Runnable {
    private final ServerSocket serverSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public BankServer(int portNum) throws IOException {
        this.serverSocket = new ServerSocket(portNum);
    }

    public void run() {
        // If a client tries to connect, open a socket for it
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                Client client = new Client(clientSocket);
                Thread thread = new Thread(client);
                thread.start();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
