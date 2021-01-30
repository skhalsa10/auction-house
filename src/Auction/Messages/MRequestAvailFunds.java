package Auction.Messages;

/**
 * Message to bank from agent to request available funds
 */
public class MRequestAvailFunds extends Message {
    private int agentId;
    private String agentName;

    /**
     * Request for available funds
     * @param agentId
     */
    public MRequestAvailFunds(int agentId, String agentName) {
        this.agentName = agentName;
        this.agentId = agentId;
    }

    /**
     * Gets id of agent requesting funds
     * @return agent id
     */
    public int getAgentId() {
        return agentId;
    }

    public String getAgentName() {
        return agentName;
    }
}
