package Auction.Messages;

public class MAvailableFunds extends Message {

    private final double availableFunds;

    public MAvailableFunds(double availableFunds){
        this.availableFunds = availableFunds;
    }

    public double getAvailableFunds() {
        return availableFunds;
    }
}
