package Auction.Messages;

/**
 * Message to bank from agent to request available funds
 */
public class MRequestAvailFunds extends Message {
    private int agentId;
    private String name;

    /**
     * Request for available funds
     * @param agentId
     */
    public MRequestAvailFunds(int agentId, String name) {
        this.agentId = agentId;
        this.name = name;
    }

    /**
     * Gets id of agent requesting funds
     * @return agent id
     */
    public int getAgentId() {
        return agentId;
    }

    public String getName() {
        return name;
    }
}
