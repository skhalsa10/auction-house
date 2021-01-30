package Auction.AuctionHouse;


import Auction.Messages.MHouseWonTimer;
import Auction.Messages.Message;
import java.util.concurrent.*;

/**
 * the purpose of this class is to trigger the winning of an item.
 * It literally times 30 seconds and sends a message to the house to process to win the item
 */
public class ItemWonTimer {

    private final int DELAY = 30;
    private ScheduledThreadPoolExecutor ex;
    private ScheduledFuture<?> task;
    private LinkedBlockingQueue<Message> messageQueue;
    private BidTracker itemInfo;
    private boolean running;

    /**
     *
     * @param messageQueue this is the blocking queue to deposit the message to.
     * @param itemInfo needed for the message
     */
    public  ItemWonTimer(LinkedBlockingQueue<Message> messageQueue, BidTracker itemInfo){
        this.messageQueue = messageQueue;
        this.itemInfo = itemInfo;
        this.running = false;
        this.ex = new ScheduledThreadPoolExecutor(1);
        this.ex.setRemoveOnCancelPolicy(true);
        this.task = null;
    }

    /**
     * this starts the timer. it schedules a future task by using the
     * ScheduledThreadPoolExecutor ex
     */
    public void start(){
        running = true;

        task = ex.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    messageQueue.put(new MHouseWonTimer(itemInfo.getBidOwnerID(),itemInfo));
                    running = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, DELAY, TimeUnit.SECONDS);

    }

    /**
     * this will cancel the old task and create a new future task
     * @return false if it can't cancel an old task or true otherwise
     */
    public boolean restart(){
        boolean canceled = task.cancel(false);
        //timer.purge();

        this.start();
        return canceled;
    }

    /**
     * forces a shut down whether there is a task running or not.
     * There should not be a task running when this gets used though so if there is that is an error
     */
    public void shutdown() {
        running = false;

        ex.shutdownNow();
        //timer = null;
    }

    /**
     *
     * @return if the timer is currently counting down
     */
    public boolean isRunning() {
        return running;
    }

}

