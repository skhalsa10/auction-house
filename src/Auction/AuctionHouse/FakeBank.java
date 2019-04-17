package Auction.AuctionHouse;

import Auction.Messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FakeBank extends Thread{
    ServerSocket serverSocket;
    Socket s;

    public FakeBank(){
        try {
            serverSocket = new ServerSocket(7788);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        ObjectOutputStream out;
        ObjectInputStream in;
        try {
            s = serverSocket.accept();
            out = new ObjectOutputStream(s.getOutputStream());
            in = new ObjectInputStream(s.getInputStream());
            Object o;
            while((o = in.readObject()) != null){
                Message m = (Message)o;
                System.out.println(m.getRequestType());
                out.writeObject(m);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
