package Main;

import DatabaseManipulation.RFIDDatabaseController;
import Persistence.RfidcardEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jmalasics on 11/11/2014.
 */
public class RFIDController {

    private RFIDDatabaseController rfidController;

    private Stage createRFIDStage;
    private Stage removeRFIDStage;

    @FXML
    private TextField rfid;
    @FXML
    private ComboBox<String> rfidDelete;

    public RFIDController() {
        rfidController = new RFIDDatabaseController(Main.PERSISTENCE_UNIT);
        rfid = new TextField();
        rfidDelete = new ComboBox<String>();
    }

    public void displayCreate() throws Exception {
        Parent createRFID = FXMLLoader.load(getClass().getClassLoader().getResource("Main/fxml/CreateRFID.fxml"));
        createRFIDStage = new Stage();
        createRFIDStage.setTitle("Create RFID");
        createRFIDStage.setScene(new Scene(createRFID, 300, 150));
        createRFIDStage.setResizable(false);
        createRFIDStage.show();
    }

    public void displayRemove() throws Exception {
        Parent removeRFID = FXMLLoader.load(getClass().getClassLoader().getResource("Main/fxml/DeleteRFID.fxml"));
        removeRFIDStage = new Stage();
        removeRFIDStage.setTitle("Delete RFID");
        removeRFIDStage.setScene(new Scene(removeRFID, 300, 150));
        removeRFIDStage.setResizable(false);
        List<RfidcardEntity> rfidcardEntities = rfidController.getAllRFID();
        List<String> rfidCodes = new ArrayList<String>();
        for(RfidcardEntity rfidcardEntity : rfidcardEntities) {
            rfidCodes.add(rfidcardEntity.getRfidCode());
        }
        Collections.sort(rfidCodes);
        ObservableList observableList = FXCollections.observableList(rfidCodes);
        rfidDelete.setItems(observableList);
        removeRFIDStage.show();
    }

    public void buildRFID() {
        rfidController.addRFIDCard(rfid.getText().toCharArray());
    }

    public void removeRFID() {
        rfidController.removeRFIDCard(rfidDelete.getValue().toCharArray());
    }

}
