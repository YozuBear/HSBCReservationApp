package rest.domain;

/**
 * Created by yozubear on 2017-02-07.
 * HSBC Employee class, compact info extracted from HSBCUser class
 */
public class Employee {
    private String id;
    private String name;
    private String dept;
    private String phoneNum;
    private String email;

    public Employee(){}

    public Employee(String id, String name, String dept, String phoneNum, String email) {
        this.id = id;
        this.name = name;
        this.dept = dept;
        this.phoneNum = phoneNum;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDept() {
        return dept;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "name: " + name + "\n" +
                "department: " + dept + "\n" +
                "phone number: " + phoneNum + "\n" +
                "e-mail: " + email ;
    }
}
