package Auction.AuctionHouse;

import Auction.Messages.MHouseClosedTimer;
import Auction.Messages.MHouseWonTimer;
import Auction.Messages.Message;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

public class ItemWonTimer {
    private final int DELAY = 30000;
    private Timer timer;
    private TimerTask itemWonTask;
    private LinkedBlockingQueue<Message> messageQueue;

    public ItemWonTimer(LinkedBlockingQueue<Message> messageQueue, BidTracker itemInfo){
        this.messageQueue = messageQueue;
        timer = new Timer("Item Won Timer");

        itemWonTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    messageQueue.put(new MHouseWonTimer(itemInfo.getBidOwnerID(),itemInfo));
                    timer.cancel();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void start(){
        timer.schedule(itemWonTask, DELAY);
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
