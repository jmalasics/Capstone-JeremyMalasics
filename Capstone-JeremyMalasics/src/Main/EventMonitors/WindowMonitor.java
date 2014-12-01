package Main.EventMonitors;

import ArduinoConnection.SerialClass;
import Main.Controllers.MainWindowController;
import Persistence.DeviceEntity;
import Persistence.EmployeeEntity;
import Persistence.EmployeerfidcardEntity;

/**
 * Created by jmalasics on 11/28/2014.
 */
public class WindowMonitor {

    private static WindowMonitor monitor;
    private MainWindowController mainWindowController;
    private SerialClass serial;

    private WindowMonitor() {

    }

    public static WindowMonitor getInstance() {
        if(monitor ==  null) {
            monitor = new WindowMonitor();
            monitor.setSerial();
        }
        return monitor;
    }

    public void setController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    private void setSerial() {
        serial = new SerialClass();
    }

    public void employeeCreateUpdate(EmployeeEntity employeeEntity, EmployeerfidcardEntity employeerfidcardEntity) {
        mainWindowController.closeCreateEmployee();
        String logMessage = "Employee created... ID: " + employeeEntity.getId() +
                " First: " + employeeEntity.getFirstName() +
                " Last: " + employeeEntity.getLastName() +
                " Hired: " + employeeEntity.getDateHired().toString() +
                " Department: " + employeeEntity.getDepartment() +
                " Job Title: " + employeeEntity.getJobTitle();
        if(employeerfidcardEntity != null) {
            logMessage = logMessage.concat(" RFID: " + employeerfidcardEntity.getRfid());
        }
        serial.writeToLog(logMessage);
    }

    public void employeeRemoveUpdate(EmployeeEntity employeeEntity) {
        mainWindowController.closeRemoveEmployee();
        serial.writeToLog("Employee removed... ID: " + employeeEntity.getId() +
                " First: " + employeeEntity.getFirstName() +
                " Last: " + employeeEntity.getLastName() +
                " Hired: " + employeeEntity.getDateHired().toString() +
                " Department: " + employeeEntity.getDepartment() +
                " Job Title: " + employeeEntity.getJobTitle());
    }

    public void deviceCreateUpdate(DeviceEntity deviceEntity) {
        mainWindowController.closeCreateDevice();
        serial.writeToLog("Device created... ID: " + deviceEntity.getId() + " Device Name: " + deviceEntity.getDevice());
    }

    public void deviceRemoveUpdate(DeviceEntity deviceEntity) {
        mainWindowController.closeRemoveDevice();
        serial.writeToLog("Device removed... ID: " + deviceEntity.getId() + " Device Name: " + deviceEntity.getDevice());
    }

    public void rfidCreateUpdate(String rfid) {
        mainWindowController.closeCreateRFID(rfid);
        serial.writeToLog("RFID created... RFID: " + rfid);
    }

    public void rfidRemoveUpdate(String rfid) {
        mainWindowController.closeRemoveRFID(rfid);
        serial.writeToLog("RFID removed... RFID: " + rfid);
    }

    public void departmentCreateUpdate(String department) {
        mainWindowController.closeCreateDepartment();
        serial.writeToLog("Department created... Department Name: " + department);
    }

    public void departmentRemoveUpdate(String department) {
        mainWindowController.closeRemoveDepartment();
        serial.writeToLog("Department removed... Department Name: " + department);
    }

}
