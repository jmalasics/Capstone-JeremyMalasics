package ArduinoConnection;

import DatabaseManipulation.RFIDDatabaseController;
import jssc.SerialPort;
import jssc.SerialPortException;

import java.io.*;
import java.util.Date;

/**
 * Created by jmalasics on 10/27/2014.
 */
public class SerialClass {
/**
    public static void main(String[] args) {
        SerialClass serial = new SerialClass();
        serial.initialize();
        serial.writeData("SYSTEM INITIALIZED.");
        boolean running = true;
        while(running) {
            serial.readData();
            running = serial.isRunning();
        }
        serial.close();
    }
*/
    public SerialPort serialPort;
    private static final String PORT_NAME = "COM5";
    public static final int TIME_OUT = 2000;
    public static final int DATA_RATE = 2400;
    private RFIDDatabaseController rfidController;
    private boolean running = true;

    public void initialize() {
        try {
            writeToLog("System initializing...");
            rfidController = new RFIDDatabaseController("capstone");
            serialPort = new SerialPort(PORT_NAME);
            serialPort.openPort();
            serialPort.setParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            Thread.sleep(TIME_OUT);
            writeToLog("System initialized.");
        } catch(SerialPortException spe) {
            spe.printStackTrace();
            writeToLog("ERROR. Exception thrown initializing serial connection.");
        } catch(InterruptedException ie) {
            ie.printStackTrace();
            writeData("ERROR. Thread interrupted exception thrown.");
        }
    }

    public void close() {
        try {
            serialPort.closePort();
        } catch(SerialPortException spe) {
            spe.printStackTrace();
            writeToLog("ERROR. Exception thrown closing serial connection.");
        }
    }

    public void readData() {
        try {
            String data = serialPort.readString(41);
            if(data != null) {
                String[] splitData = data.split(": ");
                if (splitData[0].equals("TAG")) {
                    String[] code = splitData[1].split("  ");
                    validateRFID(code[0]);
                } else if (splitData[0].equals("LOG")) {
                    writeToLog(splitData[1]);
                } else {
                    writeToLog("ERROR. Invalid response received.");
                    running = false;
                }
            }
        } catch(SerialPortException spe) {
            spe.printStackTrace();
            writeToLog("ERROR. Exception thrown reading from serial.");
        }
    }


    public void writeData(String data) {
        try {
            serialPort.writeBytes(data.getBytes());
        } catch(SerialPortException spe) {
            spe.printStackTrace();
            writeToLog("ERROR. Exception thrown writing to serial.");
        }
    }

    public void writeToLog(String message) {
        Date currentTime = new Date(System.currentTimeMillis());
        String timestamp = "[" + currentTime.toString() + "] ";
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter("SystemLog.txt", true));
            bufferedWriter.write(timestamp + message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch(IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    private void validateRFID(String message) {
        writeToLog("RFID READ: " + message);
        char[] tag = message.toCharArray();
        if(rfidController.validate(tag)) {
            writeData("TAG VALID.  ");
            writeToLog("RFID VALID: " + message);
        } else {
            writeData("TAG INVALID.");
            writeToLog("RFID INVALID: " + message);
        }
    }

    public boolean isRunning() {
        return running;
    }

}
