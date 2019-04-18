package Auction.GUI;

import javafx.animation.AnimationTimer;
import javafx.stage.Stage;

public class GUI extends AnimationTimer {
    private Stage stage;
    

    public GUI(Stage primaryStage) {
        this.stage = primaryStage;
        stage.setTitle("Auction");


        stage.show();
    }

    @Override
    public void handle(long now) {

    }

    private enum pageType {
        AUCTION_HOUSE, ITEM_LIST
    }

}
