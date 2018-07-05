/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.domain;

/**
 * Filter for Admin to view reservation entries. If all filter fields are empty,
 * then all reservation entries would be displayed.
 * @author yozubear
 */
public class AdminReservationEntries {
    private String employeeID;      // If empty, will display all employee's reservations
    private String building;        // if empty, will display all building's reservation
    private String deskID;          // If empty, will display all desks' reservations
    private String startDate;       // If empty, will be set to 00/00/00 to display all entries
    private String startTime;       // If empty, will be set to 00:00 to display all entries
    private String endDate;         // If empty, will be set to current date to display all entries
    private String endTime;         // If empty, will be set to current time to display all entries

    public AdminReservationEntries(){}

    public AdminReservationEntries(String employeeID, String building, String deskID, String startDate, String startTime, String endDate, String endTime) {
        this.employeeID = employeeID;
        this.building = building;
        this.deskID = deskID;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getDeskID() {
        return deskID;
    }

    public void setDeskID(String deskID) {
        this.deskID = deskID;
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

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }
}
