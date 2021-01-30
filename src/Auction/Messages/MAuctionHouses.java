package Auction.Messages;

import java.util.ArrayList;

/**
 * this message contains a list of house server info messages it gets sent from the bank to all clients
 */
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

