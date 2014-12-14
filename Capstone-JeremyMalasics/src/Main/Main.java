package Main;

import ArduinoConnection.SerialClass;
import Main.Controllers.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static final String PERSISTENCE_UNIT = "capstone";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Thread hardwareThread = new Thread() {
            public void run() {
                SerialClass serial = new SerialClass();
                serial.initialize();
                serial.writeData("SYSTEM INITIALIZED.");
                boolean running = true;
                while(running) {
                    serial.readData();
                    running = serial.isRunning();
                }
                serial.close();
            }
        };
        hardwareThread.start();
        Parent root = FXMLLoader.load(getClass().getResource("fxml/capstoneGUI.fxml"));
        primaryStage.setTitle("Main window");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
