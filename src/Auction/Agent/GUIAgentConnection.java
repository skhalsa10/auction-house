package Auction.Agent;

import Auction.Messages.Message;

import java.util.concurrent.LinkedBlockingQueue;

public class GUIAgentConnection implements Runnable{
    private LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<>();
    private boolean running = true;

    public GUIAgentConnection(LinkedBlockingQueue<Message> messages) {
        this.messages = messages;
    }

    public void sendMessage(Message m) {
        try {
            messages.put(m);
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }
    @Override
    public void run() {
        while(running) {

        }
    }
}
