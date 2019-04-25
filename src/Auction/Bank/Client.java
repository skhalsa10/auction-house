package Auction.Bank;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client implements Runnable {
    private final Socket clientSocket;
    private OutputStream out;
    private InputStream in;

    public Client(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        out = clientSocket.getOutputStream();
        in = clientSocket.getInputStream();
    }

    public void run() {
        //Read incoming stuff?
        //Put in bank message queue?
    }
}
