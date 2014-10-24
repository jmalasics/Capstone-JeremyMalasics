package Tests;

import DatabaseManipulation.EmployeeController;
import Persistence.EmployeeEntity;
import Persistence.EmployeerfidcardEntity;
import Persistence.RfidcardEntity;
import org.junit.Assert;
import org.junit.Test;
import java.sql.Date;
import java.util.List;

import static org.junit.Assert.*;

public class EmployeeControllerTest {

    @Test
    public void testAddEmployee() throws Exception {
        EmployeeController employeeController = new EmployeeController("capstone");
        Assert.assertSame(employeeController.addEmployee("Jeremy", "Malasics", new Date(System.currentTimeMillis()), "Executive", "President"), true);
        employeeController.dispose();
    }

    @Test
    public void testRemoveEmployee() throws Exception {
        EmployeeController employeeController = new EmployeeController("capstone");
        employeeController.addEmployee("Steve", "Fredicks", new Date(System.currentTimeMillis()), "Maintenance", "Janitor");
        Assert.assertSame(employeeController.removeEmployee(4), true);
        Assert.assertSame(employeeController.removeEmployee(5), true);
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        EmployeeController employeeController = new EmployeeController("capstone");
        EmployeeEntity employeeEntity = employeeController.getEmployeeById(1);
        System.out.println("Employee: " + employeeEntity.getFirstName() + ", " +
                employeeEntity.getLastName() + ", " +
                employeeEntity.getDateHired() + ", " +
                employeeEntity.getDepartment() + ", " +
                employeeEntity.getJobTitle());
    }

    @Test
    public void testGetEmployeesByFirstName() throws Exception {
        EmployeeController employeeController = new EmployeeController("capstone");
        List<EmployeeEntity> employeeEntities = employeeController.getEmployeesByFirstName("Jeremy");
        printList(employeeEntities);
    }

    @Test
    public void testGetEmployeesByLastName() throws Exception {
        EmployeeController employeeController = new EmployeeController("capstone");
        List<EmployeeEntity> employeeEntities = employeeController.getEmployeesByLastName("Williams");
        printList(employeeEntities);
    }

    @Test
    public void testGetEmployeeRFIDCard() throws Exception {
        EmployeeController employeeController = new EmployeeController("capstone");
        EmployeerfidcardEntity employeerfidcardEntity = employeeController.getEmployeeRFIDCard(1);
        System.out.println("RFID: " + employeerfidcardEntity.getRfid());
    }

    @Test
    public void testGetEmployeeList() throws Exception {
        EmployeeController employeeController = new EmployeeController("capstone");
        List<EmployeeEntity> employeeEntities = employeeController.getEmployeeList();
        printList(employeeEntities);
    }

    private void printList(List<EmployeeEntity> employeeEntities) {
        System.out.println("Employee List: ");
        for(EmployeeEntity employeeEntity : employeeEntities) {
            System.out.println(" " + employeeEntity.getFirstName() + ", " + employeeEntity.getLastName());
        }
    }

}