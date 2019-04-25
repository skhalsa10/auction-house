package Auction.GUI;

import Auction.Agent.GUIAgentConnection;
import Auction.AuctionHouse.Item;
import Auction.GUI.GUIMessages.*;
import Auction.Messages.MSelectHouse;
import Auction.Messages.Message;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
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
    private List<Item> items;
    private VBox itemList;

    private GUIAgentConnection connection;
    

    public GUI(Stage primaryStage) {
        this.stage = primaryStage;
        stage.setTitle("Auction");
        page = pageType.LOADING_PAGE;
        refreshNeeded = false;
        isLoading = true;
        messages = new LinkedBlockingQueue<>();

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
        accountNum = new Text("Account: --");
        balance = new Text("Balance: $670,000");
        statusMessage = new Text("this is where a status will go---Siri theory");
        infoBox = new HBox();
        infoBox.setId("info-box");
        infoPane.getChildren().add(infoBox);
        infoBox.getChildren().addAll(accountNum,balance,statusMessage);
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
        placeBidBtn = new Button("Place Bid");
        placeBidBtn.getStyleClass().add("control-button");
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

    private void processMessage() {
        GUIMessage m = messages.poll();
        if(m == null) {return;}
        if(m instanceof GUIMessageLoaded){
            System.out.println("loaded");
            isLoading = false;
            page = pageType.HOUSE_PAGE;
            houseIDs = ((GUIMessageLoaded) m).getHouseIDs();
            refreshNeeded = true;
        }
        else if(m instanceof GUIMessageAccount) {
            System.out.println("gui message account");
            String num = Integer.toString(((GUIMessageAccount) m).getAccountID());
            accountNum.setText("Account: " + num);
        }
        else if(m instanceof GUIMessageBalance) {
            String balanceValue = Integer.toString(((GUIMessageBalance) m).getBalance());
            balance.setText("Balance: $" + balanceValue);

        }
        else if(m instanceof GUIMessageItems) {
            items = ((GUIMessageItems) m).getItems();
            page = pageType.ITEM_PAGE;
            refreshNeeded = true;

        }
        else if(m instanceof GUIMessageAvailableFunds) {

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
                    int selectHouseId = Integer.parseInt(t.getText());
                    MSelectHouse m = new MSelectHouse(selectHouseId);
                    connection.sendMessage(m);
                }
            });
        }

        setSize();

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

        //here I get the list of items but I am making a dummy one for now
        /*items = new ArrayList<>();
        items.add(new Item("test1"));
        items.add(new Item("test2"));
        items.add(new Item("test3"));*/

        itemList.setAlignment(Pos.CENTER);
        itemList.setSpacing(25);
        for (Item item:items) {
            Text t = new Text(item.toString());
            t.setId("item-list");
            itemList.getChildren().add(t);
        }

        controlPane.getChildren().clear();
        controlPane.getChildren().add(controlBox);

        setSize();

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
