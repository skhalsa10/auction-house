package Auction.Messages;

public class MAvailableFunds implements Message {

    private final int availableFunds;

    public MAvailableFunds(int availableFunds){
        this.availableFunds = availableFunds;
    }

    public int getAvailableFunds() {
        return availableFunds;
    }
}
