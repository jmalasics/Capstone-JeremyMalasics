package Main;

import DatabaseManipulation.DeviceDatabaseController;
import DatabaseManipulation.EmployeeDatabaseController;
import DatabaseManipulation.RFIDDatabaseController;
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
import jfxtras.scene.control.agenda.Agenda;

import java.net.URL;
import java.sql.Date;
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
    private DeviceController deviceController;
    private EmployeeController employeeController;
    private RFIDController rfidController;
    //endregion

    //region Static Fields
    private static Stage createEmployeeStage;
    private static Stage removeEmployeeStage;
    private static Stage createDeviceStage;
    private static Stage removeDeviceStage;
    private static Stage createRFIDStage;
    private static Stage removeRFIDStage;
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
    //endregion

    //region Tree Views
    @FXML
    private TreeView<String> empList;
    @FXML
    private TreeView<String> deviceList;
    //endregion

    //region Calendar Tab
    @FXML
    private Agenda agenda;
    //endregion

    //region Device Tab Fields
    @FXML
    private Text deviceID;
    @FXML
    private Text deviceName;
    @FXML
    private Text totalUsage;
    @FXML
    private TableView<DeviceactivationtimesEntity> activationTimes;
    @FXML
    private TableColumn startTime;
    @FXML
    private TableColumn endTime;
    //endregion

    //region Constructors
    public MainWindowController() {
        deviceDatabaseController = new DeviceDatabaseController(Main.PERSISTENCE_UNIT);
        employeeDatabaseController = new EmployeeDatabaseController(Main.PERSISTENCE_UNIT);
        rfidDatabaseController = new RFIDDatabaseController(Main.PERSISTENCE_UNIT);
        deviceController = new DeviceController(this);
        employeeController = new EmployeeController(this);
        rfidController = new RFIDController();
    }
    //endregion

    //region Initialization
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TreeItem<String> rootItem = new TreeItem<String>("Employees: ");
        empList.setRoot(rootItem);
        displayEmployeeList();
        //displayAgenda();
        rootItem = new TreeItem<String>("Devices: ");
        deviceList.setRoot(rootItem);
        displayDeviceList();
        buildDepartmentComboBox();
        buildRFIDComboBox();
        startTime.setCellValueFactory(new PropertyValueFactory<DeviceactivationtimesEntity, String>("activationTime"));
        endTime.setCellValueFactory(new PropertyValueFactory<DeviceactivationtimesEntity, String>("disableTime"));
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
        List<DepartmentEntity> departmentEntities = employeeDatabaseController.getDepartments();
        List<String> departmentStrings = new ArrayList<String>();
        for(DepartmentEntity departmentEntity : departmentEntities) {
            departmentStrings.add(departmentEntity.getDepartment());
        }
        ObservableList<String> observableList = FXCollections.observableList(departmentStrings);
        department.setItems(observableList);
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
        RFID.setItems(observableList);
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

    public static void closeCreateRFID() {
        createRFIDStage.close();
    }

    public void removeRFID() throws Exception {
        Parent removeRFID = FXMLLoader.load(getClass().getClassLoader().getResource("Main/fxml/DeleteRFID.fxml"));
        removeRFIDStage = new Stage();
        removeRFIDStage.setTitle("Delete RFID");
        removeRFIDStage.setScene(new Scene(removeRFID, 300, 150));
        removeRFIDStage.setResizable(false);
        removeRFIDStage.show();
    }

    public static void closeRemoveRFID() {
        removeRFIDStage.close();
    }
    //endregion

    //region Employee Tab Commands
    public void displayEmployeeInformation(EmployeeEntity employeeEntity) {
        employeeID.setText("" + employeeEntity.getId());
        employeeID.setVisible(true);
        firstName.setText(employeeEntity.getFirstName());
        lastName.setText(employeeEntity.getLastName());
        Date date = employeeEntity.getDateHired();
        //Instant instant = date.toInstant();
        //LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate = LocalDate.now();
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
            if(RFID.getValue() != null) {
                RfidcardEntity rfidcardEntity = rfidDatabaseController.getRFIDCard(RFID.getValue().toCharArray());
                rfidDatabaseController.addRFIDCard(rfidcardEntity.getRfidCode().toCharArray(), employeeEntity.getId());
            }
            employeeDatabaseController.modifyEmployee(employeeEntity);
            updateEmployeeList();
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
    }

    private void updateEmployeeList() {
        empList.getRoot().getChildren().clear();
        displayEmployeeList();
    }
    //endregion

    //region Device Tab Commands
    public void displayDeviceInformation(DeviceEntity deviceEntity) {
        deviceID.setText("" + deviceEntity.getId());
        deviceID.setVisible(true);
        deviceName.setText(deviceEntity.getDevice());
        deviceName.setVisible(true);
        totalUsage.setText("" + getTotalUsage(deviceEntity));
        totalUsage.setVisible(true);
        displayActivationTimes(deviceEntity);
    }

    private int getTotalUsage(DeviceEntity deviceEntity) {
        List<DeviceusagehistoryEntity> deviceusagehistoryEntities = deviceDatabaseController.getDeviceUsageHistory(deviceEntity);
        int totalUsage = 0;
        for(DeviceusagehistoryEntity deviceusagehistoryEntity : deviceusagehistoryEntities) {
            totalUsage += deviceusagehistoryEntity.getRecentUsage();
        }
        return totalUsage;
    }

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
        totalUsage.setText(null);
        totalUsage.setVisible(false);
        activationTimes.getItems().clear();
    }

    private void updateDeviceList() {
        deviceList.getRoot().getChildren().clear();
        displayDeviceList();
    }
    //endregion

    //region Calendar Tab Commands
    public void displayAgenda() {
        final Map<String, Agenda.AppointmentGroup> appointmentGroupMap = new HashMap<String, Agenda.AppointmentGroup>();
        appointmentGroupMap.put("test1", new Agenda.AppointmentGroupImpl().withStyleClass("test1"));
        appointmentGroupMap.put("test2", new Agenda.AppointmentGroupImpl().withStyleClass("test2"));
        appointmentGroupMap.put("test3", new Agenda.AppointmentGroupImpl().withStyleClass("test3"));
        for(String id : appointmentGroupMap.keySet()) {
            Agenda.AppointmentGroup appointmentGroup = appointmentGroupMap.get(id);
            appointmentGroup.setDescription(id);
            agenda.appointmentGroups().add(appointmentGroup);
        }
        agenda.createAppointmentCallbackProperty().set(new Callback<Agenda.CalendarRange, Agenda.Appointment>() {
            @Override
            public Agenda.Appointment call(Agenda.CalendarRange param) {
                return new Agenda.AppointmentImpl()
                        .withStartTime(param.getStartCalendar())
                        .withEndTime(param.getEndCalendar())
                        .withSummary("new")
                        .withDescription("new")
                        .withAppointmentGroup(appointmentGroupMap.get("group1"));
            }
        });

        Calendar firstDayOfWeekCalendar = getFirstDayOfWeekCalendar(agenda.getLocale(), agenda.getDisplayedCalendar());
        int year = firstDayOfWeekCalendar.get(Calendar.YEAR);
        int month = firstDayOfWeekCalendar.get(Calendar.MONTH);
        int day = firstDayOfWeekCalendar.get(Calendar.DATE);
        agenda.appointments().addAll(
            new Agenda.AppointmentImpl()
                    .withStartTime(new GregorianCalendar(year, month, day, 8, 00))
                    .withEndTime(new GregorianCalendar(year, month, day, 11, 30))
                    .withSummary("A")
                    .withDescription("This is a test of the description.")
                    .withAppointmentGroup(appointmentGroupMap.get("test1")),
            new Agenda.AppointmentImpl()
                    .withStartTime(new GregorianCalendar(year, month, day, 8, 30))
                    .withEndTime(new GregorianCalendar(year, month, day, 10, 00))
                    .withSummary("B")
                    .withDescription("Description 2.")
                    .withAppointmentGroup(appointmentGroupMap.get("test2")),
            new Agenda.AppointmentImpl()
                    .withStartTime(new GregorianCalendar(year, month, day, 8, 30))
                    .withEndTime(new GregorianCalendar(year, month, day, 9, 30))
                    .withSummary("C")
                    .withDescription("Description 3.")
                    .withAppointmentGroup(appointmentGroupMap.get("test3")),
            new Agenda.AppointmentImpl()
                    .withStartTime(new GregorianCalendar(year, month, day, 9, 00))
                     .withEndTime(new GregorianCalendar(year, month, day, 13, 30))
                     .withSummary("D")
                     .withDescription("Description 4.")
                     .withAppointmentGroup(appointmentGroupMap.get("test1")),
            new Agenda.AppointmentImpl()
                     .withStartTime(new GregorianCalendar(year, month, day, 10, 30))
                     .withEndTime(new GregorianCalendar(year, month, day, 11, 00))
                     .withSummary("E")
                     .withDescription("Description 5.")
                     .withAppointmentGroup(appointmentGroupMap.get("test2"))
        );
        final String Ipsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                        "Duis vitae leo quis ante auctor feugiat. Quisque quis malesuada nisl. " +
                        "Proin eros leo, vestibulum et arcu sed, ultrices malesuada tortor. " +
                        "Nulla tellus lorem, volutpat id lorem non, laoreet blandit felis. " +
                        "Morbi ornare fringilla lacus, eget congue magna molestie sit amet. " +
                        "Maecenas auctor sed nibh ut blandit. Nulla ex nibh, blandit ut cursus sed, " +
                        "pellentesque ut turpis. Vestibulum eget gravida erat. Aenean sagittis, " +
                        "purus quis pellentesque efficitur, lacus tortor accumsan nulla, " +
                        "et tincidunt ex nibh nec nunc.";
        {
            Calendar start = (Calendar)firstDayOfWeekCalendar.clone();
            start.add(Calendar.SECOND, 5);
            start.add(Calendar.DATE, 1);
            Calendar end = (Calendar)start.clone();
            end.add(Calendar.DATE, 2);

            Agenda.Appointment appointment = new Agenda.AppointmentImpl()
                                                .withStartTime(start)
                                                .withEndTime(end)
                                                .withSummary(Ipsum.substring(0, new Random().nextInt(50)))
                                                .withDescription(Ipsum.substring(0, 10 + new Random().nextInt(Ipsum.length() - 10)))
                                                .withAppointmentGroup(appointmentGroupMap.get("group" + new Random().nextInt(3) + 1));
            agenda.appointments().add(appointment);

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
    //endregion

}
