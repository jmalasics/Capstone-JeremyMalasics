package ArduinoConnection;

import DatabaseManipulation.*;

import java.util.Date;

/**
 * Created by jmalasics on 10/23/2014.
 */
public class ArduinoConnection {

    private static final String PERSISTENCE_UNIT = "capstone";

    private IOHandler ioHandler;
    private RFIDController rfidController;
    private EmployeeController employeeController;
    private DeviceController deviceController;

    public ArduinoConnection() {
        ioHandler = new IOHandler();
        rfidController = new RFIDController(PERSISTENCE_UNIT);
        employeeController = new EmployeeController(PERSISTENCE_UNIT);
        deviceController = new DeviceController(PERSISTENCE_UNIT);
    }

    public void writeToLog(String message) {

    }

    public void validateRFID(String message) {
        String[] splitMessage = message.split(" ");
        char[] tag = splitMessage[1].toCharArray();
        if(rfidController.validate(tag)) {
            ioHandler.write("TAG VALID.");
            Date currentTime = new Date(System.currentTimeMillis());
            writeToLog("[" + currentTime.toString() + "] ");
        } else {
            ioHandler.write("TAG INVALID.");
        }
    }

}
