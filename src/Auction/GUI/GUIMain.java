package Auction.GUI;

import javafx.application.Application;
import javafx.stage.Stage;

public class GUIMain extends Application {
    private GUI gui;

    @Override
    public void start(Stage primaryStage) {
        gui = new GUI(primaryStage);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
