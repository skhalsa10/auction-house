package Auction.GUI;

import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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


    //OTHER Stuff
    private long lastUpdate = 0;
    private Boolean refreshNeeded;
    private pageType page;
    //loading stuff
    private final String load1 = "LOADING";
    private final String load2 = "LOADING  .";
    private final String load3 = "LOADING  .  .";
    private final String load4 = "LOADING  .  .  .";
    private int loadState = 1;
    private Text loadText;
    private int loadSlow = 0;

    //Information stuff
    private Text accountNum;
    private Text balance;
    private Text statusMessage;


    //HOUSEPAGE stuff
    private Text housePageTitle;
    private VBox houseList;



    

    public GUI(Stage primaryStage) {
        this.stage = primaryStage;
        stage.setTitle("Auction");
        page = pageType.LOADING_PAGE;
        refreshNeeded = true;

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

        //change properties of root pane
        root.setAlignment(Pos.CENTER);

        //root.minWidthProperty().bind(stage.widthProperty());


        scene = new Scene(root,500,500);
        stage.setMinWidth(500);
        stage.setMinHeight(500);
        scene.getStylesheets().add("Auction/GUI/GUI.css");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void handle(long now) {
        if (now - lastUpdate >= 33_334_000) {
            if (refreshNeeded) {
                switch (page) {
                    case LOADING_PAGE: {
                        //System.out.println("hi");
                        System.out.println(loadState);
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

    private void rendorHousePage() {

    }

    private void rendorItemPage() {

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
    }

    private enum pageType {
        LOADING_PAGE, HOUSE_PAGE, ITEM_PAGE
    }

}
