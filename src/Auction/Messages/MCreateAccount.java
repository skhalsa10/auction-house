package Auction.Messages;

public class MCreateAccount extends Message {

    private final int startingBalance;
    private final String name;

    public MCreateAccount(String name, int startingBalance){
        this.name = name;
        this.startingBalance = startingBalance;
    }

    public int getStartingBalance() {
        return startingBalance;
    }

    public String getAgentName() {
        return name;
    }
}
