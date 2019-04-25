package Auction.Agent;

import Auction.Messages.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
        }
        catch (Exception e) {
            System.err.println(e);
        }
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
     * Runs connection
     */
    @Override
    public void run () {
        Message receivedMessage;
        while(connected) {
            try {
                receivedMessage = (Message) in.readObject();
                if(receivedMessage != null) {
                    messages.put(receivedMessage);
                }
            }
            catch (Exception e) {
                System.err.println(e);
            }
        }
    }
}
