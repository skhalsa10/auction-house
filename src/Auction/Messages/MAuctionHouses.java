package Auction.Messages;

import java.util.ArrayList;

public class MAuctionHouses extends Message {
    ArrayList<MHouseServerInfo> houses;
    public MAuctionHouses(ArrayList<MHouseServerInfo> houses) {
        this.houses = houses;
    }

    public ArrayList<MHouseServerInfo> getHouses() {
        return houses;
    }
}
