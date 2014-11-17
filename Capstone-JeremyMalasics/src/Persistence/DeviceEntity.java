package Persistence;

import javax.persistence.*;

/**
 * Created by jmalasics on 11/11/2014.
 */
@Entity
@Table(name = "device", schema = "", catalog = "capstonejeremymalasics")
public class DeviceEntity {
    private int id;
    private String device;
    private int totalUsage;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "device")
    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    @Basic
    @Column(name = "totalUsage")
    public int getTotalUsage() {
        return totalUsage;
    }

    public void setTotalUsage(int totalUsage) {
        this.totalUsage = totalUsage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceEntity that = (DeviceEntity) o;

        if (id != that.id) return false;
        if (totalUsage != that.totalUsage) return false;
        if (device != null ? !device.equals(that.device) : that.device != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (device != null ? device.hashCode() : 0);
        result = 31 * result + totalUsage;
        return result;
    }
}
