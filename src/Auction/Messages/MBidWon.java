package Auction.Messages;

import Auction.AuctionHouse.BidTracker;

/**
 * this message is sent from the house to the agent that they won an Item.
 */
public class MBidWon extends Message {
    private final BidTracker itemInfo;
    private final int houseID;

    public  MBidWon(int houseID, BidTracker itemInfo){
        this.houseID = houseID;
        this.itemInfo = itemInfo.clone();
    }

    public BidTracker getItemInfo() {
        return itemInfo;
    }

    public int getHouseID() {
        return houseID;
    }
}
