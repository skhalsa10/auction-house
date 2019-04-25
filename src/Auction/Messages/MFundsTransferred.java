package Auction.Messages;

public class MFundsTransferred extends Message {

    private final double newBalance;
    private final int fromAccount;
    private final int toAccount;

    public MFundsTransferred(int fromAccount, int toAccount, double newBalance){
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.newBalance = newBalance;
    }

    public double getNewBalance() {
        return newBalance;
    }

    public int getFromAccount() {
        return fromAccount;
    }

    public int getToAccount() {
        return toAccount;
    }
}
