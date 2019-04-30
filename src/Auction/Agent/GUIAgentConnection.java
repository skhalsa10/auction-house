package Auction.Agent;

import Auction.Messages.Message;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Connection between Agent and GUI
 */
public class GUIAgentConnection implements Runnable{
    private LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<>();
    private boolean running = true;

    /**
     * Constructs gui and agent connection
     * @param messages
     */
    public GUIAgentConnection(LinkedBlockingQueue<Message> messages) {
        this.messages = messages;
    }

    /**
     * Sends message to agent from gui
     * @param m message
     */
    public void sendMessage(Message m) {
        try {
            messages.put(m);
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }

    public void closeConnection() {
        running = false;
    }

    /**
     * Runs connection
     */
    @Override
    public void run() {
        while(running) {

        }
    }
}
