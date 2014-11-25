package DatabaseManipulation;

import Persistence.*;

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

    public boolean addRFIDCard(char[] rfidCode, int employeeId) {
        try {
            String code = new String(rfidCode);
            entityManager().getTransaction().begin();
            EmployeerfidcardEntity employeerfidcardEntity = new EmployeerfidcardEntity();
            employeerfidcardEntity.setEmpId(employeeId);
            employeerfidcardEntity.setRfid(code);
            entityManager().persist(employeerfidcardEntity);
            entityManager().getTransaction().commit();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return false;
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
        try {
            String code = new String(rfidCode);
            entityManager().getTransaction().begin();
            entityManager().createNativeQuery("DELETE FROM rfidcard WHERE rfidCode = " + code);
            entityManager().createNativeQuery("DELETE FROM employeerfidcard WHERE rfid = " + code);
            entityManager().getTransaction().commit();
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

}
