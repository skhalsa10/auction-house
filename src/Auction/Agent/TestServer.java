package Auction.Agent;

import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {
    public static void main(String[] args) throws Exception {
        int portNumber = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(portNumber);

        while(true) {
            Socket clientSocket = serverSocket.accept();

        }
    }
}