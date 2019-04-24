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
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            try {
                m = (Message)in.readObject();
                if(m == null) {
                    System.out.println("no message");
                }
                else {
                    System.out.println(m.toString());
                    System.out.println("Message Received");
                    out.writeObject(m);
                }


            }
            catch (Exception e) {
                System.err.println(e);
            }

        }
    }
}