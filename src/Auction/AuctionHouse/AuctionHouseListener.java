package Auction.AuctionHouse;

import Auction.Messages.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * this class is a server socket listener for the Action House.
 * I made this because I want the action House to use its main thread to process messages for bidding
 * this class literally only listens on the server socket and accepts connections and passes it to an auctionHouseThread
 *
 * @author Siri Khalsa
 * @version 1
 *
 */
public class AuctionHouseListener extends Thread {
    //needed by the spawned sockets/threads
    private LinkedBlockingQueue<Message> houseMessageQueue;
    private HashMap<Integer, ObjectOutputStream> clientOuts;
    //server socket to listen on
    private ServerSocket serverSocket;
    private boolean listening;
    private ArrayList<AuctionHouseThread> clientThreads;

    /**
     *
     * @param serverSocket this is the server socket to listen on
     * @param messageQueue needed to pass to the spawned socket and thread
     * @param clientOuts needed to pass to the spawned socket and thread
     */
    public AuctionHouseListener(ServerSocket serverSocket, LinkedBlockingQueue<Message> messageQueue, HashMap<Integer, ObjectOutputStream> clientOuts){
        clientThreads = new ArrayList<>();
        this.houseMessageQueue = messageQueue;
        this.clientOuts = clientOuts;
        this.serverSocket = serverSocket;
        listening = true;

    }

    /**
     * literally just listens for new connections and accepts them...dude
     */
    @Override
    public void run() {

        try {
            while (listening) {
                Socket s = serverSocket.accept();
                AuctionHouseThread t = new AuctionHouseThread(s, houseMessageQueue, clientOuts);
                clientThreads.add(t);
                t.start();
            }
        } catch (IOException e) {
            if(!listening) {
                System.out.println("gracefully cought IOException");
            }else {
                e.printStackTrace();
            }
        }
        System.out.println("Leaving listener run");
        for(AuctionHouseThread t: clientThreads){
            t.shutDown();
        }

    }

    public void shutDown(){

        listening = false;

        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void shutDownClient(int id) {
        clientThreads.get(id).shutDown();
    }
}
