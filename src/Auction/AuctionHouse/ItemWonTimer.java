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
    private BidTracker itemInfo;
    private boolean running;

    public ItemWonTimer(LinkedBlockingQueue<Message> messageQueue, BidTracker itemInfo){
        this.messageQueue = messageQueue;
        timer = new Timer("Item Won Timer");
        this.itemInfo = itemInfo;
        running = false;
    }

    public void start(){
        running = true;
        timer = new Timer("Item Won Timer");

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    messageQueue.put(new MHouseWonTimer(itemInfo.getBidOwnerID(),itemInfo));
                    timer.cancel();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, DELAY);
    }


    public void restart(){
        timer.cancel();
        //timer.purge();
        this.start();
        //timer = null;
    }

    public void shutdown() {
        running = false;
        timer.cancel();
        //timer = null;
    }

    public boolean isRunning() {
        return running;
    }

    public static void main(String args[]){
        ItemWonTimer test = new ItemWonTimer(new LinkedBlockingQueue<Message>(), new BidTracker(new Item("test", 1),9,2));

        test.start();
        test.restart();

    }
}

