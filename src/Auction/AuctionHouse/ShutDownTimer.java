package Auction.AuctionHouse;

import Auction.Messages.MHouseClosedTimer;
import Auction.Messages.Message;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

public class ShutDownTimer {
    private Timer timer;
    private TimerTask shutDownTask;
    private LinkedBlockingQueue<Message> messageQueue;

    public ShutDownTimer(LinkedBlockingQueue<Message> messageQueue){
        this.messageQueue = messageQueue;
        timer = new Timer("Shut Down Timer");
        shutDownTask = new TimerTask() {
            @Override
            public void run() {
                messageQueue.put(new MHouseClosedTimer());
            }
        }
    }

}
