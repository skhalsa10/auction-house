package Auction.Bank;

import Auction.Messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Runnable {
    private final Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Client(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        out = new ObjectOutputStream(clientSocket.getOutputStream()); // To the bank
        in = new ObjectInputStream(clientSocket.getInputStream()); // From the bank
    }

    public void run() {
        //Read incoming stuff?
        //Put in bank message queue?
        try {
            Object o = in.readObject();
            while(o != null) {
                if (o instanceof Message) {
                    out.writeObject((Message) o);
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
