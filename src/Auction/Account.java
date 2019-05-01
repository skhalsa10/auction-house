package Auction;

import java.text.DecimalFormat;

public class Account {
    private int accountNum;
    private double totalBalance;
    private double availableBalance;

    public Account() {
        int newNum = Bank.getAccountCounter();
        newNum++;
        Bank.incrementAccountCounter();

        this.accountNum = newNum;
        this.totalBalance = 0.00;
        this.availableBalance = 0.00;
        System.out.println("Created new account " + this.accountNum + " with balance $" + this.totalBalance);
    }

    public Account(double balance) {
        int newNum = Bank.getAccountCounter();
        newNum++;
        Bank.incrementAccountCounter();

        this.accountNum = newNum;
        this.totalBalance = balance;
        this.availableBalance = balance;
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println("Created new account " + this.accountNum + " with balance $" + df.format(this.totalBalance));
    }
}
