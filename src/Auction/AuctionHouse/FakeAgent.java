package Auction.AuctionHouse;

import java.io.IOException;
import java.net.Socket;

public class FakeAgent extends Thread{
    private Socket houseSocket;

    public FakeAgent(String houseHost, int housePort){
        try {
            houseSocket = new Socket(houseHost,housePort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
