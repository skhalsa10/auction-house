package Auction.GUI;

import Auction.Agent.Agent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Main Entry Point for Agent
 */
public class GUIMain extends Application {
    private GUI gui;
    private Agent a;

    /**
     * Starts the agent and the gui
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {

        gui = new GUI(primaryStage);
        gui.start();
        try {
            String bankHost = getParameters().getUnnamed().get(0);
            int bankPortNum = Integer.parseInt(getParameters().getUnnamed().get(1));
            String name = getParameters().getUnnamed().get(2);
            int initialBalance = Integer.parseInt(getParameters().getUnnamed().get(3));
            a = new Agent(bankHost, bankPortNum, name, initialBalance, gui);
            gui.setGUIAgentConnection(a.getMessages());
        }
        catch (Exception e ) {
            System.out.println("Arguments: Bank Hostname, Bank Port, Agent Name, Initial Balance");
            System.out.println("Please close and try again");
        }
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(a != null) {
                    if(a.getOngoingBids() == 0) {
                        System.out.println("No ongoing bids");
                        a.shutDown();
                        Platform.exit();
                        System.exit(0);
                    }
                    else {
                        System.out.println("There are still ongoing bids!");
                        event.consume();
                    }
                }
                else {
                    Platform.exit();
                    System.exit(0);
                }

            }
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}
