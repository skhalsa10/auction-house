package Auction.Messages;

/**
 * message sent from the house to the bank to unblock funds for an agent
 */
public class MUnblockFunds extends Message {

    private final int amount;
    private final int houseID;
    private final int agentID;

    public MUnblockFunds(int houseID, int agentID, int amount){
        this.houseID = houseID;
        this.agentID = agentID;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public int getHouseID() {
        return houseID;
    }

    public int getAgentID() {
        return agentID;
    }
}
