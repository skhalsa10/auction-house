/***********************************
 * Alexandra Valdez
 * CS 351-002
 * April 24, 2019
 ***********************************/
package Auction.Bank;

public class Account {

    private String name;
    private int accountNum;
    private int totalBalance;
    private int availableBalance;

    // No arguments constructor
    public Account() {
        int newNum = Bank.getAccountCounter();
        newNum++;
        Bank.incrementAccountCounter();

        this.name = null;
        this.accountNum = newNum;
        this.totalBalance = 0;
        this.availableBalance = 0;
        System.out.println("Created new account " + this.accountNum + " with balance $" + this.totalBalance);
    }

    // Given name and starting balance constructor - handles null name
    public Account(String name, int balance) {
        int newNum = Bank.getAccountCounter();
        newNum++;
        Bank.incrementAccountCounter();

        if (name != null) {
            this.name = name;
        }

        this.accountNum = newNum;
        this.totalBalance = balance;
        this.availableBalance = balance;
        System.out.println("Created new account " + this.accountNum + " with balance $" + this.totalBalance);
    }

    public void deductFunds(int amount) {
        totalBalance -= amount;
    }

    public void addFunds(int amount) {
        totalBalance += amount;
    }

    public void blockFunds(int amount) {
        availableBalance -= amount;
    }

    public void unblockFunds(int amount) {
        availableBalance += amount;
    }

    public int getTotalBalance() {
        return totalBalance;
    }

    public int getAvailableBalance() {
        return availableBalance;
    }

    public int getAccountID() {
      return accountNum;
    }

}
