package Auction.Messages;

/**
 * Message to bank from agent to request houses
 */
public class MRequestHouses extends Message {
    private final int agentId;

    /**
     * Constructs request houses message
     * @param agentId
     */
    public MRequestHouses(int agentId) {
        this.agentId = agentId;
    }

    /**
     * Gets id of agent requesting houses
     * @return id of agent
     */
    public int getAgentId() {
        return agentId;
    }
}

