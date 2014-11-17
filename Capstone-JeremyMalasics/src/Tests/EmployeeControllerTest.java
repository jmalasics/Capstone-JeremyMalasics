package Tests;

import DatabaseManipulation.EmployeeDatabaseController;
import Persistence.EmployeeEntity;
import Persistence.EmployeerfidcardEntity;
import org.junit.Assert;
import org.junit.Test;
import java.sql.Date;
import java.util.List;

public class EmployeeControllerTest {

    @Test
    public void testAddEmployee() throws Exception {
        EmployeeDatabaseController employeeController = new EmployeeDatabaseController("capstone");
        Assert.assertSame(employeeController.addEmployee("Jeremy", "Malasics", new Date(System.currentTimeMillis()), "Executive", "President"), true);
        employeeController.dispose();
    }

    @Test
    public void testRemoveEmployee() throws Exception {
        EmployeeDatabaseController employeeController = new EmployeeDatabaseController("capstone");
        employeeController.addEmployee("Steve", "Fredicks", new Date(System.currentTimeMillis()), "Maintenance", "Janitor");
        Assert.assertSame(employeeController.removeEmployee(4), true);
        Assert.assertSame(employeeController.removeEmployee(5), true);
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        EmployeeDatabaseController employeeController = new EmployeeDatabaseController("capstone");
        EmployeeEntity employeeEntity = employeeController.getEmployeeById(1);
        System.out.println("Employee: " + employeeEntity.getFirstName() + ", " +
                employeeEntity.getLastName() + ", " +
                employeeEntity.getDateHired() + ", " +
                employeeEntity.getDepartment() + ", " +
                employeeEntity.getJobTitle());
    }

    @Test
    public void testGetEmployeesByFirstName() throws Exception {
        EmployeeDatabaseController employeeController = new EmployeeDatabaseController("capstone");
        List<EmployeeEntity> employeeEntities = employeeController.getEmployeesByFirstName("Jeremy");
        printList(employeeEntities);
    }

    @Test
    public void testGetEmployeesByLastName() throws Exception {
        EmployeeDatabaseController employeeController = new EmployeeDatabaseController("capstone");
        List<EmployeeEntity> employeeEntities = employeeController.getEmployeesByLastName("Williams");
        printList(employeeEntities);
    }

    @Test
    public void testGetEmployeeRFIDCard() throws Exception {
        EmployeeDatabaseController employeeController = new EmployeeDatabaseController("capstone");
        EmployeerfidcardEntity employeerfidcardEntity = employeeController.getEmployeeRFIDCard(1);
        System.out.println("RFID: " + employeerfidcardEntity.getRfid());
    }

    @Test
    public void testGetEmployeeList() throws Exception {
        EmployeeDatabaseController employeeController = new EmployeeDatabaseController("capstone");
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