package Auction.Agent;

import Auction.Messages.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {
    public static void main(String[] args) throws Exception {
        Message m;
        ObjectInputStream in;
        ObjectOutputStream out;
        ServerSocket serverSocket = new ServerSocket(4444);
        System.out.println("Server started...");
        while(true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("client accepted");
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());

            try {
                String test = "Message sent from server";
                out.writeObject(test);
                m = (Message)in.readObject();
                System.out.println("Message Received");

            }
            catch (Exception e) {
                System.err.println(e);
            }

        }
    }
}