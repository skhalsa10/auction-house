package Auction.Messages;

import Auction.AuctionHouse.BidTracker;
import java.util.ArrayList;

public class MItemList extends Message{

    private final int houseID;
    private final ArrayList<BidTracker> bidTrackers;

    public MItemList(int houseID, ArrayList<BidTracker> bidTrackers){
        ArrayList<BidTracker> list = new ArrayList<>();
        for(BidTracker b: bidTrackers){
            list.add(b.clone());
        }
        this.houseID = houseID;
        this.bidTrackers = list;

    }

    public int getHouseID() {
        return houseID;
    }

    public ArrayList<BidTracker> getBidTrackers() {
        ArrayList<BidTracker> temp = new ArrayList<>();

        for (BidTracker b : bidTrackers){

            //TODO may need to add a clone function to bittracker to maek this safe.
            temp.add(b);
        }
        return temp;
    }
}

