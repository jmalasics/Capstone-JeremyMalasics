package DatabaseManipulation;

import java.util.List;
import Persistence.*;

/**
 * Created by jmalasics on 10/16/2014.
 */
public class DeviceDatabaseController extends DatabaseController {

    public DeviceDatabaseController(String persistenceUnit) {
        super(persistenceUnit);
    }

    public boolean addDevice(String device) {
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
        try {
            entityManager().getTransaction().begin();
            DeviceEntity deviceEntity = (DeviceEntity) entityManager().createNativeQuery("SELECT * FROM device WHERE id = " + deviceId, DeviceEntity.class).getSingleResult();
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
        try {
            entityManager().getTransaction().begin();
            DeviceEntity deviceEntity = (DeviceEntity) entityManager().createNativeQuery("SELECT * FROM device WHERE id = " + deviceId, DeviceEntity.class).getSingleResult();
            entityManager().getTransaction().commit();
            return deviceEntity;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return null;
        }
    }

    public DeviceEntity getDevice(String deviceName) {
        try {
            entityManager().getTransaction().begin();
            DeviceEntity deviceEntity = (DeviceEntity) entityManager().createNativeQuery("SELECT * FROM device WHERE device = '" + deviceName + "'", DeviceEntity.class).getSingleResult();
            entityManager().getTransaction().commit();
            return deviceEntity;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return null;
        }
    }

    public List<DeviceEntity> getDeviceList() {
        try {
            entityManager().getTransaction().begin();
            List<DeviceEntity> deviceEntities = (List<DeviceEntity>) entityManager().createNativeQuery("SELECT * FROM device", DeviceEntity.class).getResultList();
            entityManager().getTransaction().commit();
            return deviceEntities;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return null;
        }
    }

    public List<DeviceactivationtimesEntity> getDeviceActivationTimes(DeviceEntity deviceEntity) {
        try {
            entityManager().getTransaction().begin();
            List<DeviceactivationtimesEntity> deviceactivationtimesEntities = (List<DeviceactivationtimesEntity>) entityManager().createNativeQuery("SELECT * FROM deviceactivationtimes WHERE deviceID = " + deviceEntity.getId(), DeviceactivationtimesEntity.class).getResultList();
            entityManager().getTransaction().commit();
            return deviceactivationtimesEntities;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return null;
        }
    }

    public List<DeviceusagehistoryEntity> getDeviceUsageHistory(DeviceEntity deviceEntity) {
        try {
            entityManager().getTransaction().begin();
            List<DeviceusagehistoryEntity> deviceusagehistoryEntities = (List<DeviceusagehistoryEntity>) entityManager().createNativeQuery("SELECT * FROM deviceusagehistory WHERE deviceId = " + deviceEntity.getId(), DeviceusagehistoryEntity.class).getResultList();
            entityManager().getTransaction().commit();
            return deviceusagehistoryEntities;
        } catch(Exception e) {
            e.printStackTrace();
            entityManager().getTransaction().rollback();
            return null;
        }
    }

}
