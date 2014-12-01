package Main.Controllers;

import DatabaseManipulation.DeviceDatabaseController;
import Main.EventMonitors.WindowMonitor;
import Main.Main;
import Persistence.DeviceEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by jmalasics on 11/10/2014.
 */
public class DeviceController implements Initializable {

    private DeviceDatabaseController deviceController;
    private WindowMonitor monitor;

    @FXML
    private TextField deviceName;
    @FXML
    private ComboBox<String> deleteDevice;

    public DeviceController() {
        deviceController = new DeviceDatabaseController(Main.PERSISTENCE_UNIT);
        monitor = WindowMonitor.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(deviceName != null) {
            buildCreateDevice();
        }
        if(deleteDevice != null) {
            buildRemoveDevice();
        }
    }

    public void buildCreateDevice() {
        deviceName.setText("Enter device name here.");
    }

    public void buildRemoveDevice() {
        deleteDevice.setCellFactory((comboBox) -> {
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
        deleteDevice.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                if (object == null) {
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
        deleteDevice.setOnAction((event) -> {
            String selectedString = deleteDevice.getSelectionModel().getSelectedItem();
            deleteDevice.setValue(selectedString);
        });
        List<DeviceEntity> deviceEntities = deviceController.getDeviceList();
        List<String> deviceNames = new ArrayList<String>();
        for(DeviceEntity deviceEntity : deviceEntities) {
            deviceNames.add(deviceEntity.getDevice());
        }
        Collections.sort(deviceNames);
        ObservableList observableList = FXCollections.observableList(deviceNames);
        deleteDevice.setItems(observableList);
    }

    public void buildDevice() {
        DeviceEntity deviceEntity = deviceController.addDevice(deviceName.getText());
        if(deviceEntity != null) {
            monitor.deviceCreateUpdate(deviceEntity);
        }
    }

    public void removeDevice() {
        DeviceEntity deviceEntity = deviceController.getDevice(deleteDevice.getValue());
        if(deviceEntity != null) {
            deviceController.removeDevice(deviceEntity.getId());
            monitor.deviceRemoveUpdate(deviceEntity);
        }
    }
}
