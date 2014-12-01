package Persistence;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by jmalasics on 11/30/2014.
 */
@Entity
@Table(name = "rfidusagehistory", schema = "", catalog = "capstonejeremymalasics")
public class RfidusagehistoryEntity {
    private int id;
    private int employeeId;
    private String rfidCode;
    private String device;
    private Timestamp timeUsed;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "employeeID")
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    @Basic
    @Column(name = "rfidCode")
    public String getRfidCode() {
        return rfidCode;
    }

    public void setRfidCode(String rfidCode) {
        this.rfidCode = rfidCode;
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
    @Column(name = "timeUsed")
    public Timestamp getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(Timestamp timeUsed) {
        this.timeUsed = timeUsed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RfidusagehistoryEntity that = (RfidusagehistoryEntity) o;

        if (employeeId != that.employeeId) return false;
        if (id != that.id) return false;
        if (device != null ? !device.equals(that.device) : that.device != null) return false;
        if (rfidCode != null ? !rfidCode.equals(that.rfidCode) : that.rfidCode != null) return false;
        if (timeUsed != null ? !timeUsed.equals(that.timeUsed) : that.timeUsed != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + employeeId;
        result = 31 * result + (rfidCode != null ? rfidCode.hashCode() : 0);
        result = 31 * result + (device != null ? device.hashCode() : 0);
        result = 31 * result + (timeUsed != null ? timeUsed.hashCode() : 0);
        return result;
    }
}
