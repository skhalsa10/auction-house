package Auction.Messages;

/**
 * this message is sent from agent to house to request a current list of their items.
 */
public class MRequestItems extends Message {

    private final int agentID;

    public MRequestItems(int agentID){
        this.agentID = agentID;
    }

    public int getAgentID() {
        return agentID;
    }
}
