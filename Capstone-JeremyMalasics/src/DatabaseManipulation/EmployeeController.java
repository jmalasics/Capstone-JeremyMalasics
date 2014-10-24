package DatabaseManipulation;

import java.sql.Date;
import java.util.List;
import Persistence.*;

/**
 * Created by jmalasics on 10/16/2014.
 */
public class EmployeeController extends DatabaseController {

    public EmployeeController(String persistenceUnit) {
        super(persistenceUnit);
    }

    public boolean addEmployee(String firstName, String lastName, Date dateHired, String department, String jobTitle) {
        try {
            EmployeeEntity employeeEntity = new EmployeeEntity();
            employeeEntity.setFirstName(firstName);
            employeeEntity.setLastName(lastName);
            employeeEntity.setDateHired(dateHired);
            employeeEntity.setDepartment(department);
            employeeEntity.setJobTitle(jobTitle);
            entityManager().getTransaction().begin();
            entityManager().persist(employeeEntity);
            entityManager().getTransaction().commit();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean removeEmployee(int employeeId) {
        try {
            entityManager().getTransaction().begin();
            EmployeeEntity employeeEntity = (EmployeeEntity) entityManager().createNativeQuery("SELECT * FROM employee WHERE id = " + employeeId, EmployeeEntity.class).getSingleResult();
            entityManager().remove(employeeEntity);
            entityManager().getTransaction().commit();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return false;
        }
    }

    public EmployeeEntity getEmployeeById(int employeeId) {
        try {
            entityManager().getTransaction().begin();
            EmployeeEntity employeeEntity = (EmployeeEntity) entityManager().createNativeQuery("SELECT * FROM employee WHERE id = " + employeeId, EmployeeEntity.class).getSingleResult();
            entityManager().getTransaction().commit();
            return employeeEntity;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return null;
        }
    }

    public List<EmployeeEntity> getEmployeesByFirstName(String firstName) {
        try {
            entityManager().getTransaction().begin();
            List<EmployeeEntity> employeeEntities = (List<EmployeeEntity>) entityManager().createNativeQuery("SELECT * FROM employee WHERE firstName = '" + firstName + "'", EmployeeEntity.class).getResultList();
            entityManager().getTransaction().commit();
            return employeeEntities;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return null;
        }
    }

    public List<EmployeeEntity> getEmployeesByLastName(String lastName) {
        try {
            entityManager().getTransaction().begin();
            List<EmployeeEntity> employeeEntities = (List<EmployeeEntity>) entityManager().createNativeQuery("SELECT * FROM employee WHERE lastName = '" + lastName + "'", EmployeeEntity.class).getResultList();
            entityManager().getTransaction().commit();
            return employeeEntities;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return null;
        }
    }

    public EmployeerfidcardEntity getEmployeeRFIDCard(int employeeId) {
        try {
            entityManager().getTransaction().begin();
            EmployeerfidcardEntity employeerfidcardEntity = (EmployeerfidcardEntity) entityManager().createNativeQuery("SELECT * FROM employeerfidcard WHERE empId = " + employeeId, EmployeerfidcardEntity.class).getSingleResult();
            entityManager().getTransaction().commit();
            return employeerfidcardEntity;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return null;
        }
    }

    public List<EmployeeEntity> getEmployeeList() {
        try {
            entityManager().getTransaction().begin();
            List<EmployeeEntity> employeeEntities = (List<EmployeeEntity>) entityManager().createNativeQuery("SELECT * FROM employee", EmployeeEntity.class).getResultList();
            entityManager().getTransaction().commit();
            return employeeEntities;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return null;
        }
    }

}
