package Persistence;

import javax.persistence.*;

/**
 * Created by jmalasics on 11/24/2014.
 */
@Entity
@Table(name = "employeerfidcard", schema = "", catalog = "capstonejeremymalasics")
public class EmployeerfidcardEntity {
    private int id;
    private String rfid;
    private Integer empId;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "rfid")
    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    @Basic
    @Column(name = "empId")
    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmployeerfidcardEntity that = (EmployeerfidcardEntity) o;

        if (id != that.id) return false;
        if (empId != null ? !empId.equals(that.empId) : that.empId != null) return false;
        if (rfid != null ? !rfid.equals(that.rfid) : that.rfid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (rfid != null ? rfid.hashCode() : 0);
        result = 31 * result + (empId != null ? empId.hashCode() : 0);
        return result;
    }
}
