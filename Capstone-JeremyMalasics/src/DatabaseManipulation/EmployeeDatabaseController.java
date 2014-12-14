package DatabaseManipulation;

import java.sql.Date;
import java.util.List;

import Persistence.*;

/**
 * Created by jmalasics on 10/16/2014.
 */
public class EmployeeDatabaseController extends DatabaseController {

    public EmployeeDatabaseController(String persistenceUnit) {
        super(persistenceUnit);
    }

    public EmployeeEntity addEmployee(String firstName, String lastName, Date dateHired, String department, String jobTitle) {
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
            return employeeEntity;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return null;
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

    public boolean modifyEmployee(EmployeeEntity employeeEntity) {
        try {
            entityManager().getTransaction().begin();
            entityManager().merge(employeeEntity);
            entityManager().getTransaction().commit();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean modifyEmployeeRFIDCard(int employeeId, String rfidCode) {
        try {
            entityManager().getTransaction().begin();
            entityManager().createNativeQuery("UPDATE employeerfidcard SET rfid = " + rfidCode + " WHERE empId = " + employeeId);
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
            entityManager().getTransaction().rollback();
            EmployeerfidcardEntity noRFID = new EmployeerfidcardEntity();
            noRFID.setEmpId(employeeId);
            noRFID.setRfid(null);
            return noRFID;
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

    public List<EmployeeEntity> getEmployeesByDepartment(String department) {
        try {
            entityManager().getTransaction().begin();
            List<EmployeeEntity> employeeEntities = (List<EmployeeEntity>) entityManager().createNativeQuery("SELECT * FROM employee WHERE department = '" + department + "'", EmployeeEntity.class).getResultList();
            entityManager().getTransaction().commit();
            return employeeEntities;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return null;
        }
    }

    public List<DepartmentEntity> getDepartments() {
        try {
            entityManager().getTransaction().begin();
            List<DepartmentEntity> departmentEntities = (List<DepartmentEntity>) entityManager().createNativeQuery("SELECT * FROM department", DepartmentEntity.class).getResultList();
            entityManager().getTransaction().commit();
            return departmentEntities;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return null;
        }
    }

    public EmployeeEntity getEmployeeByFirstAndLast(String firstName, String lastName) {
        try {
            entityManager().getTransaction().begin();
            EmployeeEntity employeeEntity = (EmployeeEntity) entityManager().createNativeQuery("SELECT * FROM employee WHERE firstName = '" + firstName + "' AND lastName = '" + lastName + "'", EmployeeEntity.class).getSingleResult();
            entityManager().getTransaction().commit();
            return employeeEntity;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return null;
        }
    }

    public List<RfidusagehistoryEntity> getEmployeeHistory(int employeeId) {
        try {
            entityManager().getTransaction().begin();
            List<RfidusagehistoryEntity> rfidusagehistoryEntities = (List<RfidusagehistoryEntity>) entityManager().createNativeQuery("SELECT * FROm rfidusagehistory WHERE employeeID = " + employeeId, RfidusagehistoryEntity.class).getResultList();
            entityManager().getTransaction().commit();
            return rfidusagehistoryEntities;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return null;
        }
    }

    public boolean addDepartment(String name) {
        try {
            entityManager().getTransaction().begin();
            DepartmentEntity departmentEntity = new DepartmentEntity();
            departmentEntity.setDepartment(name);
            entityManager().persist(departmentEntity);
            entityManager().getTransaction().commit();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean removeDepartment(String name) {
        try {
            entityManager().getTransaction().begin();
            DepartmentEntity departmentEntity = (DepartmentEntity) entityManager().createNativeQuery("SELECT * FROM department WHERE department = '" + name + "'", DepartmentEntity.class).getSingleResult();
            entityManager().remove(departmentEntity);
            entityManager().getTransaction().commit();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return false;
        }
    }

}
