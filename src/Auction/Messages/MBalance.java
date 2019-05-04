package Auction.Messages;

/**
 * this is a message that gets sent from the bank to agents with thier balance
 */
public class MBalance extends Message {
    private final int balance;
    public MBalance(int balance) {
        this.balance = balance;
    }
    public int getBalance() {
        return balance;
    }
}
