package Auction.Messages;

/**
 * Message from agent to bank requesting balance
 */
public class MRequestBalance extends Message {
    private int agentId;
    private String agentName;

    /**
     * Request for balance
     * @param agentId
     */
    public MRequestBalance (int agentId, String agentName) {
        this.agentName = agentName;
        this.agentId = agentId;
    }

    /**
     * Gets id of agent requesting balance
     * @return agent id
     */
    public int getAgentId() {
        return agentId;
    }

    public String getAgentName() {
        return agentName;
    }
}
