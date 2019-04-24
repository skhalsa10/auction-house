package Auction.Messages;

public class MBlockAccepted implements Message {
    private final int itemID;
    private final int agentID;
    private final int amount;

    public MBlockAccepted(int agentID, int amount, int itemID) {
        this.agentID = agentID;
        this.amount = amount;
        this.itemID = itemID;
    }

    public int getItemID() {
        return itemID;
    }

    public int getAgentID() {
        return agentID;
    }

    public int getAmount() {
        return amount;
    }
}
