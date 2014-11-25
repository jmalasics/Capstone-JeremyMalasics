package Main;

import DatabaseManipulation.RFIDDatabaseController;
import Persistence.RfidcardEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by jmalasics on 11/11/2014.
 */
public class RFIDController implements Initializable {

    private RFIDDatabaseController rfidController;

    @FXML
    private TextField rfid;
    @FXML
    private ComboBox<String> rfidDelete;

    public RFIDController() {
        rfidController = new RFIDDatabaseController(Main.PERSISTENCE_UNIT);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(rfid != null) {
            buildCreateRFID();
        }
        if(rfidDelete != null) {
            buildRemoveRFID();
        }
    }

    public void buildCreateRFID() {
        rfid.setText("Enter RFID here.");
    }

    public void buildRemoveRFID() {
        rfidDelete.setCellFactory((comboBox) -> {
            return new ListCell<String>() {
                @Override
                protected void updateItem(String s, boolean empty) {
                    super.updateItem(s, empty);
                    if (s == null || empty) {
                        setText(null);
                    } else {
                        setText(s);
                    }
                }
            };
        });
        rfidDelete.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                if(object == null) {
                    return null;
                } else {
                    return object;
                }
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        });
        rfidDelete.setOnAction((event) -> {
            String selectedString = rfidDelete.getSelectionModel().getSelectedItem();
            rfidDelete.setValue(selectedString);
        });
        List<RfidcardEntity> rfidcardEntities = rfidController.getAllRFID();
        List<String> rfidCodes = new ArrayList<String>();
        for(RfidcardEntity rfidcardEntity : rfidcardEntities) {
            rfidCodes.add(rfidcardEntity.getRfidCode());
        }
        Collections.sort(rfidCodes);
        ObservableList observableList = FXCollections.observableList(rfidCodes);
        rfidDelete.setItems(observableList);
    }

    public void buildRFID() {
        rfidController.addRFIDCard(rfid.getText().toCharArray());
        MainWindowController.closeCreateRFID();
    }

    public void removeRFID() {
        rfidController.removeRFIDCard(rfidDelete.getValue().toCharArray());
        MainWindowController.closeRemoveRFID();
    }

}
