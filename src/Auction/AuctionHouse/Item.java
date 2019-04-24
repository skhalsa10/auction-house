package Auction.AuctionHouse;

/**
 * this class is very simple it encapulates a string as an Item and if we need to add a price to it later we can add
 * in this class.
 */
public class Item {
    private String description;

    public Item(String description){
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
