package Auction.Messages;

import Auction.AuctionHouse.BidTracker;

import java.io.Serializable;
import java.rmi.registry.Registry;
import java.util.ArrayList;


public class Message implements Serializable {
    private int shutdownID;
    private int itemID;
    private RequestType requestType;
    private String name;
    private int startingBalance;
    private int account;
    private int agentID;
    private int houseID;
    private int amount;
    private ArrayList<BidTracker> bidTrackers;
    private BidTracker bidInfo;


    public enum RequestType implements Serializable{
        CREATE_ACCOUNT, ACCOUNT_CREATED, REQUEST_ITEMS,ITEM_LIST, BID, BID_ACCEPTED, BID_REJECTED, BID_OUTBID, BID_WON,
        SHUT_DOWN, BLOCK_ACCEPTED, BLOCK_REJECTED, BLOCK_FUNDS, UNBLOCK_FUNDS,
        FUNDS_TRANSFERRED,TRANSFER_FUNDS, HOUSE_SERVER_INFO, AUCTION_HOUSES, AVAILABLE_FUNDS;
    }

    /**
     * Use this for creating an account the Agent and the house can use it to open an account with the  bank
     * @param type This shoul
     * @param name null if a House or a name if an agent
     * @param startingBalance this is the starting balance in the account. the house should start with 0
     */
    public Message(RequestType type, String name, int startingBalance) {
        if (type != RequestType.CREATE_ACCOUNT) {
            System.out.println("this constructor was meant to be used for the CREATE ACCOUNT type");
        }
        this.requestType = type;
        this.name = name;
        this.startingBalance = startingBalance;
    }

    /**
     * this Constructor is compatible with the following Request types:
     *
     *  REQUEST_ITEMS
     *  ACCOUNT_CREATED
     *  SHUT_DOWN
     *
     * @param type one of the types above
     * @param accountID this will be set to agentID, account, or shutdownID according to the type
     */
    public Message(RequestType type,int accountID){
        this.requestType = type;
        if (requestType == RequestType.REQUEST_ITEMS){
            this.agentID = accountID;
        }
        else if(requestType == RequestType.ACCOUNT_CREATED){
            this.account = accountID;
        }
        else if(requestType == RequestType.SHUT_DOWN){
            this.shutdownID = accountID;
        }
        else{
            System.out.println("WRONG type for constructor");
        }

    }

    /**
     * this constructor is compatible with type  BID only
     * @param type this needs to be BID to use this constructor
     * @param ID1 the agent ID/Account number making the bid
     * @param ID2 the item ID the agent is bidding on this ID can be the same in different houses but is unique to the house
     * @param bidAmount the dollar amount being bid.
     */
    public Message(RequestType type, int ID1, int ID2, int bidAmount){
        this.requestType = type;
        this.agentID = agentID;
        this.itemID = itemID;
        this.amount = bidAmount;
    }

    /**
     * this Constructor is compatible with the ITEM_LIST type
     * @param type This needs to be ITEM_LIST
     * @param houseID houseID that is sending the list of ITEMS it has
     * @param bidTRackers this is a list of bid trackers wich include Items and their bid info
     */
    public Message(RequestType type, int houseID, ArrayList<BidTracker> bidTRackers){
        this.requestType = type;
        this.houseID = houseID;
        this.bidTrackers = bidTRackers;
    }

    /**
     * This Constructor has to be used with the following types:
     *
     *  BID_ACCEPTED
     *  BID_REJECTED
     *  BID_OUTBID
     *  BID_WON
     *
     * @param type please see above for accepted bid types
     * @param bidInfo
     */
    public Message(RequestType type, BidTracker bidInfo){
        this.requestType = type;
        this.bidInfo = bidInfo;

    }

    public Message(RequestType type, int fromAccount, int toAccount, int amount){

    }


}
