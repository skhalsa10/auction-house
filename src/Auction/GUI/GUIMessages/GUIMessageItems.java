package Auction.GUI.GUIMessages;

import Auction.AuctionHouse.Item;

import java.util.ArrayList;

/**
 * Message to gui about items
 */
public class GUIMessageItems extends GUIMessage {
    private ArrayList<Item> items;

    /**
     * Constructs message to be sent to gui about items
     * @param items list of items
     */
    public GUIMessageItems(ArrayList<Item> items) {
        this.items = items;
    }

    /**
     * Gets items
     * @return list of items
     */
    public ArrayList<Item> getItems() {
        return items;
    }
}
