package Auction.GUI.GUIMessages;

import Auction.AuctionHouse.BidTracker;
import Auction.Messages.*;

/**
 * Message to gui about bid statuses
 */
public class GUIMessageStatus implements GUIMessage {
    private String status;

    /**
     * Message to gui to update the status of bids
     * @param statusMessage status message
     */
    public GUIMessageStatus(Message statusMessage) {
        if(statusMessage instanceof MBidAccepted) {
            status = "Bid Accepted! ";
        }
        else if(statusMessage instanceof MBidOutbid) {
            status = "Outbid!";
        }
        else if(statusMessage instanceof MBidRejected) {
            status = "Bid Rejected!";
        }
        else if(statusMessage instanceof MBidWon) {
            status = "Bid Won!";
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
