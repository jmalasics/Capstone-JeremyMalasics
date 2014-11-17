package Persistence;

import javax.persistence.*;

/**
 * Created by jmalasics on 11/11/2014.
 */
@Entity
@Table(name = "department", schema = "", catalog = "capstonejeremymalasics")
public class DepartmentEntity {
    private int id;
    private String department;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "department")
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DepartmentEntity that = (DepartmentEntity) o;

        if (id != that.id) return false;
        if (department != null ? !department.equals(that.department) : that.department != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (department != null ? department.hashCode() : 0);
        return result;
    }
}
