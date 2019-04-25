package Auction.Messages;

/**
 * this Message gets inserted into the houses message queue when no one has bid after X amount of time. there will be a timer that runs and
 * inserts this after X amount of time of inactivity. I will assume that any message that come in afterwords dont matter.
 */
public class MHouseClosedTimer {
    //no parameters are needed
    public MHouseClosedTimer(){}
}
