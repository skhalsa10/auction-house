package Auction;

public class Account {
    private int accountNum;

    public Account() {
        int newNum = Bank.getAccountCounter();
        newNum++;
        Bank.incrememntAccountCounter();

        this.accountNum = newNum;
    }
}
