package Auction.Messages;

public class MUnblockFunds implements Message {

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
