package Main;

import DatabaseManipulation.EmployeeDatabaseController;
import DatabaseManipulation.RFIDDatabaseController;
import Persistence.DepartmentEntity;
import Persistence.EmployeeEntity;
import Persistence.RfidcardEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jmalasics on 11/5/2014.
 */
public class EmployeeController {

    private EmployeeDatabaseController employeeController;
    private RFIDDatabaseController rfidController;

    private Stage createEmployeeStage;
    private Stage removeEmployeeStage;

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
        firstName = new TextField();
        lastName = new TextField();
        dateHired = new DatePicker();
        jobTitle = new TextField();
    }

    public void displayCreate() throws Exception {
        Parent createEmployee = FXMLLoader.load(getClass().getClassLoader().getResource("Main/fxml/CreateEmployee.fxml"));
        createEmployeeStage = new Stage();
        createEmployeeStage.setTitle("Create Employee");
        createEmployeeStage.setScene(new Scene(createEmployee, 500, 200));
        createEmployeeStage.setResizable(false);
        List<DepartmentEntity> departmentEntities = employeeController.getDepartments();
        List<RfidcardEntity> rfidcardEntityList = rfidController.getAllRFID();
        List<RfidcardEntity> freeRfidCards = new ArrayList<RfidcardEntity>();
        for(RfidcardEntity rfidcardEntity : rfidcardEntityList) {
            if(!rfidController.isRFIDTaken(rfidcardEntity.getRfidCode())) {
                freeRfidCards.add(rfidcardEntity);
            }
        }
        List<String> departmentStrings = new ArrayList<String>();
        for(DepartmentEntity departmentEntity : departmentEntities) {
            departmentStrings.add(departmentEntity.getDepartment());
        }
        List<String> rfidCardStrings = new ArrayList<String>();
        for(RfidcardEntity rfidcardEntity : freeRfidCards) {
            rfidCardStrings.add(rfidcardEntity.getRfidCode());
        }
        ObservableList<String> observableList = FXCollections.observableList(departmentStrings);
        department = new ComboBox<String>(observableList);
        observableList = FXCollections.observableList(rfidCardStrings);
        rfid = new ComboBox<String>(observableList);
        createEmployeeStage.show();
    }

    public void displayRemove() throws Exception {
        Parent removeEmployee = FXMLLoader.load(getClass().getClassLoader().getResource("Main/fxml/DeleteEmployee.fxml"));
        removeEmployeeStage = new Stage();
        removeEmployeeStage.setTitle("Delete Employee");
        removeEmployeeStage.setScene(new Scene(removeEmployee, 300, 150));
        removeEmployeeStage.setResizable(false);
        List<EmployeeEntity> employeeEntities = employeeController.getEmployeeList();
        List<String> employeeStrings = new ArrayList<String>();
        for(EmployeeEntity employeeEntity : employeeEntities) {
            employeeStrings.add(employeeEntity.getLastName() + ", " + employeeEntity.getFirstName());
        }
        Collections.sort(employeeStrings);
        ObservableList observableList = FXCollections.observableList(employeeStrings);
        deleteEmployee.setItems(observableList);
        removeEmployeeStage.show();
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
            rfidController.addRFIDCard(rfidCode.toCharArray(), employeeEntity.getId());
        }
        createEmployeeStage.close();
    }

    public void removeEmployee() {
        String[] employee = deleteEmployee.getValue().split(", ");
        EmployeeEntity employeeEntity = employeeController.getEmployeeByFirstAndLast(employee[1], employee[0]);
        employeeController.removeEmployee(employeeEntity.getId());
        removeEmployeeStage.close();
    }

}
