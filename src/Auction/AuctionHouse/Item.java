package Auction.AuctionHouse;

import java.io.Serializable;


/**
 * this class is very simple it encapulates a string as an Item and if we need to add a price to it later we can add
 * in this class.
 */
public class Item implements Serializable, Cloneable{
    private final String description;
    private final int ID;

    public Item(String description, int ID){
        this.ID = ID;
        this.description = description;
    }

    /**
     *
     * @return the ID Number of this item
     */
    public int getID() {
        return ID;
    }

    /**
     *
     * @return returns the string representation of this item
     */
    public String getDescription(){
        return description;
    }

    /**
     *
     * @return the string representation of the ITEM very useful
     */
    @Override
    public String toString() {
        return ""+ID+": " +description;
    }

    @Override
    protected Item clone() throws CloneNotSupportedException {
        Item item = new Item(description, ID);
        return item;
    }
}
