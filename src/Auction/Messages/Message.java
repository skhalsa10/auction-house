package Auction.Messages;

public class Message {
    private RequestType requestType;

    public enum RequestType {
        CREATE_ACCOUNT;
    }

    public Message() {

    }

    public RequestType getRequestType() {
        return requestType;
    }
}
