package Auction.Messages;

import Auction.AuctionHouse.BidTracker;

public class MBidAccepted implements Message {

    private final BidTracker itemInfo;
    private final int houseID;

    public  MBidAccepted(int houseID, BidTracker itemInfo){
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
