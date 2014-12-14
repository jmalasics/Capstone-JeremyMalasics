package Main.Controllers;

import DatabaseManipulation.EmployeeDatabaseController;
import DatabaseManipulation.RFIDDatabaseController;
import Main.EventMonitors.WindowMonitor;
import Main.Main;
import Persistence.DepartmentEntity;
import Persistence.EmployeeEntity;
import Persistence.EmployeerfidcardEntity;
import Persistence.RfidcardEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.Date;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by jmalasics on 11/5/2014.
 */
public class EmployeeController implements Initializable {

    private EmployeeDatabaseController employeeController;
    private RFIDDatabaseController rfidController;
    private WindowMonitor monitor;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private DatePicker dateHired;

    @FXML
    private ComboBox<String> department;

    @FXML
    private TextField jobTitle;

    @FXML
    private ComboBox<String> rfid;

    @FXML
    private ComboBox<String> deleteEmployee;

    public EmployeeController() {
        employeeController = new EmployeeDatabaseController(Main.PERSISTENCE_UNIT);
        rfidController = new RFIDDatabaseController(Main.PERSISTENCE_UNIT);
        monitor = WindowMonitor.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(department != null && rfid != null) {
            buildCreateEmployee();
        } else if(deleteEmployee != null) {
            buildDeleteEmployeeComboBox();
        }
    }

    private void buildCreateEmployee() {
        firstName.setPromptText("Enter first name here.");
        lastName.setPromptText("Enter last name here.");
        jobTitle.setPromptText("Enter job title here.");
        buildDepartmentComboBox();
        buildRFIDComboBox();
    }

    private void buildDepartmentComboBox() {
        department.setCellFactory((comboBox) -> {
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
        department.setConverter(new StringConverter<String>() {
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
        department.setOnAction((event) -> {
            String selectedString = department.getSelectionModel().getSelectedItem();
            department.setValue(selectedString);
        });
        List<DepartmentEntity> departmentEntities = employeeController.getDepartments();
        List<String> departmentStrings = new ArrayList<String>();
        for(DepartmentEntity departmentEntity : departmentEntities) {
            departmentStrings.add(departmentEntity.getDepartment());
        }
        ObservableList<String> observableList = FXCollections.observableList(departmentStrings);
        department.setItems(observableList);
        department.setPromptText("Choose department...");
    }

    private void buildRFIDComboBox() {
        rfid.setCellFactory((comboBox) -> {
            return new ListCell<String>() {
                @Override
                protected void updateItem(String s, boolean empty) {
                    super.updateItem(s, empty);
                    if(s == null || empty) {
                        setText(null);
                    } else {
                        setText(s);
                    }
                }
            };
        });
        rfid.setConverter(new StringConverter<String>() {
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
        rfid.setOnAction((event) -> {
            String selectedString = rfid.getSelectionModel().getSelectedItem();
            rfid.setValue(selectedString);
        });
        List<RfidcardEntity> rfidcardEntityList = rfidController.getAllRFID();
        List<RfidcardEntity> freeRfidCards = new ArrayList<RfidcardEntity>();
        for(RfidcardEntity rfidcardEntity : rfidcardEntityList) {
            if(!rfidController.isRFIDTaken(rfidcardEntity.getRfidCode())) {
                freeRfidCards.add(rfidcardEntity);
            }
        }

        List<String> rfidCardStrings = new ArrayList<String>();
        for(RfidcardEntity rfidcardEntity : freeRfidCards) {
            rfidCardStrings.add(rfidcardEntity.getRfidCode());
        }
        ObservableList<String> observableList = FXCollections.observableList(rfidCardStrings);
        rfid.setItems(observableList);
        rfid.setPromptText("Choose RFID...");
    }

    public void buildDeleteEmployeeComboBox() {
        deleteEmployee.setCellFactory((comboBox) -> {
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
        deleteEmployee.setConverter(new StringConverter<String>() {
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
        deleteEmployee.setOnAction((event) -> {
            String selectedString = deleteEmployee.getSelectionModel().getSelectedItem();
            deleteEmployee.setValue(selectedString);
        });
        List<EmployeeEntity> employeeEntities = employeeController.getEmployeeList();
        List<String> employeeStrings = new ArrayList<String>();
        for(EmployeeEntity employeeEntity : employeeEntities) {
            employeeStrings.add(employeeEntity.getLastName() + ", " + employeeEntity.getFirstName());
        }
        Collections.sort(employeeStrings);
        ObservableList observableList = FXCollections.observableList(employeeStrings);
        deleteEmployee.setItems(observableList);
        deleteEmployee.setPromptText("Choose employee...");
    }

    public void buildEmployee() {
        String first = firstName.getText();
        String last = lastName.getText();
        Instant instant = dateHired.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        java.util.Date date = Date.from(instant);
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        String department = this.department.getValue();
        String title = jobTitle.getText();
        String rfidCode = rfid.getValue();
        EmployeeEntity employee = employeeController.addEmployee(first, last, sqlDate, department, title);
        EmployeerfidcardEntity employeerfidcardEntity = null;
        if(rfidCode != null) {
            EmployeeEntity employeeEntity = employeeController.getEmployeeByFirstAndLast(first, last);
            rfidController.addRFIDCard(rfidCode.toCharArray());
            employeerfidcardEntity = rfidController.addRFIDCard(rfidCode.toCharArray(), employeeEntity.getId());
        }
        monitor.employeeCreateUpdate(employee, employeerfidcardEntity);
    }

    public void removeEmployee() {
        String[] employee = deleteEmployee.getValue().split(", ");
        EmployeeEntity employeeEntity = employeeController.getEmployeeByFirstAndLast(employee[1], employee[0]);
        if(employeeController.removeEmployee(employeeEntity.getId())) {
            monitor.employeeRemoveUpdate(employeeEntity);
        }
    }
}
