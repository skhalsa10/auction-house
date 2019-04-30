package Auction.GUI.GUIMessages;

import Auction.AuctionHouse.BidTracker;
import Auction.AuctionHouse.Item;

import java.util.ArrayList;

/**
 * Message to gui about items
 */
public class GUIMessageItems extends GUIMessage {
    private ArrayList<BidTracker> bidTrackers;

    /**
     * Constructs message to be sent to gui about items
     * @param bidTrackers list of items
     */
    public GUIMessageItems(ArrayList<BidTracker> bidTrackers) {
        ArrayList<BidTracker> list = new ArrayList<>();
        for(BidTracker b: bidTrackers){
            list.add(b.clone());
        }
        this.bidTrackers = list;
    }

    /**
     * Gets items
     * @return list of items
     */
    public ArrayList<BidTracker> getItems() {
        return bidTrackers;
    }
}
