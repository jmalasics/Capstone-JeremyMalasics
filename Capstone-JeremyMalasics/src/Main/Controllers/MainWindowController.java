package Main.Controllers;

import DatabaseManipulation.DeviceDatabaseController;
import DatabaseManipulation.EmployeeDatabaseController;
import DatabaseManipulation.RFIDDatabaseController;
import Main.EventMonitors.WindowMonitor;
import Models.EmployeeRfid;
import Main.Main;
import Persistence.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import jfxtras.internal.scene.control.skin.agenda.AgendaWeekSkin;
import jfxtras.scene.control.agenda.Agenda;

import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by jmalasics on 11/10/2014.
 */
public class MainWindowController implements Initializable {

    //region Private Fields
    private DeviceDatabaseController deviceDatabaseController;
    private EmployeeDatabaseController employeeDatabaseController;
    private RFIDDatabaseController rfidDatabaseController;
    private WindowMonitor monitor;
    //endregion

    //region Stages
    private Stage createEmployeeStage;
    private Stage removeEmployeeStage;
    private Stage createDepartmentStage;
    private Stage removeDepartmentStage;
    private Stage createDeviceStage;
    private Stage removeDeviceStage;
    private Stage createRFIDStage;
    private Stage removeRFIDStage;
    //endregion

    //region Employee Tab Fields
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
    private ComboBox<String> RFID;
    @FXML
    private TableView empHistory;
    @FXML
    private TableColumn empID;
    @FXML
    private TableColumn rfidCode;
    @FXML
    private TableColumn device;
    @FXML
    private TableColumn loggedTime;
    @FXML
    private Text employeeUpdated;
    //endregion

    //region Tree Views
    @FXML
    private TreeView<String> empList;
    @FXML
    private TreeView<String> deviceList;
    //endregion

    //region Calendar Tab
    @FXML
    private AnchorPane agendaAnchor;
    private Agenda agenda;
    private AgendaWeekSkin skin;
    //endregion

    //region Device Tab Fields
    @FXML
    private Text deviceID;
    @FXML
    private Text deviceName;
    @FXML
    private TableView<DeviceactivationtimesEntity> activationTimes;
    @FXML
    private TableColumn startTime;
    @FXML
    private TableColumn endTime;
    //endregion

    //region RFID Tab Fields
    @FXML
    private TableView<EmployeeRfid> rfidTable;
    @FXML
    private TableColumn rfidColumn;
    @FXML
    private TableColumn employeeColumn;
    @FXML
    private TableColumn firstNameColumn;
    @FXML
    private TableColumn lastNameColumn;
    //endregion

    //region Constructors
    public MainWindowController() {
        deviceDatabaseController = new DeviceDatabaseController(Main.PERSISTENCE_UNIT);
        employeeDatabaseController = new EmployeeDatabaseController(Main.PERSISTENCE_UNIT);
        rfidDatabaseController = new RFIDDatabaseController(Main.PERSISTENCE_UNIT);
    }
    //endregion

    //region Initialization
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TreeItem<String> rootItem = new TreeItem<String>("Employees: ");
        empList.setRoot(rootItem);
        displayEmployeeList();
        buildAgenda();
        displayAgenda();
        rootItem = new TreeItem<String>("Devices: ");
        deviceList.setRoot(rootItem);
        displayDeviceList();
        buildDepartmentComboBox();
        buildRFIDComboBox();
        empID.setCellValueFactory(new PropertyValueFactory<RfidusagehistoryEntity, String>("employeeId"));
        rfidCode.setCellValueFactory(new PropertyValueFactory<RfidusagehistoryEntity, String>("rfidCode"));
        device.setCellValueFactory(new PropertyValueFactory<RfidusagehistoryEntity, String>("device"));
        loggedTime.setCellValueFactory(new PropertyValueFactory<RfidusagehistoryEntity, String>("timeUsed"));
        rfidColumn.setCellValueFactory(new PropertyValueFactory<EmployeeRfid, String>("rfidCode"));
        employeeColumn.setCellValueFactory(new PropertyValueFactory<EmployeeRfid, String>("employeeId"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<EmployeeRfid, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<EmployeeRfid, String>("lastName"));
        startTime.setCellValueFactory(new PropertyValueFactory<DeviceactivationtimesEntity, String>("activationTime"));
        endTime.setCellValueFactory(new PropertyValueFactory<DeviceactivationtimesEntity, String>("disableTime"));
        displayRFIDTable();
        monitor = WindowMonitor.getInstance();
        monitor.setController(this);
    }

    public void displayEmployeeList() {
        List<DepartmentEntity> departmentEntities = employeeDatabaseController.getDepartments();
        for(DepartmentEntity departmentEntity : departmentEntities) {
            List<EmployeeEntity> employeeEntities = employeeDatabaseController.getEmployeesByDepartment(departmentEntity.getDepartment());
            TreeItem<String> treeItem = new TreeItem<String>(departmentEntity.getDepartment());
            for(EmployeeEntity employeeEntity : employeeEntities) {
                TreeItem<String> treeChild = new TreeItem<String>(employeeEntity.getLastName() + ", " + employeeEntity.getFirstName());
                treeItem.getChildren().add(treeChild);
            }
            empList.getRoot().getChildren().add(treeItem);
        }
        empList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) {
                TreeItem<String> selectedItem = newValue;
                if(selectedItem != null && selectedItem.getValue() != null && selectedItem.getValue().contains(",")) {
                    String[] splitString = selectedItem.getValue().split(", ");
                    displayEmployeeInformation(employeeDatabaseController.getEmployeeByFirstAndLast(splitString[1], splitString[0]));
                } else {
                    clearEmployeeInformation();
                }
            }
        });
    }

    public void displayDeviceList() {
        List<DeviceEntity> deviceEntities = deviceDatabaseController.getDeviceList();
        for(DeviceEntity deviceEntity : deviceEntities) {
            TreeItem<String> treeItem = new TreeItem<String>(deviceEntity.getDevice());
            deviceList.getRoot().getChildren().add(treeItem);
        }
        deviceList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) {
                TreeItem<String> selectedItem = newValue;
                if(selectedItem != null && selectedItem.getValue() != null && !selectedItem.getValue().contains(":")) {
                    displayDeviceInformation(deviceDatabaseController.getDevice(newValue.getValue()));
                } else {
                    clearDeviceInformation();
                }
            }
        });
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
        updateDepartmentComboBox();
    }

    private void buildRFIDComboBox() {
        RFID.setCellFactory((comboBox) -> {
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
        RFID.setConverter(new StringConverter<String>() {
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
        RFID.setOnAction((event) -> {
            String selectedString = RFID.getSelectionModel().getSelectedItem();
            RFID.setValue(selectedString);
        });
        List<RfidcardEntity> rfidcardEntityList = rfidDatabaseController.getAllRFID();
        List<RfidcardEntity> freeRfidCards = new ArrayList<RfidcardEntity>();
        for(RfidcardEntity rfidcardEntity : rfidcardEntityList) {
            if(!rfidDatabaseController.isRFIDTaken(rfidcardEntity.getRfidCode())) {
                freeRfidCards.add(rfidcardEntity);
            }
        }

        List<String> rfidCardStrings = new ArrayList<String>();
        for(RfidcardEntity rfidcardEntity : freeRfidCards) {
            rfidCardStrings.add(rfidcardEntity.getRfidCode());
        }
        ObservableList<String> observableList = FXCollections.observableList(rfidCardStrings);
        RFID.getItems().add("");
        RFID.getItems().addAll(observableList);
        RFID.setValue("");
        RFID.getSelectionModel().select("");
    }
    //endregion

    //region External Window Commands
    public void createEmployee() throws Exception {
        Parent createEmployee = FXMLLoader.load(getClass().getClassLoader().getResource("Main/fxml/CreateEmployee.fxml"));
        createEmployeeStage = new Stage();
        createEmployeeStage.setTitle("Create Employee");
        createEmployeeStage.setScene(new Scene(createEmployee, 500, 200));
        createEmployeeStage.setResizable(false);
        createEmployeeStage.show();
    }

    public void closeCreateEmployee() {
        createEmployeeStage.close();
        updateEmployeeList();
    }

    public void removeEmployee() throws Exception {
        Parent removeEmployee = FXMLLoader.load(getClass().getClassLoader().getResource("Main/fxml/DeleteEmployee.fxml"));
        removeEmployeeStage = new Stage();
        removeEmployeeStage.setTitle("Delete Employee");
        removeEmployeeStage.setScene(new Scene(removeEmployee, 300, 150));
        removeEmployeeStage.setResizable(false);
        removeEmployeeStage.show();
    }

    public void closeRemoveEmployee() {
        removeEmployeeStage.close();
        updateEmployeeList();
    }

    public void createDevice() throws Exception {
        Parent createDevice = FXMLLoader.load(getClass().getClassLoader().getResource("Main/fxml/CreateDevice.fxml"));
        createDeviceStage = new Stage();
        createDeviceStage.setTitle("Create Device");
        createDeviceStage.setScene(new Scene(createDevice, 300, 125));
        createDeviceStage.setResizable(false);
        createDeviceStage.show();
    }

    public void closeCreateDevice() {
        createDeviceStage.close();
        updateDeviceList();
    }

    public void removeDevice() throws Exception {
        Parent removeDevice = FXMLLoader.load(getClass().getClassLoader().getResource("Main/fxml/DeleteDevice.fxml"));
        removeDeviceStage = new Stage();
        removeDeviceStage.setTitle("Delete Device");
        removeDeviceStage.setScene(new Scene(removeDevice, 300, 150));
        removeDeviceStage.setResizable(false);
        removeDeviceStage.show();
    }

    public void closeRemoveDevice() {
        removeDeviceStage.close();
        updateDeviceList();
    }

    public void createRFID() throws Exception {
        Parent createRFID = FXMLLoader.load(getClass().getClassLoader().getResource("Main/fxml/CreateRFID.fxml"));
        createRFIDStage = new Stage();
        createRFIDStage.setTitle("Create RFID");
        createRFIDStage.setScene(new Scene(createRFID, 300, 150));
        createRFIDStage.setResizable(false);
        createRFIDStage.show();
    }

    public void closeCreateRFID(String rfidCode) {
        createRFIDStage.close();
        updateRFID(rfidCode);
    }

    public void removeRFID() throws Exception {
        Parent removeRFID = FXMLLoader.load(getClass().getClassLoader().getResource("Main/fxml/DeleteRFID.fxml"));
        removeRFIDStage = new Stage();
        removeRFIDStage.setTitle("Delete RFID");
        removeRFIDStage.setScene(new Scene(removeRFID, 300, 150));
        removeRFIDStage.setResizable(false);
        removeRFIDStage.show();
    }

    public void closeRemoveRFID(String rfidCode) {
        removeRFIDStage.close();
        updateRFID(rfidCode);
    }

    public void createDepartment() throws Exception {
        Parent createDepartment = FXMLLoader.load(getClass().getClassLoader().getResource("Main/fxml/CreateDepartment.fxml"));
        createDepartmentStage = new Stage();
        createDepartmentStage.setTitle("Create Department");
        createDepartmentStage.setScene(new Scene(createDepartment, 300, 150));
        createDepartmentStage.setResizable(false);
        createDepartmentStage.show();
    }

    public void closeCreateDepartment() {
        createDepartmentStage.close();
        updateDepartments();
    }

    public void removeDepartment() throws Exception {
        Parent removeDepartment = FXMLLoader.load(getClass().getClassLoader().getResource("Main/fxml/DeleteDepartment.fxml"));
        removeDepartmentStage = new Stage();
        removeDepartmentStage.setTitle("Remove Department");
        removeDepartmentStage.setScene(new Scene(removeDepartment, 300, 150));
        removeDepartmentStage.setResizable(false);
        removeDepartmentStage.show();
    }

    public void closeRemoveDepartment() {
        removeDepartmentStage.close();
        updateDepartments();
    }
    //endregion

    //region Employee Tab Commands
    public void displayEmployeeInformation(EmployeeEntity employeeEntity) {
        employeeID.setText("" + employeeEntity.getId());
        employeeID.setVisible(true);
        firstName.setText(employeeEntity.getFirstName());
        lastName.setText(employeeEntity.getLastName());
        LocalDate localDate = employeeEntity.getDateHired().toLocalDate();
        this.dateHired.setValue(localDate);
        department.getSelectionModel().select(employeeEntity.getDepartment());
        department.setValue(employeeEntity.getDepartment());
        jobTitle.setText(employeeEntity.getJobTitle());
        EmployeerfidcardEntity employeerfidcardEntity = employeeDatabaseController.getEmployeeRFIDCard(employeeEntity.getId());
        RfidcardEntity currentRFID;
        if(employeerfidcardEntity.getRfid() != null) {
            currentRFID = rfidDatabaseController.getRFIDCard(employeerfidcardEntity.getRfid().toCharArray());
            RFID.getSelectionModel().select(currentRFID.getRfidCode());
            RFID.setValue(currentRFID.getRfidCode());
        } else {
            RFID.getSelectionModel().select(null);
            RFID.setValue(null);
        }
        displayEmployeeHistory(employeeEntity);
    }

    public void modifyEmployee() {
        try {
            EmployeeEntity employeeEntity = new EmployeeEntity();
            employeeEntity.setId(Integer.parseInt(employeeID.getText()));
            employeeEntity.setFirstName(firstName.getText());
            employeeEntity.setLastName(lastName.getText());
            Instant instant = dateHired.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            java.util.Date date = Date.from(instant);
            employeeEntity.setDateHired(new java.sql.Date(date.getTime()));
            employeeEntity.setDepartment(department.getValue());
            employeeEntity.setJobTitle(jobTitle.getText());
            if(RFID.getValue() != null && RFID.getValue().length() == 10) {
                RfidcardEntity rfidcardEntity = rfidDatabaseController.getRFIDCard(RFID.getValue().toCharArray());
                rfidDatabaseController.addRFIDCard(rfidcardEntity.getRfidCode().toCharArray(), employeeEntity.getId());
            } else {
                EmployeerfidcardEntity employeerfidcardEntity = employeeDatabaseController.getEmployeeRFIDCard(Integer.parseInt(employeeID.getText()));
                rfidDatabaseController.removeRFIDFromEmployee(Integer.parseInt(employeeID.getText()));
                updateRFID(employeerfidcardEntity.getRfid());
            }
            employeeDatabaseController.modifyEmployee(employeeEntity);
            updateEmployeeList();
            buildRFIDComboBox();
            clearEmployeeInformation();
        } catch(NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void clearEmployeeInformation() {
        employeeID.setVisible(false);
        firstName.setText("");
        lastName.setText("");
        dateHired.setValue(null);
        department.setValue(null);
        department.valueProperty().setValue(null);
        jobTitle.setText(null);
        RFID.setValue(null);
        RFID.valueProperty().setValue(null);
        empHistory.getItems().clear();
    }

    private void updateEmployeeList() {
        empList.getRoot().getChildren().clear();
        displayEmployeeList();
    }

    private void displayEmployeeHistory(EmployeeEntity employeeEntity) {
        empHistory.getItems().clear();
        ObservableList<RfidusagehistoryEntity> observableList = FXCollections.observableList(employeeDatabaseController.getEmployeeHistory(employeeEntity.getId()));
        empHistory.getItems().addAll(observableList);
    }

    private void updateDepartments() {
        updateEmployeeList();
        updateDepartmentComboBox();
    }

    private void updateDepartmentComboBox() {
        List<DepartmentEntity> departmentEntities = employeeDatabaseController.getDepartments();
        List<String> departmentStrings = new ArrayList<String>();
        for(DepartmentEntity departmentEntity : departmentEntities) {
            departmentStrings.add(departmentEntity.getDepartment());
        }
        ObservableList<String> observableList = FXCollections.observableList(departmentStrings);
        department.setItems(observableList);
    }
    //endregion

    //region Device Tab Commands
    public void displayDeviceInformation(DeviceEntity deviceEntity) {
        deviceID.setText("" + deviceEntity.getId());
        deviceID.setVisible(true);
        deviceName.setText(deviceEntity.getDevice());
        deviceName.setVisible(true);
        displayActivationTimes(deviceEntity);
    }

    /**
    private int getTotalUsage(DeviceEntity deviceEntity) {
        List<DeviceusagehistoryEntity> deviceusagehistoryEntities = deviceDatabaseController.getDeviceUsageHistory(deviceEntity);
        int totalUsage = 0;
        for(DeviceusagehistoryEntity deviceusagehistoryEntity : deviceusagehistoryEntities) {
            totalUsage += deviceusagehistoryEntity.getRecentUsage();
        }
        return totalUsage;
    }
     */

    private void displayActivationTimes(DeviceEntity deviceEntity) {
        activationTimes.getItems().clear();
        List<DeviceactivationtimesEntity> deviceactivationtimesEntities = deviceDatabaseController.getDeviceActivationTimes(deviceEntity);
        ObservableList<DeviceactivationtimesEntity> observableList = FXCollections.observableList(deviceactivationtimesEntities);
        activationTimes.getItems().addAll(observableList);
    }

    private void clearDeviceInformation() {
        deviceID.setText(null);
        deviceID.setVisible(false);
        deviceName.setText(null);
        deviceName.setVisible(false);
        activationTimes.getItems().clear();
    }

    private void updateDeviceList() {
        deviceList.getRoot().getChildren().clear();
        displayDeviceList();
    }
    //endregion

    //region Calendar Tab Commands
    private void buildAgenda() {
        agenda = new Agenda();
        agenda.setPrefSize(810, 580);
        agenda.setMaxSize(810, 580);
        skin = new AgendaWeekSkin(agenda);
        agenda.setSkin(skin);
        agendaAnchor.getChildren().add(agenda);
    }

    public void displayAgenda() {
        final Map<String, Agenda.AppointmentGroup> appointmentGroupMap = new HashMap<String, Agenda.AppointmentGroup>();
        appointmentGroupMap.put("group0", new Agenda.AppointmentGroupImpl().withStyleClass("group0"));
        appointmentGroupMap.put("group1", new Agenda.AppointmentGroupImpl().withStyleClass("group1"));
        appointmentGroupMap.put("group2", new Agenda.AppointmentGroupImpl().withStyleClass("group2"));
        appointmentGroupMap.put("group3", new Agenda.AppointmentGroupImpl().withStyleClass("group3"));
        appointmentGroupMap.put("group4", new Agenda.AppointmentGroupImpl().withStyleClass("group4"));
        appointmentGroupMap.put("group5", new Agenda.AppointmentGroupImpl().withStyleClass("group5"));
        appointmentGroupMap.put("group6", new Agenda.AppointmentGroupImpl().withStyleClass("group6"));
        appointmentGroupMap.put("group7", new Agenda.AppointmentGroupImpl().withStyleClass("group7"));
        appointmentGroupMap.put("group8", new Agenda.AppointmentGroupImpl().withStyleClass("group8"));
        appointmentGroupMap.put("group9", new Agenda.AppointmentGroupImpl().withStyleClass("group9"));
        appointmentGroupMap.put("group10", new Agenda.AppointmentGroupImpl().withStyleClass("group10"));
        appointmentGroupMap.put("group11", new Agenda.AppointmentGroupImpl().withStyleClass("group11"));
        appointmentGroupMap.put("group12", new Agenda.AppointmentGroupImpl().withStyleClass("group12"));
        appointmentGroupMap.put("group13", new Agenda.AppointmentGroupImpl().withStyleClass("group13"));
        appointmentGroupMap.put("group14", new Agenda.AppointmentGroupImpl().withStyleClass("group14"));
        appointmentGroupMap.put("group15", new Agenda.AppointmentGroupImpl().withStyleClass("group15"));
        appointmentGroupMap.put("group16", new Agenda.AppointmentGroupImpl().withStyleClass("group16"));
        appointmentGroupMap.put("group17", new Agenda.AppointmentGroupImpl().withStyleClass("group17"));
        appointmentGroupMap.put("group18", new Agenda.AppointmentGroupImpl().withStyleClass("group18"));
        appointmentGroupMap.put("group19", new Agenda.AppointmentGroupImpl().withStyleClass("group19"));
        appointmentGroupMap.put("group0", new Agenda.AppointmentGroupImpl().withStyleClass("group20"));
        appointmentGroupMap.put("group1", new Agenda.AppointmentGroupImpl().withStyleClass("group21"));
        appointmentGroupMap.put("group2", new Agenda.AppointmentGroupImpl().withStyleClass("group22"));
        appointmentGroupMap.put("group3", new Agenda.AppointmentGroupImpl().withStyleClass("group23"));
        for (String lId : appointmentGroupMap.keySet())
        {
            Agenda.AppointmentGroup lAppointmentGroup = appointmentGroupMap.get(lId);
            lAppointmentGroup.setDescription(lId);
            agenda.appointmentGroups().add(lAppointmentGroup);
        }

        // accept new appointments
        agenda.createAppointmentCallbackProperty().set(new Callback<Agenda.CalendarRange, Agenda.Appointment>()
        {
            public Agenda.Appointment call(Agenda.CalendarRange calendarRange)
            {
                return new Agenda.AppointmentImpl()
                        .withStartTime(calendarRange.getStartCalendar())
                        .withEndTime(calendarRange.getEndCalendar())
                        .withSummary("new")
                        .withDescription("new")
                        .withAppointmentGroup(appointmentGroupMap.get("group1"));
            }
        });

        // initial set
        Calendar lFirstDayOfWeekCalendar = getFirstDayOfWeekCalendar(agenda.getLocale(), agenda.getDisplayedCalendar());
        int lYear = lFirstDayOfWeekCalendar.get(Calendar.YEAR);
        int lMonth = lFirstDayOfWeekCalendar.get(Calendar.MONTH);
        int lDay = lFirstDayOfWeekCalendar.get(Calendar.DATE);
        agenda.appointments().addAll(
                new Agenda.AppointmentImpl()
                        .withStartTime(new GregorianCalendar(lYear, lMonth, lDay, 8, 00))
                        .withEndTime(new GregorianCalendar(lYear, lMonth, lDay, 11, 30))
                        .withSummary("A")
                        .withDescription("A much longer test description")
                        .withAppointmentGroup(appointmentGroupMap.get("group7"))
                , 	new Agenda.AppointmentImpl()
                        .withStartTime(new GregorianCalendar(lYear, lMonth, lDay, 8, 30))
                        .withEndTime(new GregorianCalendar(lYear, lMonth, lDay, 10, 00))
                        .withSummary("B")
                        .withDescription("A description 2")
                        .withAppointmentGroup(appointmentGroupMap.get("group8"))
                , 	new Agenda.AppointmentImpl()
                        .withStartTime(new GregorianCalendar(lYear, lMonth, lDay, 8, 30))
                        .withEndTime(new GregorianCalendar(lYear, lMonth, lDay, 9, 30))
                        .withSummary("C")
                        .withDescription("A description 3")
                        .withAppointmentGroup(appointmentGroupMap.get("group9"))
                , 	new Agenda.AppointmentImpl()
                        .withStartTime(new GregorianCalendar(lYear, lMonth, lDay, 9, 00))
                        .withEndTime(new GregorianCalendar(lYear, lMonth, lDay, 13, 30))
                        .withSummary("D")
                        .withDescription("A description 4")
                        .withAppointmentGroup(appointmentGroupMap.get("group7"))
                , 	new Agenda.AppointmentImpl()
                        .withStartTime(new GregorianCalendar(lYear, lMonth, lDay, 10, 30))
                        .withEndTime(new GregorianCalendar(lYear, lMonth, lDay, 11, 00))
                        .withSummary("E")
                        .withDescription("A description 4")
                        .withAppointmentGroup(appointmentGroupMap.get("group7"))
                , 	new Agenda.AppointmentImpl()
                        .withStartTime(new GregorianCalendar(lYear, lMonth, lDay, 12, 30))
                        .withEndTime(new GregorianCalendar(lYear, lMonth, lDay, 13, 30))
                        .withSummary("F")
                        .withDescription("A description 4")
                        .withAppointmentGroup(appointmentGroupMap.get("group7"))
                , 	new Agenda.AppointmentImpl()
                        .withStartTime(new GregorianCalendar(lYear, lMonth, lDay, 13, 00))
                        .withEndTime(new GregorianCalendar(lYear, lMonth, lDay, 13, 30))
                        .withSummary("H")
                        .withDescription("A description 4")
                        .withAppointmentGroup(appointmentGroupMap.get("group7"))
                , 	new Agenda.AppointmentImpl()
                        .withStartTime(new GregorianCalendar(lYear, lMonth, lDay, 14, 00))
                        .withEndTime(new GregorianCalendar(lYear, lMonth, lDay, 14, 45))
                        .withSummary("G")
                        .withDescription("A description 4")
                        .withAppointmentGroup(appointmentGroupMap.get("group7"))
                , 	new Agenda.AppointmentImpl()
                        .withStartTime(new GregorianCalendar(lYear, lMonth, lDay, 15, 00))
                        .withEndTime(new GregorianCalendar(lYear, lMonth, lDay, 16, 00))
                        .withSummary("I")
                        .withDescription("A description 4")
                        .withAppointmentGroup(appointmentGroupMap.get("group7"))
                , 	new Agenda.AppointmentImpl()
                        .withStartTime(new GregorianCalendar(lYear, lMonth, lDay, 15, 30))
                        .withEndTime(new GregorianCalendar(lYear, lMonth, lDay, 16, 00))
                        .withSummary("J")
                        .withDescription("A description 4")
                        .withAppointmentGroup(appointmentGroupMap.get("group7"))
                // -----
                , 	new Agenda.AppointmentImpl()
                        .withStartTime(new GregorianCalendar(lYear, lMonth, lDay))
                        .withSummary("all day1")
                        .withDescription("A description")
                        .withAppointmentGroup(appointmentGroupMap.get("group7"))
                        .withWholeDay(true)
                , 	new Agenda.AppointmentImpl()
                        .withStartTime(new GregorianCalendar(lYear, lMonth, lDay))
                        .withSummary("all day2")
                        .withDescription("A description")
                        .withAppointmentGroup(appointmentGroupMap.get("group8"))
                        .withWholeDay(true)
                , 	new Agenda.AppointmentImpl()
                        .withStartTime(new GregorianCalendar(lYear, lMonth, lDay))
                        .withSummary("all day3")
                        .withDescription("A description3")
                        .withAppointmentGroup(appointmentGroupMap.get("group9"))
                        .withWholeDay(true)
                , 	new Agenda.AppointmentImpl()
                        .withStartTime(new GregorianCalendar(lYear, lMonth, lDay + 1))
                        .withSummary("all day")
                        .withDescription("A description3")
                        .withAppointmentGroup(appointmentGroupMap.get("group3"))
                        .withWholeDay(true)
        );
        final String Ipsum = "Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo. Quisque sit amet est et sapien ullamcorper pharetra. Vestibulum erat wisi, condimentum sed, commodo vitae, ornare sit amet, wisi. Aenean fermentum, elit eget tincidunt condimentum, eros ipsum rutrum orci, sagittis tempus lacus enim ac dui. Donec non enim in turpis pulvinar facilisis. Ut felis. Praesent dapibus, neque id cursus faucibus, tortor neque egestas augue, eu vulputate magna eros eu erat. Aliquam erat volutpat. Nam dui mi, tincidunt quis, accumsan porttitor, facilisis luctus, metus";
        // day spanner
        {
            Calendar lStart = (Calendar)lFirstDayOfWeekCalendar.clone();
            lStart.add(Calendar.SECOND, 5);
            lStart.add(Calendar.DATE, 1);
            Calendar lEnd = (Calendar)lStart.clone();
            lEnd.add(Calendar.DATE, 2);

            Agenda.Appointment lAppointment = new Agenda.AppointmentImpl()
                    .withStartTime(lStart)
                    .withEndTime(lEnd)
                    .withSummary(Ipsum.substring(0, new Random().nextInt(50)))
                    .withDescription(Ipsum.substring(0, 10 + new Random().nextInt(Ipsum.length() - 10)))
                    .withAppointmentGroup(appointmentGroupMap.get("group" + (new Random().nextInt(3) + 1)));

            agenda.appointments().add(lAppointment);
        }




            final AtomicBoolean skippedFirstRangeChange = new AtomicBoolean(false);
            agenda.calendarRangeCallbackProperty().set(new Callback<Agenda.CalendarRange, Void>() {
                @Override
                public Void call(Agenda.CalendarRange param) {
                    if(skippedFirstRangeChange.get() == false) {
                        skippedFirstRangeChange.set(true);
                        return null;
                    }
                    for(int i = 0; i < 20; i++) {
                        Calendar firstDayOfWeekCalendar = getFirstDayOfWeekCalendar(agenda.getLocale(), agenda.getDisplayedCalendar());

                        Calendar start = (Calendar) firstDayOfWeekCalendar.clone();
                        start.add(Calendar.DATE, new Random().nextInt(7));
                        start.add(Calendar.HOUR_OF_DAY, new Random().nextInt(24));
                        start.add(Calendar.MINUTE, new Random().nextInt(60));

                        Calendar end = (Calendar)start.clone();
                        end.add(Calendar.MINUTE, 15 + new Random().nextInt(24*60));

                        Agenda.Appointment appointment1 = new Agenda.AppointmentImpl()
                                    .withStartTime(start)
                                    .withEndTime(end)
                                    .withWholeDay(new Random().nextInt(50) > 40)
                                    .withSummary(Ipsum.substring(0, new Random().nextInt(50)))
                                    .withDescription(Ipsum.substring(0, new Random().nextInt(Ipsum.length())))
                                    .withAppointmentGroup(appointmentGroupMap.get("group" + (new Random().nextInt(24))));
                        agenda.appointments().add(appointment1);
                    }
                    return null;
                }
            });
    }

    private static Calendar getFirstDayOfWeekCalendar(Locale locale, Calendar c) {
        int firstDayOfWeek = Calendar.getInstance(locale).getFirstDayOfWeek();
        int currentDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int delta = 0;
        if(firstDayOfWeek <= currentDayOfWeek) {
            delta = -currentDayOfWeek + firstDayOfWeek;
        } else {
            delta = - currentDayOfWeek - (7 - firstDayOfWeek);
        }
        c = ((Calendar)c.clone());
        c.add(Calendar.DATE, delta);
        return c;
    }

    public void addAppointment(Agenda.Appointment appointment) {
        Timestamp startDate = new Timestamp(appointment.getStartTime().getTime().getTime());
        Timestamp endDate = new Timestamp(appointment.getEndTime().getTime().getTime());
        DeviceactivationtimesEntity deviceactivationtimesEntity = new DeviceactivationtimesEntity();
        deviceactivationtimesEntity.setActivationTime(startDate);
        deviceactivationtimesEntity.setDisableTime(endDate);
        deviceactivationtimesEntity.setSummary(appointment.getSummary());
        deviceactivationtimesEntity.setDescription(appointment.getDescription());
        deviceactivationtimesEntity.setGroupName(appointment.getAppointmentGroup().getDescription());

    }
    //endregion

    //region RFID Tab Commands
    private void updateRFID(String rfidCode) {
        RFID.getItems().add(rfidCode);
        displayRFIDTable();
    }

    private void displayRFIDTable() {
        rfidTable.getItems().clear();
        List<EmployeerfidcardEntity> employeerfidcardEntities = rfidDatabaseController.getRFIDList();
        List<EmployeeRfid> employeeRfids = new ArrayList<EmployeeRfid>();
        for(EmployeerfidcardEntity employeerfidcardEntity : employeerfidcardEntities) {
            EmployeeEntity employeeEntity = null;
            if(employeerfidcardEntity.getEmpId() != null) {
                employeeEntity = employeeDatabaseController.getEmployeeById(employeerfidcardEntity.getEmpId());
            } else {
                employeeEntity = new EmployeeEntity();
                employeeEntity.setFirstName("");
                employeeEntity.setLastName("");
            }
            EmployeeRfid employeeRfid = null;
            if(employeerfidcardEntity.getEmpId() != null) {
                employeeRfid = new EmployeeRfid("" + employeerfidcardEntity.getEmpId(), employeerfidcardEntity.getRfid(), employeeEntity.getFirstName(), employeeEntity.getLastName());
            } else {
                employeeRfid = new EmployeeRfid("", employeerfidcardEntity.getRfid(), employeeEntity.getFirstName(), employeeEntity.getLastName());
            }
            employeeRfids.add(employeeRfid);
        }
        rfidTable.getItems().addAll(employeeRfids);
    }
    //endregion

}
