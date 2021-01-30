package Auction.AuctionHouse;

import Auction.Messages.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.BindException;
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
        }catch (IOException e) {
            if(!listening) {
                System.out.println(" ");
            }else {
                e.printStackTrace();
            }
        }
        System.out.println("Leaving House Server socket Listener thread");

    }

    /**
     * shut down clients threads and sockets and close the server listenerthread and socket.
     */
    public void shutDown(){

        listening = false;

        //first shutdown all client threads and sockets
        for(AuctionHouseThread t: clientThreads){
            t.shutDown();
        }

        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * shut down all client input streams
     */
    public void shutdownClientIns(){
        for(AuctionHouseThread t : clientThreads){
            t.shutDownIn();
        }
    }

    /**
     * this method will shutdown the input and output of a a client socket stream. it will then close the socket it and remove it
     * from the client threads and the map of output streams
     * @param id
     */
    public void shutDownClient(int id) {
        AuctionHouseThread temp = null;
        for(AuctionHouseThread t : clientThreads){
            if(t.getClientID() == id){
                t.shutDownIn();
                try {
                    clientOuts.get(id).close();
                    clientOuts.remove(id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                t.shutDown();
                temp = t;
            }
        }

        clientThreads.remove(temp);

    }
}
