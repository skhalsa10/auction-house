package Auction.AuctionHouse;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * this class is a server socket listener for the Action House.
 * I made this because I want the action House to use its main thread to process messages for bidding
 *
 */
public class AuctionHouseListener extends Thread {
    private LinkedBlockingQueue messageQueue;
    private ServerSocket serverSocket;
    private boolean listening;

    public AuctionHouseListener(ServerSocket serverSocket, LinkedBlockingQueue messageQueue){
        this.messageQueue = messageQueue;
        this.serverSocket = serverSocket;
        listening = true;

    }

    @Override
    public void run() {
        while(listening){
            try {

                new AuctionHouseThread(serverSocket.accept(),messageQueue).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
