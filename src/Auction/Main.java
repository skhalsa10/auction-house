package Auction;

import Auction.Messages.Message;

public class Main {
    public static void main(String[] args) {
        Bank daBigBank = new Bank();
        Message msg1 = new Message(Message.RequestType.CREATE_ACCOUNT);
        daBigBank.receiveMessage(msg1);
    }
}
