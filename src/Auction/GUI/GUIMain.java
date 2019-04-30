package Auction.GUI;

import Auction.Agent.Agent;
import javafx.application.Application;
import javafx.stage.Stage;

public class GUIMain extends Application {
    private GUI gui;
    private Agent a;

    @Override
    public void start(Stage primaryStage) {

        gui = new GUI(primaryStage);
        gui.start();
        String bankHost = getParameters().getUnnamed().get(0);
        int bankPortNum = Integer.parseInt(getParameters().getUnnamed().get(1));
        String name = getParameters().getUnnamed().get(2);
        int initialBalance = Integer.parseInt(getParameters().getUnnamed().get(3));
        a = new Agent(bankHost, bankPortNum, name, initialBalance, gui);
        gui.setGUIAgentConnection(a.getMessages());

        //a.sendHouseList();

    }

    @Override
    public void stop() throws Exception {
        a.shutDown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
