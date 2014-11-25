package Main;

import DatabaseManipulation.EmployeeDatabaseController;
import DatabaseManipulation.RFIDDatabaseController;
import Persistence.DepartmentEntity;
import Persistence.EmployeeEntity;
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
    private MainWindowController mainController;

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

    public EmployeeController(MainWindowController mainController) {
        employeeController = new EmployeeDatabaseController(Main.PERSISTENCE_UNIT);
        rfidController = new RFIDDatabaseController(Main.PERSISTENCE_UNIT);
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(department != null) {
            buildDepartmentComboBox();
        }
        if(rfid != null) {
            buildRFIDComboBox();
        }
        if(deleteEmployee != null) {
            buildDeleteEmployeeComboBox();
        }
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
    }

    public void buildEmployee() {
        String first = firstName.getText();
        String last = lastName.getText();
        java.util.Date date = new java.util.Date(dateHired.getValue().toEpochDay());
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        String department = this.department.getValue();
        String title = jobTitle.getText();
        String rfidCode = rfid.getValue();
        employeeController.addEmployee(first, last, sqlDate, department, title);
        if(rfidCode != null) {
            EmployeeEntity employeeEntity = employeeController.getEmployeeByFirstAndLast(first, last);
            rfidController.addRFIDCard(rfidCode.toCharArray());
            rfidController.addRFIDCard(rfidCode.toCharArray(), employeeEntity.getId());
        }
        mainController.closeCreateEmployee();
    }

    public void removeEmployee() {
        String[] employee = deleteEmployee.getValue().split(", ");
        EmployeeEntity employeeEntity = employeeController.getEmployeeByFirstAndLast(employee[1], employee[0]);
        employeeController.removeEmployee(employeeEntity.getId());
        mainController.closeRemoveEmployee();
    }

}
