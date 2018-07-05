package rest.domain;

/**
 * HSBC User class, mimic the internal HSBC user info JSON response
 * @author yozubear
 */
public class HSBCUser {

    private String staffDetails_name;
    private String staffDetails_empid;
    private String staffDetails_extphone;
    private String staffDetails_country;
    private String staffDetails_jobrole;
    private String staffDetails_dept;
    private String staffDetails_photourl;
    private String staffDetails_extemail;
    private String staffDetails_employeeType;
    private String staffDetails_location;
    private String staffDetails_intphone;
    private String staffDetails_postaladdress;
    private String errormessage;

    public HSBCUser(){}
    public HSBCUser(String staffDetails_name, String staffDetails_empid, String staffDetails_extphone,
                    String staffDetails_country, String staffDetails_jobrole, String staffDetails_dept,
                    String staffDetails_photourl, String staffDetails_extemail, String staffDetails_employeeType,
                    String staffDetails_location, String staffDetails_intphone, String staffDetails_postaladdress,
                    String errormessage) {
        this.staffDetails_name = staffDetails_name;
        this.staffDetails_empid = staffDetails_empid;
        this.staffDetails_extphone = staffDetails_extphone;
        this.staffDetails_country = staffDetails_country;
        this.staffDetails_jobrole = staffDetails_jobrole;
        this.staffDetails_dept = staffDetails_dept;
        this.staffDetails_photourl = staffDetails_photourl;
        this.staffDetails_extemail = staffDetails_extemail;
        this.staffDetails_employeeType = staffDetails_employeeType;
        this.staffDetails_location = staffDetails_location;
        this.staffDetails_intphone = staffDetails_intphone;
        this.staffDetails_postaladdress = staffDetails_postaladdress;
        this.errormessage = errormessage;
    }

    public String getStaffDetails_name() {
        return staffDetails_name;
    }

    public void setStaffDetails_name(String staffDetails_name) {
        this.staffDetails_name = staffDetails_name;
    }

    public String getStaffDetails_empid() {
        return staffDetails_empid;
    }

    public void setStaffDetails_empid(String staffDetails_empid) {
        this.staffDetails_empid = staffDetails_empid;
    }

    public String getStaffDetails_extphone() {
        return staffDetails_extphone;
    }

    public void setStaffDetails_extphone(String staffDetails_extphone) {
        this.staffDetails_extphone = staffDetails_extphone;
    }

    public String getStaffDetails_country() {
        return staffDetails_country;
    }

    public void setStaffDetails_country(String staffDetails_country) {
        this.staffDetails_country = staffDetails_country;
    }

    public String getStaffDetails_jobrole() {
        return staffDetails_jobrole;
    }

    public void setStaffDetails_jobrole(String staffDetails_jobrole) {
        this.staffDetails_jobrole = staffDetails_jobrole;
    }

    public String getStaffDetails_dept() {
        return staffDetails_dept;
    }

    public void setStaffDetails_dept(String staffDetails_dept) {
        this.staffDetails_dept = staffDetails_dept;
    }

    public String getStaffDetails_photourl() {
        return staffDetails_photourl;
    }

    public void setStaffDetails_photourl(String staffDetails_photourl) {
        this.staffDetails_photourl = staffDetails_photourl;
    }

    public String getStaffDetails_extemail() {
        return staffDetails_extemail;
    }

    public void setStaffDetails_extemail(String staffDetails_extemail) {
        this.staffDetails_extemail = staffDetails_extemail;
    }

    public String getStaffDetails_employeeType() {
        return staffDetails_employeeType;
    }

    public void setStaffDetails_employeeType(String staffDetails_employeeType) {
        this.staffDetails_employeeType = staffDetails_employeeType;
    }

    public String getStaffDetails_location() {
        return staffDetails_location;
    }

    public void setStaffDetails_location(String staffDetails_location) {
        this.staffDetails_location = staffDetails_location;
    }

    public String getStaffDetails_intphone() {
        return staffDetails_intphone;
    }

    public void setStaffDetails_intphone(String staffDetails_intphone) {
        this.staffDetails_intphone = staffDetails_intphone;
    }

    public String getStaffDetails_postaladdress() {
        return staffDetails_postaladdress;
    }

    public void setStaffDetails_postaladdress(String staffDetails_postaladdress) {
        this.staffDetails_postaladdress = staffDetails_postaladdress;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }
}
