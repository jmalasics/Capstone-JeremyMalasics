package DatabaseManipulation;

import Persistence.*;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * Created by jmalasics on 10/16/2014.
 */
public class RFIDDatabaseController extends DatabaseController {

    public RFIDDatabaseController(String persistenceUnit) {
        super(persistenceUnit);
    }

    public boolean validate(char[] rfidcardCode) {
        try {
            entityManager().getTransaction().begin();
            String code = new String(rfidcardCode);
            RfidcardEntity rfidcardEntity = (RfidcardEntity) entityManager().createNativeQuery("SELECT * FROM rfidcard WHERE rfidCode = '" + code + "'", RfidcardEntity.class).getSingleResult();
            entityManager().getTransaction().commit();
            return rfidcardEntity != null;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return false;
        }
    }

    public EmployeerfidcardEntity addRFIDCard(char[] rfidCode, int employeeId) {
        EmployeerfidcardEntity employeerfidcardEntity;
        String code = new String(rfidCode);
        try {
            entityManager().getTransaction().begin();
            employeerfidcardEntity = (EmployeerfidcardEntity) entityManager().createNativeQuery("SELECT * FROM employeerfidcard WHERE empId = " + employeeId, EmployeerfidcardEntity.class).getSingleResult();
            employeerfidcardEntity.setRfid(code);
            entityManager().merge(employeerfidcardEntity);
            entityManager().getTransaction().commit();
            return employeerfidcardEntity;
        } catch(NoResultException nre) {
            employeerfidcardEntity = new EmployeerfidcardEntity();
            employeerfidcardEntity.setEmpId(employeeId);
            employeerfidcardEntity.setRfid(code);
            entityManager().persist(employeerfidcardEntity);
            entityManager().getTransaction().commit();
            return employeerfidcardEntity;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return null;
        }
    }

    public boolean addRFIDCard(char[] rfidCode) {
        try {
            String code = new String(rfidCode);
            entityManager().getTransaction().begin();
            RfidcardEntity rfidcardEntity = new RfidcardEntity();
            rfidcardEntity.setRfidCode(code);
            entityManager().persist(rfidcardEntity);
            entityManager().getTransaction().commit();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean removeRFIDCard(char[] rfidCode) {
        String code = new String(rfidCode);
        try {
            entityManager().getTransaction().begin();
            EmployeerfidcardEntity employeerfidcardEntity = (EmployeerfidcardEntity) entityManager().createNativeQuery("SELECT * FROM employeerfidcard WHERE rfid = '" + code + "'", EmployeerfidcardEntity.class).getSingleResult();
            entityManager().remove(employeerfidcardEntity);
            RfidcardEntity rfidcardEntity = (RfidcardEntity) entityManager().createNativeQuery("SELECT * FROM rfidcard WHERE rfidCode = '" + code + "'", RfidcardEntity.class).getSingleResult();
            entityManager().remove(rfidcardEntity);
            entityManager().getTransaction().commit();
            return true;
        } catch(NoResultException nre) {
            RfidcardEntity rfidcardEntity = (RfidcardEntity) entityManager().createNativeQuery("SELECT * FROM rfidcard WHERE rfidCode = '" + code + "'", RfidcardEntity.class).getSingleResult();
            entityManager().remove(rfidcardEntity);
            entityManager().getTransaction().commit();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean removeRFIDFromEmployee(int employeeId) {
        try {
            entityManager().getTransaction().begin();
            EmployeerfidcardEntity employeerfidcardEntity = (EmployeerfidcardEntity) entityManager().createNativeQuery("SELECT * FROM employeerfidcard WHERE empId = " + employeeId, EmployeerfidcardEntity.class).getSingleResult();
            entityManager().remove(employeerfidcardEntity);
            entityManager().getTransaction().commit();
            return true;
        } catch(NoResultException nre) {
            entityManager().getTransaction().rollback();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return false;
        }
    }

    public RfidcardEntity getRFIDCard(char[] rfidCode) {
        try {
            String code = new String(rfidCode);
            entityManager().getTransaction().begin();
            RfidcardEntity rfidcardEntity = (RfidcardEntity) entityManager().createNativeQuery("SELECT * FROM rfidcard WHERE rfidCode = '" + code + "'", RfidcardEntity.class).getSingleResult();
            entityManager().getTransaction().commit();
            return rfidcardEntity;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return null;
        }
    }

    public List<RfidcardEntity> getAllRFID() {
        try {
            entityManager().getTransaction().begin();
            List<RfidcardEntity> rfidcardEntities = (List<RfidcardEntity>) entityManager().createNativeQuery("SELECT * FROM rfidcard", RfidcardEntity.class).getResultList();
            entityManager().getTransaction().commit();
            return rfidcardEntities;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
        }
        return null;
    }

    public boolean isRFIDTaken(String rfidCode) {
        try {
            entityManager().getTransaction().begin();
            List<EmployeerfidcardEntity> employeerfidcardEntities = (List<EmployeerfidcardEntity>) entityManager().createNativeQuery("SELECT * FROM employeerfidcard", EmployeerfidcardEntity.class).getResultList();
            for(EmployeerfidcardEntity employeerfidcardEntity : employeerfidcardEntities) {
                if(employeerfidcardEntity.getRfid().equals(rfidCode)) {
                    entityManager().getTransaction().commit();
                    return true;
                }
            }
            entityManager().getTransaction().commit();
            return false;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return false;
        }
    }

    public List<EmployeerfidcardEntity> getRFIDList() {
        try {
            entityManager().getTransaction().begin();
            List<EmployeerfidcardEntity> employeerfidcardEntities = (List<EmployeerfidcardEntity>) entityManager().createNativeQuery("SELECT * FROM employeerfidcard", EmployeerfidcardEntity.class).getResultList();
            List<RfidcardEntity> rfidcardEntities = (List<RfidcardEntity>) entityManager().createNativeQuery("SELECT * FROM rfidcard", RfidcardEntity.class).getResultList();
            entityManager().getTransaction().commit();
            employeerfidcardEntities = addFreeRFIDs(employeerfidcardEntities, rfidcardEntities);
            return employeerfidcardEntities;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return null;
        }
    }

    private List<EmployeerfidcardEntity> addFreeRFIDs(List<EmployeerfidcardEntity> takenRfids, List<RfidcardEntity> allRfids) {
        for(RfidcardEntity rfidcardEntity : allRfids) {
            if(!isRFIDTaken(rfidcardEntity.getRfidCode())) {
                EmployeerfidcardEntity employeerfidcardEntity = new EmployeerfidcardEntity();
                employeerfidcardEntity.setRfid(rfidcardEntity.getRfidCode());
                takenRfids.add(employeerfidcardEntity);
            }
        }
        return takenRfids;
    }

}
