package Auction.GUI.GUIMessages;

import java.util.ArrayList;
import java.util.List;

/**
 * Message to gui that houses are available
 */
public class GUIMessageLoaded extends GUIMessage{
    private List<Integer> houseIDs;

    /**
     * Message to update gui to display houses
     * @param houseIDs
     */
    public GUIMessageLoaded(List<Integer> houseIDs){
        //make a copy of the input
        this.houseIDs = new ArrayList<>();
        for(Integer id:houseIDs){
            this.houseIDs.add(id);
        }
    }

    /**
     * Gets house ids
     * @return list of house ids
     */
    public List<Integer> getHouseIDs() {
        return houseIDs;
    }
}
