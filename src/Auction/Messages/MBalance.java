package Auction.Messages;

public class MBalance extends Message {
    private final int balance;
    public MBalance(int balance) {
        this.balance = balance;
    }
    public int getBalance() {
        return balance;
    }
}
