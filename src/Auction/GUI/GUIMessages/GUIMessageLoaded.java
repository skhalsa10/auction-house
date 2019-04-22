package Auction.GUI.GUIMessages;

import java.util.ArrayList;
import java.util.List;

public class GUIMessageLoaded extends GUIMessage{
    private List<Integer> houseIDs;

    public GUIMessageLoaded(List<Integer> houseIDs){
        //make a copy of the input
        houseIDs = new ArrayList<>();
        for(Integer id:houseIDs){
            houseIDs.add(id);
        }
    }

    public List<Integer> getHouseIDs() {
        return houseIDs;
    }
}
