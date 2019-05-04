package Auction.GUI.GUIMessages;

/**
 * Message to GUI about bank account id
 */
public class GUIMessageAccount extends GUIMessage{
    private int accountID;

    /**
     * Creates message to GUI about bank account id
     * @param accountID
     */
    public GUIMessageAccount(int accountID) {
        this.accountID = accountID;
    }

    /**
     * Gets the bank account id
     * @return bank account id
     */
    public int getAccountID() {
        return accountID;
    }
}
