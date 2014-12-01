package Main.Controllers;

import DatabaseManipulation.EmployeeDatabaseController;
import Main.EventMonitors.WindowMonitor;
import Main.Main;
import Persistence.DepartmentEntity;
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
 * Created by jmalasics on 11/28/2014.
 */
public class DepartmentController implements Initializable {

    private EmployeeDatabaseController employeeController;
    private WindowMonitor monitor;

    @FXML
    private TextField department;
    @FXML
    private ComboBox<String> deleteDepartment;

    public DepartmentController() {
        employeeController = new EmployeeDatabaseController(Main.PERSISTENCE_UNIT);
        monitor = WindowMonitor.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(department != null) {
            buildCreateDepartment();
        } else if(deleteDepartment != null) {
            buildRemoveDepartment();
        }
    }

    public void buildCreateDepartment() {
        department.setPromptText("Enter department name here.");
    }

    public void buildRemoveDepartment() {
        deleteDepartment.setCellFactory((comboBox) -> {
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
        deleteDepartment.setConverter(new StringConverter<String>() {
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
        deleteDepartment.setOnAction((event) -> {
            String selectedString = deleteDepartment.getSelectionModel().getSelectedItem();
            deleteDepartment.setValue(selectedString);
        });
        List<DepartmentEntity> departmentEntities = employeeController.getDepartments();
        List<String> deviceNames = new ArrayList<String>();
        for(DepartmentEntity departmentEntity : departmentEntities) {
            deviceNames.add(departmentEntity.getDepartment());
        }
        Collections.sort(deviceNames);
        ObservableList observableList = FXCollections.observableList(deviceNames);
        deleteDepartment.setItems(observableList);
    }

    public void buildDepartment() {
        if(employeeController.addDepartment(department.getText())) {
            monitor.departmentCreateUpdate(department.getText());
        }
    }

    public void deleteDepartment() {
        if(employeeController.removeDepartment(deleteDepartment.getValue())) {
            monitor.departmentRemoveUpdate(deleteDepartment.getValue());
        }
    }

}
