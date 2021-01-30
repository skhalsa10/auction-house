package Auction.Messages;

/**
 * this message is used by house, agent, and bank to tell others they are shutting down.
 */
public class MShutDown extends Message {

    private final int ID;
    private final String name;

    public  MShutDown(int ID, String name){
        this.name = name;
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }
}
