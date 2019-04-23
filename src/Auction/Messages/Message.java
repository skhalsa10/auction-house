package Auction.Messages;

import java.io.Serializable;

public class Message implements Serializable {
    private RequestType requestType;

    //needed for most requestTypes
    private int ID;

    //needed for CREATE_ACCOUNT
    private int openBalance;
    private double startingBalance;

    //needed for HOUSE_SERVER_INFO
    private String houseHost;
    private int housePort;

    public Message(RequestType type) {
        this.requestType = type;
    }

    public void setOpenBalance(int openBalance) {
        this.openBalance = openBalance;
    }

    public int getOpenBalance() {
        return openBalance;
    }

    public double getStartingBalance() {
        return startingBalance;
    }

    public void setStartingBalance(double startingBalance) {
        this.startingBalance = startingBalance;
    }

    public enum RequestType implements Serializable{
        CREATE_ACCOUNT, ACCOUNT_CREATED, CHECK_BALANCE, TRANSFER_FUNDS, ACCEPT_BID, REJECT_BID, SHUT_DOWN,
        FUNDS_AVAIL, FUNDS_NOT_AVAIL, FUNDS_TRANSFERRED, ITEM_WON, HOUSE_SERVER_INFO;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public String getHouseHost() {
        return houseHost;
    }

    public void setHouseHost(String houseHost) {
        this.houseHost = houseHost;
    }

    public int getHousePort() {
        return housePort;
    }

    public void setHousePort(int housePort) {
        this.housePort = housePort;
    }
}
