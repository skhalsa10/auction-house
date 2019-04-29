package Auction.Messages;

import Auction.AuctionHouse.BidTracker;

public class MBidRejected extends Message {

    private final BidTracker itemInfo;
    private final int houseID;

    public  MBidRejected(int houseID, BidTracker itemInfo){
        this.houseID = houseID;
        this.itemInfo = itemInfo;
    }

    public BidTracker getItemInfo() {
        return itemInfo;
    }

    public int getHouseID() {
        return houseID;
    }
}
