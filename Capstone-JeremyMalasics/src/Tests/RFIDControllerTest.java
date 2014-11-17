package Tests;

import DatabaseManipulation.EmployeeDatabaseController;
import DatabaseManipulation.RFIDDatabaseController;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;

public class RFIDControllerTest {

    @Test
    public void testValidate() throws Exception {
        RFIDDatabaseController rfidController = new RFIDDatabaseController("capstone");
        Assert.assertSame(rfidController.validate("010077A2A7".toCharArray()), true);
        rfidController.dispose();
    }

    @Test
    public void testAddRFIDCard() throws Exception {
        RFIDDatabaseController rfidController = new RFIDDatabaseController("capstone");
        Assert.assertSame(rfidController.addRFIDCard("010077A2A7".toCharArray(), 1), true);
        rfidController.dispose();
    }

    @Test
    public void testRemoveRFIDCard() throws Exception {
        EmployeeDatabaseController employeeController = new EmployeeDatabaseController("capstone");
        employeeController.addEmployee("Greg", "Williams", new Date(System.currentTimeMillis()), "IT", "System Admin");
        employeeController.dispose();
        RFIDDatabaseController rfidController = new RFIDDatabaseController("capstone");
        rfidController.addRFIDCard("01007687A5".toCharArray(), 2);
        Assert.assertSame(rfidController.removeRFIDCard("01007687A5".toCharArray()), true);
        rfidController.dispose();
    }

}