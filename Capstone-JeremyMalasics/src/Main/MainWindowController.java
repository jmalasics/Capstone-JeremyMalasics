package Main;

import DatabaseManipulation.DeviceDatabaseController;
import DatabaseManipulation.EmployeeDatabaseController;
import DatabaseManipulation.RFIDDatabaseController;
import Persistence.DepartmentEntity;
import Persistence.EmployeeEntity;
import Persistence.EmployeerfidcardEntity;
import Persistence.RfidcardEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.scene.control.TextField;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmalasics on 11/10/2014.
 */
public class MainWindowController {

    private DeviceDatabaseController deviceDatabaseController;
    private EmployeeDatabaseController employeeDatabaseController;
    private RFIDDatabaseController rfidDatabaseController;
    private DeviceController deviceController;
    private EmployeeController employeeController;
    private RFIDController rfidController;

    @FXML
    private Text employeeID;
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
    private ComboBox<RfidcardEntity> RFID;
    @FXML
    private AnchorPane empListAnchor;
    @FXML
    private TreeView<String> empList;

    public MainWindowController() {
        deviceDatabaseController = new DeviceDatabaseController(Main.PERSISTENCE_UNIT);
        employeeDatabaseController = new EmployeeDatabaseController(Main.PERSISTENCE_UNIT);
        rfidDatabaseController = new RFIDDatabaseController(Main.PERSISTENCE_UNIT);
        deviceController = new DeviceController();
        employeeController = new EmployeeController();
        rfidController = new RFIDController();
        setUpFields();
    }

    public void setUpFields() {
        firstName = new TextField();
        lastName = new TextField();
        dateHired = new DatePicker();
        department = new ComboBox<String>();
        jobTitle = new TextField();
        empListAnchor = new AnchorPane();
        empList = new TreeView<String>();
        displayEmployeeList();
    }

    public void createEmployee() throws Exception {
        employeeController.displayCreate();
    }

    public void removeEmployee() throws Exception {
        employeeController.displayRemove();
    }

    public void createDevice() throws Exception {
        deviceController.displayCreate();
    }

    public void removeDevice() throws Exception {
        deviceController.displayRemove();
    }

    public void createRFID() throws Exception {
        rfidController.displayCreate();
    }

    public void removeRFID() throws Exception {
        rfidController.displayRemove();
    }

    public void displayEmployeeList() {
        TreeItem<String> rootItem = new TreeItem<String>("Employees: ");
        List<DepartmentEntity> departmentEntities = employeeDatabaseController.getDepartments();
        for(DepartmentEntity departmentEntity : departmentEntities) {
            List<EmployeeEntity> employeeEntities = employeeDatabaseController.getEmployeesByDepartment(departmentEntity.getDepartment());
            TreeItem<String> treeItem = new TreeItem<String>(departmentEntity.getDepartment());
            for(EmployeeEntity employeeEntity : employeeEntities) {
                TreeItem<String> treeChild = new TreeItem<String>(employeeEntity.getLastName() + ", " + employeeEntity.getFirstName());
                treeItem.getChildren().add(treeChild);
            }
            rootItem.getChildren().add(treeItem);
        }
        empList = new TreeView<String>(rootItem);
        empListAnchor.getChildren().add(empList);
    }

    public void displayEmployeeInformation(EmployeeEntity employeeEntity) {
        employeeID.setText("" +employeeEntity.getId());
        firstName.setText(employeeEntity.getFirstName());
        lastName.setText(employeeEntity.getLastName());
        Date dateHired = employeeEntity.getDateHired();
        Instant instant = dateHired.toInstant();
        LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        this.dateHired.setValue(date);
        List<DepartmentEntity> departmentEntities = employeeDatabaseController.getDepartments();
        ObservableList departmentObservables = FXCollections.observableList(departmentEntities);
        department.setItems(departmentObservables);
        department.setValue(employeeEntity.getDepartment());
        jobTitle.setText(employeeEntity.getJobTitle());
        EmployeerfidcardEntity employeerfidcardEntity = employeeDatabaseController.getEmployeeRFIDCard(employeeEntity.getId());
        List<RfidcardEntity> rfidcardEntityList = rfidDatabaseController.getAllRFID();
        List<RfidcardEntity> freeRfidCards = new ArrayList<RfidcardEntity>();
        for(int i = 0; i < rfidcardEntityList.size(); i++) {
            if(!rfidDatabaseController.isRFIDTaken(rfidcardEntityList.get(i).getRfidCode())) {
                freeRfidCards.add(rfidcardEntityList.get(i));
            }
        }
        RfidcardEntity currentRFID = rfidDatabaseController.getRFIDCard(employeerfidcardEntity.getRfid().toCharArray());
        freeRfidCards.add(currentRFID);
        ObservableList observableList = FXCollections.observableList(freeRfidCards);
        RFID.setItems(observableList);
        RFID.setValue(currentRFID);
    }

    public void modifyEmployee() {
        try {
            EmployeeEntity employeeEntity = new EmployeeEntity();
            employeeEntity.setId(Integer.parseInt(employeeID.getText()));
            employeeEntity.setFirstName(firstName.getText());
            employeeEntity.setLastName(lastName.getText());
            java.util.Date date = new java.util.Date(dateHired.getValue().toEpochDay());
            employeeEntity.setDateHired(new java.sql.Date(date.getTime()));
            employeeEntity.setDepartment(department.getValue());
            employeeEntity.setJobTitle(jobTitle.getText());
            RfidcardEntity rfidcardEntity = RFID.getValue();
            employeeDatabaseController.modifyEmployee(employeeEntity);
        } catch(NumberFormatException e) {
            e.printStackTrace();
        }
    }

}
