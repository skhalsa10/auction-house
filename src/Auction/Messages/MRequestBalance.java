package Auction.Messages;

/**
 * Message from agent to bank requesting balance
 */
public class MRequestBalance extends Message {
    private int agentId;

    /**
     * Request for balance
     * @param agentId
     */
    public MRequestBalance (int agentId) {
        this.agentId = agentId;
    }

    /**
     * Gets id of agent requesting balance
     * @return agent id
     */
    public int getAgentId() {
        return agentId;
    }
}
