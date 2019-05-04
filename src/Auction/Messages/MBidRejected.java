package Auction.Messages;

import Auction.AuctionHouse.BidTracker;

/**
 * this message is sent from the house to the agent to let them know their bid request was rejected.
 */
public class MBidRejected extends Message {

    private final BidTracker itemInfo;
    private final int houseID;

    public  MBidRejected(int houseID, BidTracker itemInfo){
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
