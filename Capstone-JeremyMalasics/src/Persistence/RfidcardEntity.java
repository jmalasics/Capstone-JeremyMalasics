package Persistence;

import javax.persistence.*;

/**
 * Created by jmalasics on 11/30/2014.
 */
@Entity
@Table(name = "rfidcard", schema = "", catalog = "capstonejeremymalasics")
public class RfidcardEntity {
    private int id;
    private String rfidCode;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "rfidCode")
    public String getRfidCode() {
        return rfidCode;
    }

    public void setRfidCode(String rfidCode) {
        this.rfidCode = rfidCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RfidcardEntity that = (RfidcardEntity) o;

        if (id != that.id) return false;
        if (rfidCode != null ? !rfidCode.equals(that.rfidCode) : that.rfidCode != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (rfidCode != null ? rfidCode.hashCode() : 0);
        return result;
    }
}
