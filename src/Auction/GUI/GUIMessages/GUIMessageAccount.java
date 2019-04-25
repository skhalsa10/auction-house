package Auction.GUI.GUIMessages;

public class GUIMessageAccount extends GUIMessage{

    private int accountID;
    public GUIMessageAccount(int accountID) {
        this.accountID = accountID;
    }

    public int getAccountID() {
        return accountID;
    }
}
