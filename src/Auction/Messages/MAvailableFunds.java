package Auction.Messages;

/**
 * this will tell the client that requested it what its available funds are
 */
public class MAvailableFunds extends Message {
    private final int availableFunds;

    public MAvailableFunds(int availableFunds){
        this.availableFunds = availableFunds;
    }

    public int getAvailableFunds() {
        return availableFunds;
    }

}
