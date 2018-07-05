package rest.domain;

/**
 * Created by yozubear on 2017-02-07.
 * The base class used by all Reservation-related operations
 */
public class ReservationBase {
    private Employee employee;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;

    public ReservationBase(){}

    public ReservationBase(Employee employee, String startDate, String startTime, String endDate, String endTime) {
        this.employee = employee;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return  "start date: " + startDate +
                "\nstart time: " + startTime +
                "\nend date: " + endDate +
                "\nend time: " + endTime ;
    }
}
