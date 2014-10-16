package Persistence;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by jmalasics on 10/15/2014.
 */
@Entity
@Table(name = "deviceusagehistory", schema = "", catalog = "capstonejeremymalasics")
public class DeviceusagehistoryEntity {
    private int id;
    private int deviceId;
    private Timestamp timeStored;
    private int recentUsage;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "deviceId")
    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    @Basic
    @Column(name = "timeStored")
    public Timestamp getTimeStored() {
        return timeStored;
    }

    public void setTimeStored(Timestamp timeStored) {
        this.timeStored = timeStored;
    }

    @Basic
    @Column(name = "recentUsage")
    public int getRecentUsage() {
        return recentUsage;
    }

    public void setRecentUsage(int recentUsage) {
        this.recentUsage = recentUsage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceusagehistoryEntity that = (DeviceusagehistoryEntity) o;

        if (deviceId != that.deviceId) return false;
        if (id != that.id) return false;
        if (recentUsage != that.recentUsage) return false;
        if (timeStored != null ? !timeStored.equals(that.timeStored) : that.timeStored != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + deviceId;
        result = 31 * result + (timeStored != null ? timeStored.hashCode() : 0);
        result = 31 * result + recentUsage;
        return result;
    }
}
