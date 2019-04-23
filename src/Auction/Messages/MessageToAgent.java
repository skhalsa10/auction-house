package Auction.Messages;

import java.io.Serializable;

public class MessageToAgent extends Message {
    private RequestType requestType;
    private int houseId;
    private int bidAmount;
    private int itemId;

    public MessageToAgent(RequestType selectHouse, int houseId) {
        this.requestType = selectHouse;
        this.houseId = houseId;
    }

    public MessageToAgent(RequestType bidOnItem, int itemId, int bidAmount) {
        this.requestType = bidOnItem;
        this.itemId = itemId;
        this.bidAmount = bidAmount;
    }

    public enum RequestType implements Serializable {
        SELECT_HOUSE, BID_ITEM
    }

    public int getHouseId() {
        return houseId;
    }

    public int getBidAmount() {
        return bidAmount;
    }

    public void printMessage() {
        System.out.println("Message Type " + this.requestType);
        System.out.println("House ID: " + houseId);
    }


}
