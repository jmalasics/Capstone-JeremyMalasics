package Persistence;

import javax.persistence.*;

/**
 * Created by jmalasics on 10/15/2014.
 */
@Entity
@Table(name = "rfidcard", schema = "", catalog = "capstonejeremymalasics")
public class RfidcardEntity {
    private int id;
    private int rfidCode;

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
    public int getRfidCode() {
        return rfidCode;
    }

    public void setRfidCode(int rfidCode) {
        this.rfidCode = rfidCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RfidcardEntity that = (RfidcardEntity) o;

        if (id != that.id) return false;
        if (rfidCode != that.rfidCode) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + rfidCode;
        return result;
    }
}
