package Auction.GUI.GUIMessages;

/**
 * Message to gui to update agent balance
 */
public class GUIMessageBalance extends GUIMessage {
    private int balance;

    /**
     * Constructs a gui message balance
     * @param balance agent balance
     */
    public GUIMessageBalance(int balance) {
        this.balance = balance;
    }

    /**
     * Gets agent balance
     * @return agent balance
     */
    public int getBalance() {
        return balance;
    }
}
