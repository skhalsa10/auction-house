package Auction.Messages;

public class MShutDown implements Message {

    private final int ID;

    public  MShutDown(int ID){
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
}
