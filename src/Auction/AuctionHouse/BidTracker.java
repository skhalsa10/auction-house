package Auction.AuctionHouse;

/**
 * The goal of this class is to keep track of an item  and its bidding status
 */
public class BidTracker {
    private final Item item;
    private int  currentBid;
    private int bidOwnerID;

    public BidTracker(Item item){
        this.item = item;
        currentBid = 0;
        bidOwnerID = -1;
    }

    /**
     *
     * @return this returns the account number/ID of the agent that is currently winning the bid
     */
    public int getBidOwnerID() {
        return bidOwnerID;
    }

    /**
     *
     * @return the current dollar amount that has been bid and owned by the owner.
     */
    public int getCurrentBid() {
        return currentBid;
    }


    /**
     *
     * @return returns the Item being tracked
     */
    public Item getItem() {
        return item;
    }

    /**
     * this will update the bid and the owner of the bid. it has to be done togethor
     * @param bidAmount bid amount that will now be the current bid
     * @param agentID Agent ID/Account # that placed the bid.
     * @return true if we successfully updated the bid. for the bid to update the bid amount must be greater than the current bid
     * if there was a minimum bid it would have to be greater by this number.
     */
    public synchronized Boolean setBid(int bidAmount, int agentID){
        if(bidAmount < currentBid) {return false;}
        currentBid = bidAmount;
        bidOwnerID = agentID;
        return true;
    }
}
