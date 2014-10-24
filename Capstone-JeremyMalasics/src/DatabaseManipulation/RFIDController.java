package DatabaseManipulation;

import Persistence.*;

/**
 * Created by jmalasics on 10/16/2014.
 */
public class RFIDController extends DatabaseController {

    public RFIDController(String persistenceUnit) {
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
            RfidcardEntity rfidcardEntity = new RfidcardEntity();
            rfidcardEntity.setRfidCode(code);
            EmployeerfidcardEntity employeerfidcardEntity = new EmployeerfidcardEntity();
            employeerfidcardEntity.setEmpId(employeeId);
            employeerfidcardEntity.setRfid(code);
            entityManager().persist(rfidcardEntity);
            entityManager().persist(employeerfidcardEntity);
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

}
