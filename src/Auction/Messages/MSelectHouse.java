package Auction.Messages;

/**
 * Message to agent from gui of selected house
 */
public class MSelectHouse extends Message {
    private int houseId;

    /**
     * Constructs message to agent about selected house from gui
     * @param houseId
     */
    public MSelectHouse(int houseId) {
        this.houseId = houseId;
    }

    /**
     * Gets house id
     * @return house id of selected house
     */
    public int getHouseId(){
        return houseId;
    }
}
