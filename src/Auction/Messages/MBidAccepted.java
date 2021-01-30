package Auction.Messages;

import Auction.AuctionHouse.BidTracker;

/**
 * this message gets sent from a house to an agent when bid request has been accepted
 */
public class MBidAccepted extends Message {

    private final BidTracker itemInfo;
    private final int houseID;

    public  MBidAccepted(int houseID, BidTracker itemInfo){
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
