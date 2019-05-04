package Auction.Messages;

/**
 * this message is sent from the agents to the house to request a bid on an item
 */
public class MBid extends Message {

    private final int agentID;
    private final int bidAmount;
    private final int itemID;

    public MBid(int agentID, int itemID, int bidAmount){
        this.agentID = agentID;
        this.itemID = itemID;
        this.bidAmount = bidAmount;
    }

    public int getAgentID() {
        return agentID;
    }

    public int getBidAmount() {
        return bidAmount;
    }

    public int getItemID() {
        return itemID;
    }
}
