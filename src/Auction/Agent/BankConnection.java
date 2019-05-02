package Auction.Agent;

import Auction.Messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLOutput;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Connection between Agent and Bank
 */
public class BankConnection implements Runnable {
    private Socket socket;
    private boolean connected = false;
    private LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<>();
    private ObjectInputStream in;
    private ObjectOutputStream out;

    /**
     * Constructs BankConnection
     * @param hostName bank host name
     * @param portNumber bank port number
     * @param messages agent messages
     */
    public BankConnection(String hostName, int portNumber, LinkedBlockingQueue messages){
        this.messages = messages;
        try {
            this.socket = new Socket(hostName, portNumber);
            connected = true;
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            new Thread(this).start();
        }
        catch (Exception e) {
            //System.err.println(e);
            System.out.println("bank server not running! cannot connect!");
        }
    }

    public boolean isConnected() {
        System.out.println(connected);
        return connected;
    }

    /**
     * Sends messages to bank
     * @param m message to be sent
     */
    public void sendMessage(Message m) {
        try {
            out.writeObject(m);
        }
        catch(Exception e) {
            System.err.println(e);
        }

    }

    /**
     * Close this connection
     */
    public void closeConnection() {
        connected = false;
        try {
            closeAll();
        }
        catch (IOException e) {
            System.out.println("Connection to Bank closed");
        }
    }

    /**
     * Close input, output and socket
     * @throws IOException
     */
    public void closeAll() throws IOException {
        connected = false;
        try {
            out.close();
        }
        finally {
            try {
                in.close();
            }
            finally {
                socket.close();
            }
        }

    }


    /**
     * Runs connection
     */
    @Override
    public void run () {
        Message receivedMessage;
        System.out.println("connected to bank");
        while(connected) {
            try {
                receivedMessage = (Message) in.readObject();
                if(receivedMessage != null) {
                    messages.put(receivedMessage);
                }
            }
            catch (IOException e) {
                System.out.println("Closed bank connection");
                connected = false;
            }
            catch (Exception e) {
                System.err.println(e);
            }
        }
    }
}
