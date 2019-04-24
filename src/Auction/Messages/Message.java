package Auction.Messages;

import Auction.Account;
import Auction.AuctionHouse;

import java.io.Serializable;

public class Message implements Serializable {
    private RequestType requestType;
    private Account account;
    private AuctionHouse auctionHouse;

    public enum RequestType implements Serializable{
        CREATE_ACCOUNT, CHECK_BALANCE, TRANSFER_FUNDS;
    }

    public Message(RequestType type) {
        this.requestType = type;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public Account getAccount() {
        return account;
    }
}
