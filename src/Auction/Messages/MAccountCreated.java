package Auction.Messages;

public class MAccountCreated implements Message {


    private final int accountID;

    public MAccountCreated(int accountID){
        this.accountID = accountID;
    }

    public int getAccountID() {
        return accountID;
    }
}
