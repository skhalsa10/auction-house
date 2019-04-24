package Auction.Messages;

public class MTransferFunds extends Message {

    private final int amount;
    private final int fromAccount;
    private final int toAccount;

    public  MTransferFunds(int fromAccount, int toAccount, int amount){
        this.fromAccount = fromAccount;
        this.toAccount =toAccount;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public int getFromAccount() {
        return fromAccount;
    }

    public int getToAccount() {
        return toAccount;
    }
}
