package Auction.AuctionHouse;

import Auction.Messages.MHouseClosedTimer;
import Auction.Messages.Message;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;


public class ShutDownTimer {
    private final int DELAY = 60000;
    private Timer timer;
    private TimerTask shutDownTask;
    private LinkedBlockingQueue<Message> messageQueue;

    public ShutDownTimer(LinkedBlockingQueue<Message> messageQueue){
        this.messageQueue = messageQueue;
        timer = new Timer("Shut Down Timer");

        shutDownTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    messageQueue.put(new MHouseClosedTimer());
                    timer.cancel();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void start(){
        timer.schedule(shutDownTask, DELAY);
    }
    public void restart(){
        timer.cancel();
        timer.purge();
        this.start();
    }

    public void shutdown() {
        timer.cancel();
    }
}
