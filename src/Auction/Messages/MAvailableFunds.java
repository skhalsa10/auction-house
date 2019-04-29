package Auction.Messages;

public class MAvailableFunds extends Message {
    private final int availableFunds;

    public MAvailableFunds(int availableFunds){
        this.availableFunds = availableFunds;
    }

    public int getAvailableFunds() {
        return availableFunds;
    }

}
