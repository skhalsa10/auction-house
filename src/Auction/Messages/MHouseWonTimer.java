package Auction.Messages;

import Auction.AuctionHouse.BidTracker;

/**
 * this Message is ONLY used by the the auction house class. it is used in the timer that counts down before an item is won.
 * when the timer counts down it will insert this message into the  message queue for the house.
 *
 * It is possible that this item can get inserted when someone bid just in time so will need to check that.
 */
public class MHouseWonTimer extends Message{
    private final int agentID;
    private final BidTracker itemWon;

    public MHouseWonTimer(int agentID, BidTracker itemWon){
        this.agentID = agentID;
        this.itemWon = itemWon;
    }

    public int getAgentID() {
        return agentID;
    }

    public BidTracker getItemWon() {
        return itemWon;
    }
}
