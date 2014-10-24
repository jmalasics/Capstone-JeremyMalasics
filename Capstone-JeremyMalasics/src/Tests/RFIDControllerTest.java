package Tests;

import DatabaseManipulation.EmployeeController;
import DatabaseManipulation.RFIDController;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;

import static org.junit.Assert.*;

public class RFIDControllerTest {

    @Test
    public void testValidate() throws Exception {
        RFIDController rfidController = new RFIDController("capstone");
        Assert.assertSame(rfidController.validate("010077A2A7".toCharArray()), true);
        rfidController.dispose();
    }

    @Test
    public void testAddRFIDCard() throws Exception {
        RFIDController rfidController = new RFIDController("capstone");
        Assert.assertSame(rfidController.addRFIDCard("010077A2A7".toCharArray(), 1), true);
        rfidController.dispose();
    }

    @Test
    public void testRemoveRFIDCard() throws Exception {
        EmployeeController employeeController = new EmployeeController("capstone");
        employeeController.addEmployee("Greg", "Williams", new Date(System.currentTimeMillis()), "IT", "System Admin");
        employeeController.dispose();
        RFIDController rfidController = new RFIDController("capstone");
        rfidController.addRFIDCard("01007687A5".toCharArray(), 2);
        Assert.assertSame(rfidController.removeRFIDCard("01007687A5".toCharArray()), true);
        rfidController.dispose();
    }

}