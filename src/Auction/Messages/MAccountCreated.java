package Auction.Messages;

/**
 * this message is used to to give the new bank account number/ID to the client that requested a new account
 */
public class MAccountCreated extends Message {
    private final int accountID;

    public MAccountCreated(int accountID){
        this.accountID = accountID;
    }

    public int getAccountID() {
        return accountID;
    }
}
