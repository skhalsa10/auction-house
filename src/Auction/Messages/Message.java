package Auction.Messages;

import java.io.Serializable;

public class Message implements Serializable {
    private RequestType requestType;
    private double startingBalance;

    public enum RequestType implements Serializable{
        CREATE_ACCOUNT, CHECK_BALANCE, TRANSFER_FUNDS, ACCEPT_BID, REJECT_BID,
        SHUT_DOWN, FUNDS_AVAIL, FUNDS_NOT_AVAIL, FUNDS_TRANSFERRED, ITEM_WON;
    }

    public Message() {

    }

    public Message(RequestType type) {
        this.requestType = type;
    }

    public Message(RequestType type, double startingBalance) {
        this.requestType = type;
        this.startingBalance = startingBalance;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public double getStartingBalance() {
        return startingBalance;
    }
}
