package Auction.Messages;

/**
 * Message to bank from agent to request available funds
 */
public class MRequestAvailFunds extends Message {
    private int agentId;

    /**
     * Request for available funds
     * @param agentId 
     */
    public MRequestAvailFunds(int agentId) {
        this.agentId = agentId;
    }

    /**
     * Gets id of agent requesting funds
     * @return agent id
     */
    public int getAgentId() {
        return agentId;
    }
}
