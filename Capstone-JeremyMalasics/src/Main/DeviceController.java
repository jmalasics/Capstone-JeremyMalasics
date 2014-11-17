package Main;

import DatabaseManipulation.DeviceDatabaseController;
import Persistence.DeviceEntity;
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
 * Created by jmalasics on 11/10/2014.
 */
public class DeviceController {

    private DeviceDatabaseController deviceController;

    private Stage createDeviceStage;
    private Stage removeDeviceStage;

    @FXML
    private TextField deviceName;
    @FXML
    private ComboBox<String> deleteDevice;

    public DeviceController() {
        deviceController = new DeviceDatabaseController(Main.PERSISTENCE_UNIT);
        deviceName = new TextField();
        deleteDevice = new ComboBox<String>();
    }

    public void displayCreate() throws Exception {
        Parent createDevice = FXMLLoader.load(getClass().getClassLoader().getResource("Main/fxml/CreateDevice.fxml"));
        createDeviceStage = new Stage();
        createDeviceStage.setTitle("Create Device");
        createDeviceStage.setScene(new Scene(createDevice, 300, 125));
        createDeviceStage.setResizable(false);
        deviceName.setText("Enter device name here.");
        createDeviceStage.show();
    }

    public void displayRemove() throws Exception {
        Parent removeDevice = FXMLLoader.load(getClass().getClassLoader().getResource("Main/fxml/DeleteDevice.fxml"));
        removeDeviceStage = new Stage();
        removeDeviceStage.setTitle("Delete Device");
        removeDeviceStage.setScene(new Scene(removeDevice, 300, 150));
        removeDeviceStage.setResizable(false);
        List<DeviceEntity> deviceEntities = deviceController.getDeviceList();
        List<String> deviceNames = new ArrayList<String>();
        for(DeviceEntity deviceEntity : deviceEntities) {
            deviceNames.add(deviceEntity.getDevice());
        }
        Collections.sort(deviceNames);
        ObservableList observableList = FXCollections.observableList(deviceNames);
        deleteDevice.setItems(observableList);
        removeDeviceStage.show();
    }

    public void buildDevice() {
        deviceController.addDevice(deviceName.getText());
        createDeviceStage.close();
    }

    public void removeDevice() {
        DeviceEntity deviceEntity = deviceController.getDevice(deleteDevice.getValue());
        deviceController.removeDevice(deviceEntity.getId());
        removeDeviceStage.close();
    }

}
