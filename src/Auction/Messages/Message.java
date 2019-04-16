package Auction.Messages;

import Auction.Account;
import Auction.AuctionHouse;

public class Message {
    private RequestType requestType;
    private Account account;
    private AuctionHouse auctionHouse;
    private String agentName;
    private int agentBalance;

    public enum RequestType {
        CREATE_ACCOUNT, CHECK_BALANCE, TRANSFER_FUNDS, CREATE_AGENT_ACCOUNT;
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

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public void setAgentBalance(int agentBalance) {
        this.agentBalance = agentBalance;
    }
}
