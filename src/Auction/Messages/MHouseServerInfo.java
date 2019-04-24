package Auction.Messages;

public class MHouseServerInfo implements Message {

    private final int housePort;
    private final int houseID;
    private final String houseHostName;

    public MHouseServerInfo(int houseID, String houseHostName, int housePort){
        this.houseID = houseID;
        this.houseHostName = houseHostName;
        this.housePort = housePort;
    }

    public int getHousePort() {
        return housePort;
    }

    public int getHouseID() {
        return houseID;
    }

    public String getHouseHostName() {
        return houseHostName;
    }
}
