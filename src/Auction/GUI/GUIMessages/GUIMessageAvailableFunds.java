package Auction.GUI.GUIMessages;

/**
 * Message to gui about available funds
 */
public class GUIMessageAvailableFunds implements GUIMessage {
    private int availFunds;

    /**
     * Constructs message to update gui about available funds
     * @param availFunds
     */
    public GUIMessageAvailableFunds(int availFunds) {
        this.availFunds = availFunds;
    }

    /**
     * Gets available funds
     * @return available funds
     */
    public int getAvailFunds() {
        return availFunds;
    }
}
