package Auction.Messages;

/**
 * Message to bank from agent to request houses
 */
public class MRequestHouses extends Message {
    private final int agentId;
    private String agentName;

    /**
     * Constructs request houses message
     * @param agentId
     */
    public MRequestHouses(int agentId, String agentName) {
        this.agentName = agentName;
        this.agentId = agentId;
    }

    /**
     * Gets id of agent requesting houses
     * @return id of agent
     */
    public int getAgentId() {
        return agentId;
    }

    public String getAgentName() {
        return agentName;
    }


}

