package Auction.Agent;

import Auction.Messages.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(4444);
        System.out.println("Server started...");
        while(true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("client accepted");
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
        }
    }
}