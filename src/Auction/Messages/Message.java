package Auction.Messages;

import Auction.Account;

public class Message {
    private RequestType requestType;
    private Account account;

    public enum RequestType {
        CREATE_ACCOUNT, CHECK_BALANCE, TRANSFER_FUNDS;
    }

    public Message(Account account) {
        this.account = account;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public Account getAccount() {
        return account;
    }
}
