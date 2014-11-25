package Persistence;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by jmalasics on 11/24/2014.
 */
@Entity
@Table(name = "deviceactivationtimes", schema = "", catalog = "capstonejeremymalasics")
public class DeviceactivationtimesEntity {
    private int id;
    private int deviceId;
    private Timestamp activationTime;
    private Timestamp disableTime;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "deviceID")
    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    @Basic
    @Column(name = "activationTime")
    public Timestamp getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(Timestamp activationTime) {
        this.activationTime = activationTime;
    }

    @Basic
    @Column(name = "disableTime")
    public Timestamp getDisableTime() {
        return disableTime;
    }

    public void setDisableTime(Timestamp disableTime) {
        this.disableTime = disableTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceactivationtimesEntity that = (DeviceactivationtimesEntity) o;

        if (deviceId != that.deviceId) return false;
        if (id != that.id) return false;
        if (activationTime != null ? !activationTime.equals(that.activationTime) : that.activationTime != null)
            return false;
        if (disableTime != null ? !disableTime.equals(that.disableTime) : that.disableTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + deviceId;
        result = 31 * result + (activationTime != null ? activationTime.hashCode() : 0);
        result = 31 * result + (disableTime != null ? disableTime.hashCode() : 0);
        return result;
    }
}
