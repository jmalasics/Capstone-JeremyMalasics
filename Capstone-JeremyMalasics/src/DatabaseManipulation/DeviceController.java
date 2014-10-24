package DatabaseManipulation;

import java.util.List;
import Persistence.*;

/**
 * Created by jmalasics on 10/16/2014.
 */
public class DeviceController extends DatabaseController {

    public DeviceController(String persistenceUnit) {
        super(persistenceUnit);
    }

    public boolean addDevice(String device) {
        //TODO Test add device
        try {
            entityManager().getTransaction().begin();
            DeviceEntity deviceEntity = new DeviceEntity();
            deviceEntity.setDevice(device);
            entityManager().persist(deviceEntity);
            entityManager().getTransaction().commit();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean removeDevice(int deviceId) {
        //TODO Test remove device
        try {
            entityManager().getTransaction().begin();
            DeviceEntity deviceEntity = (DeviceEntity) entityManager().createNativeQuery("SELECT * FROM device").getSingleResult();
            entityManager().remove(deviceEntity);
            entityManager().getTransaction().commit();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return false;
        }
    }

    public DeviceEntity getDevice(int deviceId) {
        //TODO Test get device
        try {
            entityManager().getTransaction().begin();
            DeviceEntity deviceEntity = (DeviceEntity) entityManager().createNativeQuery("SELECT * FROM device WHERE id = " + deviceId).getSingleResult();
            entityManager().getTransaction().commit();
            return deviceEntity;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return null;
        }
    }

    public List<DeviceEntity> getDeviceList() {
        //TODO Test list devices
        try {
            entityManager().getTransaction().begin();
            List<DeviceEntity> deviceEntities = (List<DeviceEntity>) entityManager().createNativeQuery("SELECT * FROM device").getResultList();
            entityManager().getTransaction().commit();
            return deviceEntities;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return null;
        }
    }

}
