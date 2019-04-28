/***********************************
 * Alexandra Valdez
 * CS 351-002
 * April 24, 2019
 ***********************************/
package Auction.Bank;

import java.text.DecimalFormat;

public class Account {

    private String name;
    private int accountNum;
    private double totalBalance;
    private double availableBalance;

    // No arguments constructor
    public Account() {
        int newNum = Bank.getAccountCounter();
        newNum++;
        Bank.incrementAccountCounter();

        this.name = null;
        this.accountNum = newNum;
        this.totalBalance = 0.00;
        this.availableBalance = 0.00;
        System.out.println("Created new account " + this.accountNum + " with balance $" + this.totalBalance);
    }

    // Given name and starting balance constructor - handles null name
    public Account(String name, double balance) {
        int newNum = Bank.getAccountCounter();
        newNum++;
        Bank.incrementAccountCounter();

        if (name != null) {
            this.name = name;
        }

        this.accountNum = newNum;
        this.totalBalance = balance;
        this.availableBalance = balance;
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println("Created new account " + this.accountNum + " with balance $" + df.format(this.totalBalance));
    }

    public void deductFunds(int amount) {
        totalBalance -= amount;
    }

    public void addFunds(int amount) {
        totalBalance += amount;
    }

    public double getTotalBalance() {
        return totalBalance;
    }

    public int getAccountID() {
      return accountNum;
    }

}
