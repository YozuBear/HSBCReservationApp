package rest.domain;

/**
 * Inquire an Employee's reservation by name
 * @author yozubear
 */
public class InquireEmployee {
    String inquireDate;
    String employeeId;  // matches any name *employeeId*
                          // e.g. 'William' would match employees 'william Jones', 'Kate William' etc..


    public InquireEmployee(){}

    public InquireEmployee(String inquireDate, String employeeId) {
        this.inquireDate = inquireDate;
        this.employeeId = employeeId;
    }

    public String getInquireDate() {
        return inquireDate;
    }

    public void setInquireDate(String inquireDate) {
        this.inquireDate = inquireDate;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
