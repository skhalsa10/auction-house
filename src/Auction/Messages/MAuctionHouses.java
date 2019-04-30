package Auction.Messages;

import java.util.ArrayList;

public class MAuctionHouses extends Message {
    ArrayList<MHouseServerInfo> houses;
    public MAuctionHouses(ArrayList<MHouseServerInfo> houses) {
        this.houses = new ArrayList<>();
        for(MHouseServerInfo h: houses) {
            this.houses.add(h);
        }
    }

    public ArrayList<MHouseServerInfo> getHouses() {
        return houses;
    }
}

