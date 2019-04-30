package Auction.GUI.GUIMessages;

import Auction.AuctionHouse.BidTracker;
import Auction.AuctionHouse.Item;
import Auction.Messages.*;

/**
 * Message to gui about bid statuses
 */
public class GUIMessageStatus extends GUIMessage {
    private String status;

    /**
     * Message to gui to update the status of bids
     * @param statusMessage status message
     */
    public GUIMessageStatus(Message statusMessage) {
        Item item;
        int itemId;
        int houseId;
        String itemDescription;

        if(statusMessage instanceof MBidAccepted) {
            item = ((MBidAccepted) statusMessage).getItemInfo().getItem();
            itemId = item.getID();
            houseId = ((MBidAccepted) statusMessage).getHouseID();
            itemDescription = item.getDescription();

            status = "Bid Accepted! " + "Item: #" + itemId + " " + itemDescription + " from House: #" + houseId;
        }
        else if(statusMessage instanceof MBidOutbid) {
            item = ((MBidOutbid) statusMessage).getItemInfo().getItem();
            itemId = item.getID();
            houseId = ((MBidOutbid) statusMessage).getHouseID();
            itemDescription = item.getDescription();
            status = "Outbid! "  + "Item: #" + itemId + " " + itemDescription + " from House: #" + houseId;
        }
        else if(statusMessage instanceof MBidRejected) {
            item = ((MBidRejected) statusMessage).getItemInfo().getItem();
            itemId = item.getID();
            houseId = ((MBidRejected) statusMessage).getHouseID();
            itemDescription = item.getDescription();
            status = "Bid Rejected! "+ "Item: #" + itemId + " " + itemDescription + " from House: #" + houseId;
        }
        else if(statusMessage instanceof MBidWon) {
            item = ((MBidWon) statusMessage).getItemInfo().getItem();
            itemId = item.getID();
            houseId = ((MBidWon) statusMessage).getHouseID();
            itemDescription = item.getDescription();
            status = "Bid Won! " + "Item: #" + itemId + " " + itemDescription + " from House: #" + houseId;
        }
    }

    /**
     * Get status
     * @return status
     */
    public String getStatus() {
        return status;
    }
}
