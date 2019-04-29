package Auction.Messages;

public class MBlockFunds extends Message {

    private final int amount;
    private final int houseID;
    private final int agentID;
    private final int itemID;
    private final String houseName;

    public  MBlockFunds(int houseID, int agentID, int itemID, int amount, String houseName){

        this.houseID = houseID;
        this.agentID = agentID;
        this.itemID = itemID;
        this.amount = amount;
        this.houseName = houseName;

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

    public int getItemID() {
        return itemID;
    }

    public String getHouseName() {
        return houseName;
    }
}
