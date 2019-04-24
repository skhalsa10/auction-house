package Auction.Messages;

public class MRequestItems implements Message {

    private final int agentID;

    public MRequestItems(int agentID){
        this.agentID = agentID;
    }

    public int getAgentID() {
        return agentID;
    }
}
