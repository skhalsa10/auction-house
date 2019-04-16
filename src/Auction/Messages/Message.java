package Auction.Messages;

import Auction.Account;
import Auction.AuctionHouse;

public class Message {
    private RequestType requestType;
    private double startingBalance;

    public enum RequestType {
        CREATE_ACCOUNT, CHECK_BALANCE, TRANSFER_FUNDS;
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
