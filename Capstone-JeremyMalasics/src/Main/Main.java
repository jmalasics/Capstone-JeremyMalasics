package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private MainWindowController mainWindowController;
    private DeviceController createDeviceController;
    private EmployeeController createEmployeeController;
    public static final String PERSISTENCE_UNIT = "capstone";

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainWindowController = new MainWindowController();
        createEmployeeController = new EmployeeController();
        createDeviceController = new DeviceController();
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
