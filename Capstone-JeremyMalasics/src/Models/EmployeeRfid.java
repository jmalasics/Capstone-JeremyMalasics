package Models;

/**
 * Created by jmalasics on 11/26/2014.
 */
public class EmployeeRfid {

    private String firstName;
    private String lastName;
    private String employeeId;
    private String rfidCode;

    public EmployeeRfid(String employeeId, String rfidCode, String firstName, String lastName) {
        this.employeeId = employeeId;
        this.rfidCode = rfidCode;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getRfidCode() {
        return rfidCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

}
