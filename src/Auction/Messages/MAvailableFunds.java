package Auction.Messages;

public class MAvailableFunds extends Message {
    private int agentId;
    private final int availableFunds;

    public MAvailableFunds(int agentId, int availableFunds){
        this.agentId = agentId;
        this.availableFunds = availableFunds;
    }

    public int getAvailableFunds() {
        return availableFunds;
    }

    public int getAgentId() {
        return agentId;
    }
}
