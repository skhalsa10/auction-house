package Auction.GUI;

import Auction.Agent.GUIAgentConnection;
import Auction.AuctionHouse.BidTracker;
import Auction.AuctionHouse.Item;
import Auction.GUI.GUIMessages.*;
import Auction.Messages.MBid;
import Auction.Messages.MRequestHouses;
import Auction.Messages.MSelectHouse;
import Auction.Messages.Message;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.LinkedBlockingQueue;

public class GUI extends AnimationTimer {
    private final int LOADCAP = 5;
    //GUI stuff
    private Stage stage;
    private Scene scene;
    private VBox root;
    private StackPane titlePane;
    private StackPane listPane;
    private StackPane controlPane;
    private StackPane infoPane;
    private HBox infoBox;



    //OTHER Stuff
    private long lastUpdate = 0;
    private Boolean refreshNeeded;
    private Boolean isLoading;
    private pageType page;
    private LinkedBlockingQueue<GUIMessage> messages;

    //loading stuff
    private final String load1 = "LOADING";
    private final String load2 = "LOADING  .";
    private final String load3 = "LOADING  .  .";
    private final String load4 = "LOADING  .  .  .";
    private int loadState = 1;
    private Text loadText;
    private int loadSlow = 0;

    //Information stuff
    //private final Text accountNum;
    private Text accountNum;
    private Text balance;
    private Text availableFunds;
    private Text statusMessage;


    //HOUSEPAGE stuff
    private Text housePageTitle;
    private VBox houseList;
    private List<Integer> houseIDs;

    //control stuff
    private HBox controlBox;
    private Text itemPageTitle;
    private int selectedHouseID;
    private Pane space1;
    private Pane space2;
    private Button backBtn;
    private Button placeBidBtn;
    private TextField bidAmount;
    private Text selectedItem;

    //Item Stuff
    private List<BidTracker> bidTrackers;
    private VBox itemList;
    private int selectedItemID;

    private GUIAgentConnection connection;
    

    public GUI(Stage primaryStage) {
        this.stage = primaryStage;
        stage.setTitle("Auction");
        page = pageType.LOADING_PAGE;
        refreshNeeded = false;
        isLoading = true;
        messages = new LinkedBlockingQueue<>();
        selectedItemID = -1;
        selectedHouseID = -1;

        //initialize all skeleton panes
        root = new VBox();
        titlePane = new StackPane();
        listPane = new StackPane();
        controlPane = new StackPane();
        infoPane = new StackPane();

        //initialize Load stuff
        loadText = new Text(load1);

        //add the skeleton panes to the root pane
        root.getChildren().addAll(titlePane,listPane,controlPane,infoPane);
        root.setSpacing(10);
        root.setVgrow(listPane,Priority.ALWAYS);

        //change properties of root pane
        root.setAlignment(Pos.CENTER);

        //init info data
        availableFunds = new Text("Available: --");
        accountNum = new Text("Account: --");
        balance = new Text("Balance: --");
        statusMessage = new Text("this is where a status will go---Siri theory");
        infoBox = new HBox();
        infoBox.setId("info-box");
        infoPane.getChildren().add(infoBox);
        infoBox.getChildren().addAll(accountNum,balance,availableFunds ,statusMessage);
        infoBox.setHgrow(statusMessage, Priority.ALWAYS);

        //Auction House stuff
        housePageTitle = new Text("Available Auction Houses");
        housePageTitle.setId("house-title");
        houseList = new VBox();
        houseIDs = new ArrayList<>();

        //Control box stuff
        controlBox = new HBox();
        itemList = new VBox();
        space1 = new Pane();
        space2 = new Pane();
        backBtn = new Button("<-");
        backBtn.getStyleClass().add("control-button");
        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                backBtnHandler();
            }
        });
        placeBidBtn = new Button("Place Bid");
        placeBidBtn.getStyleClass().add("control-button");
        placeBidBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(selectedItem.getText().equals("")){
                    System.out.println("No item Selected");
                    return;
                }
                String s = bidAmount.getText();
                for(int i = 0; i< s.length(); i++){
                    if(s.charAt(i)<'0'||s.charAt(i)>'9'){
                        System.out.println("must use Numerical values in bidAmmount Text field. TRY AGAIN!");
                        return;
                    }
                }
                connection.sendMessage(new MBid(selectedHouseID,Integer.parseInt(selectedItem.getText()),Integer.parseInt(bidAmount.getText())));
            }
        });
        bidAmount =  new TextField();
        selectedItem = new Text();
        selectedItem.setId("selected-id-text");
        controlBox.getChildren().addAll(backBtn,space1,selectedItem,bidAmount,space2,placeBidBtn);
        controlBox.setAlignment(Pos.CENTER);
        controlBox.setSpacing(5);
        controlBox.setHgrow(space1,Priority.ALWAYS);
        controlBox.setHgrow(space2,Priority.ALWAYS);
        //this needs to update dynamicall but putting -7 for testing
        selectedHouseID = -7;

        scene = new Scene(root,500,500);
        stage.setMinWidth(500);
        stage.setMinHeight(500);
        scene.getStylesheets().add("Auction/GUI/GUI.css");
        stage.setScene(scene);
        stage.show();
    }

    private void backBtnHandler() {
        page = pageType.LOADING_PAGE;
        isLoading = true;
        connection.sendMessage(new MRequestHouses(-1,""));
    }

    public void sendMessage(GUIMessage m){
        try {
            messages.put(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setGUIAgentConnection(LinkedBlockingQueue<Message> toAgentMessages) {
        this.connection = new GUIAgentConnection(toAgentMessages);
    }

    @Override
    public void handle(long now) {
        if (now - lastUpdate >= 33_334_000) {

            processMessage();

            if (isLoading || refreshNeeded) {
                switch (page) {
                    case LOADING_PAGE: {
                        //System.out.println("hi");
                        //System.out.println(loadState);
                        renderLoadingPage();
                        break;
                    }
                    case ITEM_PAGE: {
                        rendorItemPage();
                        refreshNeeded = false;
                        break;
                    }
                    case HOUSE_PAGE: {
                        rendorHousePage();
                        refreshNeeded = false;
                        break;
                    }
                }
            }
            // helped to stabalize the rendor time
            lastUpdate = now;
        }
    }

    private boolean openedAuctionHouse(GUIMessage m) {
        if(((GUIMessageLoaded) m).getHouseIDs().contains(selectedHouseID)) {
            return true;
        }
        return false;
    }

    private void processMessage() {
        GUIMessage m = messages.poll();
        if(m == null) {return;}
        if(m instanceof GUIMessageLoaded){
            System.out.println("loaded");
            if(page == pageType.ITEM_PAGE && openedAuctionHouse(m)) {
                return;
            }
            isLoading = false;
            page = pageType.HOUSE_PAGE;
            houseIDs = ((GUIMessageLoaded) m).getHouseIDs();
            selectedHouseID = -1;
            refreshNeeded = true;
        }
        else if(m instanceof GUIMessageAccount) {
            System.out.println("gui message account");
            String num = Integer.toString(((GUIMessageAccount) m).getAccountID());
            accountNum.setText("Account: " + num);
        }
        else if(m instanceof GUIMessageBalance) {
            //String balanceValue = Integer.toString(((GUIMessageBalance) m).getBalance());
            NumberFormat numFormat = NumberFormat.getNumberInstance(Locale.US);
            String balanceValue = numFormat.format(((GUIMessageBalance) m).getBalance());
            balance.setText("Balance: $" + balanceValue);

        }
        else if(m instanceof GUIMessageItems) {

            if(selectedHouseID != -1){
                page = pageType.ITEM_PAGE;
            }
            if(((GUIMessageItems) m).getItems().get(0).getHouseID() == selectedHouseID){
                bidTrackers = ((GUIMessageItems) m).getItems();
            }
            refreshNeeded = true;

        }
        else if(m instanceof GUIMessageAvailableFunds) {
            //String availValue = Integer.toString(((GUIMessageAvailableFunds) m).getAvailFunds());
            NumberFormat numFormat = NumberFormat.getNumberInstance(Locale.US);
            String availValue = numFormat.format(((GUIMessageAvailableFunds) m).getAvailFunds());
            availableFunds.setText("Available: $" + availValue);
        }
        else if(m instanceof GUIMessageStatus) {
            statusMessage.setText(((GUIMessageStatus) m).getStatus());

        }
    }

    private void rendorHousePage() {
        titlePane.getChildren().clear();
        listPane.getChildren().clear();
        controlPane.getChildren().clear();
        infoPane.getChildren().clear();

        //TODO get list of houses here the following are dummy ones
        /*houseIDs.add(1);
        houseIDs.add(2);
        houseIDs.add(3);*/

        titlePane.getChildren().add(housePageTitle);
        titlePane.setId("house-title-pane");
        listPane.getChildren().add(houseList);
        infoPane.getChildren().add(infoBox);

        //generate a list of houses and add them to the list for display
        houseList.getChildren().clear();
        houseList.setAlignment(Pos.CENTER);
        houseList.setSpacing(25);
        //houseList.setPadding(new Insets(50,10,50,0));
        for (Integer houseID:houseIDs) {
            HBox h = new HBox();
            Pane spacer1 = new Pane();
            Pane spacer2 = new Pane();
            Text description = new Text("Auction House Number | ");
            description.setId("house-list");
            Text id = new Text(houseID.toString());
            id.setId("house-list");
            h.getChildren().addAll(spacer1, description,id, spacer2);
            h.setHgrow(spacer1,Priority.ALWAYS);
            h.setHgrow(spacer2,Priority.ALWAYS);
            houseList.getChildren().add(h);
            h.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    HBox temp = (HBox) event.getSource();
                    //System.out.println(temp.getChildren().size());
                    Text t = (Text) temp.getChildren().get(2);
                    System.out.println("ID is " + t.getText());
                    selectedHouseID = Integer.parseInt(t.getText());
                    MSelectHouse m = new MSelectHouse(selectedHouseID);
                    connection.sendMessage(m);
                }
            });
        }

        //setSize();

    }

    private void setSize() {

        System.out.println(root.getWidth());
        stage.setWidth(getMaxWidth());
        stage.setHeight(root.getHeight());
    }

    private double getMaxWidth() {
        System.out.println(titlePane.getChildren().get(0));
        double max = titlePane.getWidth();
        if(listPane.getWidth()>max){
            max = listPane.getWidth();
        }
        if(controlPane.getWidth()>max){
            max = controlPane.getWidth();
        }
        if(infoPane.getWidth()>max){
            max = infoPane.getWidth();
        }
        return max;
    }

    private void rendorItemPage() {
        titlePane.getChildren().clear();
        itemPageTitle = new Text("Auction House #" + selectedHouseID + " available items:");
        itemPageTitle.setId(("item-title"));
        titlePane.setId("house-title-pane");
        titlePane.getChildren().add(itemPageTitle);
        listPane.getChildren().clear();
        listPane.getChildren().add(itemList);

        itemList.setAlignment(Pos.CENTER);
        itemList.setSpacing(25);
        itemList.getChildren().clear();
        for (BidTracker bt:bidTrackers) {
            HBox h = new HBox();
            Pane spacer1 = new Pane();
            Pane spacer2 = new Pane();
            Text itemID = new Text(""+bt.getItem().getID());

            StringBuilder sb = new StringBuilder();
            sb.append(" | " +bt.getItem().getDescription());
            sb.append(" | Current Bid: " + bt.getCurrentBid());
            sb.append(" | Bid Owner ID: " + bt.getBidOwnerID());
            sb.append(" | Minimum bid " + bt.getMinimumBid());
            Text t = new Text(sb.toString());
            t.setId("item-list");
            itemID.setId("item-list");
            h.getChildren().addAll(spacer1,itemID,t,spacer2);
            h.setHgrow(spacer1,Priority.ALWAYS);
            h.setHgrow(spacer2,Priority.ALWAYS);
            itemList.getChildren().add(h);
            h.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    HBox temp = (HBox) event.getSource();
                    Text t2 = (Text) temp.getChildren().get(1);
                    if(selectedItem.getText().equals(t2.getText())){
                        selectedItem.setText("");
                    }else{
                        selectedItem.setText(t2.getText());
                    }

                }
            });
        }

        controlPane.getChildren().clear();
        controlPane.getChildren().add(controlBox);

        //setSize();

    }

    private void renderLoadingPage() {
        titlePane.getChildren().clear();
        listPane.getChildren().clear();
        switch(loadState){
            case 1:{
                loadText.setText(load1);
                if(loadSlow++ ==LOADCAP){
                    loadSlow = 0;
                    loadState++;
                }
                break;
            }
            case 2:{
                loadText.setText(load2);
                if(loadSlow++ ==LOADCAP){
                    loadSlow = 0;
                    loadState++;
                }
                break;
            }
            case 3:{
                loadText.setText(load3);
                if(loadSlow++ ==LOADCAP){
                    loadSlow = 0;
                    loadState++;
                }
                break;
            }
            case 4:{
                loadText.setText(load4);
                if(loadSlow++ ==LOADCAP){
                    loadSlow = 0;
                    loadState=1;
                }
                break;
            }
        }
        loadText.setId("load-text");
        listPane.getChildren().add(loadText);
        controlPane.getChildren().clear();
        infoPane.getChildren().clear();
        stage.setWidth(500);
        stage.setHeight(500);
    }

    private enum pageType {
        LOADING_PAGE, HOUSE_PAGE, ITEM_PAGE
    }

}
