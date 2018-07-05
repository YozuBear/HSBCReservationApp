package utility.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import rest.domain.Desk;
import rest.domain.DeskSelection;
import rest.domain.Employee;
import rest.domain.ReservationBase;
import utility.DBUtility;

/**
 *
 * @author yozubear
 */
public class AdminReservationQuery {

    /**
     * view all reservations filtered by following fields. If the fields are
     * empty, return all reservations
     *
     * @param employeeID - the ID of employee who made the reservation e.g.
     * 13223432
     * @param desk - the desk the employee reserves
     * @param startTime - the reservation start time, find all reservation
     * entries made after this time
     * @param endTime - the reservation end time, find all reservation entries
     * made before this end time
     * @return Array of reservations (i.e. DeskSelection) filtered by the
     * fields.
     * @throws java.sql.SQLException
     */
    public static List<DeskSelection> viewDeskSelection(String employeeID, Desk desk, Timestamp startTime, Timestamp endTime) throws SQLException {
        // check if strings are empty, means no filter for empty fields
        // startTime and endTime already set at controller to default values if empty
        // no start time filter == 1969-12-31 16:00:00.0
        // no end time filter == 9999-01-01 00:00:00.0
        List<DeskSelection> listOfDeskSelections = new ArrayList<>();
        System.out.println("Query:" + startTime);
        System.out.println("Query:" + endTime);
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM deskreservation " + "JOIN employee ON deskreservation.employee = employee.id " + "WHERE status='reserved' AND desk_id LIKE ? AND " + "location_name LIKE ? AND employee LIKE ? AND " + "( start_datetime >= ? AND end_datetime <= ?)";
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, desk.getDeskID() + "%");
            ps.setString(2, desk.getBuilding() + "%");
            ps.setString(3, employeeID + "%");
            ps.setTimestamp(4, startTime);
            ps.setTimestamp(5, endTime);
            System.out.println(ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                Desk d = new Desk();
                d.setBuilding(rs.getString("location_name"));
                d.setDeskID(rs.getString("desk_id"));
                Employee emp = new Employee();
                emp.setDept(rs.getString("emp_dept"));
                emp.setEmail(rs.getString("email"));
                emp.setId(rs.getString("id"));
                emp.setName(rs.getString("emp_name"));
                emp.setPhoneNum(rs.getString("phoneNum"));
                ReservationBase rbase = new ReservationBase();
                rbase.setEmployee(emp);
                String sDate = new SimpleDateFormat("yyyy-MM-dd").format(rs.getTimestamp("start_datetime"));
                String eDate = new SimpleDateFormat("yyyy-MM-dd").format(rs.getTimestamp("end_datetime"));
                System.out.println(sDate);
                System.out.println(eDate);
                String sTime = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(rs.getTimestamp("start_datetime"));
                String eTime = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(rs.getTimestamp("end_datetime"));
                System.out.println(sTime);
                System.out.println(eTime);
                rbase.setStartDate(sDate);
                rbase.setStartTime(sTime);
                rbase.setEndDate(eDate);
                rbase.setEndTime(eTime);
                DeskSelection deskSel = new DeskSelection();
                deskSel.setDesk(d);
                deskSel.setBase(rbase);
                listOfDeskSelections.add(deskSel);
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for filtering reservations");
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
        }
        return listOfDeskSelections;
    }

    /**
     * delete an existing reservation entry
     *
     * @param reserveEntry - the reservation to be deleted
     * @throws java.sql.SQLException
     */
    public static void deleteReservationEntry(DeskSelection reserveEntry) throws SQLException, NoSuchElementException {
        String deskID = reserveEntry.getDesk().getDeskID();
        String deskLoc = reserveEntry.getDesk().getBuilding();
        String emp = reserveEntry.getBase().getEmployee().getId();
        String startDate = reserveEntry.getBase().getStartDate();
        String startTime = reserveEntry.getBase().getStartTime();
        String endDate = reserveEntry.getBase().getEndDate();
        String endTime = reserveEntry.getBase().getEndTime();
        String startDateTimeString = startDate + " " + startTime;
        String endDateTimeString = endDate + " " + endTime;
        Timestamp startTimestamp = Timestamp.valueOf(startDateTimeString);
        Timestamp endTimestamp = Timestamp.valueOf(endDateTimeString);
        PreparedStatement ps = null;
        PreparedStatement compPS = null;
        ResultSet rs = null;
        String compQuery = "SELECT EXISTS (SELECT 1 FROM deskreservation WHERE " + "desk_id = ? AND location_name = ? AND employee =? AND start_datetime =? AND end_datetime =?)";
        String query = "DELETE FROM deskreservation " + "WHERE desk_id = ? AND location_name = ? AND employee =? AND start_datetime =? AND end_datetime =?";
        int count = 0;
        try {
            Connection connection = DBUtility.getConnection();
            compPS = connection.prepareStatement(compQuery);
            compPS.setString(1, deskID);
            compPS.setString(2, deskLoc);
            compPS.setString(3, emp);
            compPS.setTimestamp(4, startTimestamp);
            compPS.setTimestamp(5, endTimestamp);
            System.out.println(compPS);
            rs = compPS.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            if (count > 0) {
                connection = DBUtility.getConnection();
                ps = connection.prepareStatement(query);
                ps.setString(1, deskID);
                ps.setString(2, deskLoc);
                ps.setString(3, emp);
                ps.setTimestamp(4, startTimestamp);
                ps.setTimestamp(5, endTimestamp);
                System.out.println(ps);
                ps.executeUpdate();
            } else {
                throw new NoSuchElementException("Reservation does not exist");
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for deleting reservation entry");
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                }
            }
            if (compPS != null) {
                try {
                    compPS.close();
                } catch (SQLException ex) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
        }
    }

    //--------------------------------------------------------------------------
    //
    //      Admin Page - Modify Reservations
    //
    //--------------------------------------------------------------------------
    /**
     * modify an existing reservation entry
     *
     * @param oldEntry - the old reservation entry
     * @param newEntry - the new reservation entry
     * @throws java.sql.SQLException
     */
    public static void modifyReservationEntry(DeskSelection oldEntry, DeskSelection newEntry) throws SQLException, NoSuchElementException {
        String oldDeskID = oldEntry.getDesk().getDeskID();
        String oldDeskLoc = oldEntry.getDesk().getBuilding();
        String oldEmp = oldEntry.getBase().getEmployee().getId();
        String oldStartDate = oldEntry.getBase().getStartDate();
        String oldStartTime = oldEntry.getBase().getStartTime();
        String oldEndDate = oldEntry.getBase().getEndDate();
        String oldEndTime = oldEntry.getBase().getEndTime();
        String oldStartString = oldStartDate + " " + oldStartTime;
        String oldEndString = oldEndDate + " " + oldEndTime;
        Timestamp oldStartTimestamp = Timestamp.valueOf(oldStartString);
        Timestamp oldEndTimestamp = Timestamp.valueOf(oldEndString);
        String newDeskID = newEntry.getDesk().getDeskID();
        String newDeskLoc = newEntry.getDesk().getBuilding();
        String newEmp = newEntry.getBase().getEmployee().getId();
        String newStartDate = newEntry.getBase().getStartDate();
        String newStartTime = newEntry.getBase().getStartTime();
        String newEndDate = newEntry.getBase().getEndDate();
        String newEndTime = newEntry.getBase().getEndTime();
        String startDateTimeString = newStartDate + " " + newStartTime;
        String endDateTimeString = newEndDate + " " + newEndTime;
        Timestamp newStartTimestamp = Timestamp.valueOf(startDateTimeString);
        Timestamp newEndTimestamp = Timestamp.valueOf(endDateTimeString);
        
        if (ReservationQuery.isBookingOverlapped(newEmp, newEntry.getDesk(), newStartTimestamp, newEndTimestamp, oldStartTimestamp)) {
            throw new NoSuchElementException("New reservation period overlaps with other bookings");
        }
        if (!deskAndLocationExists(newEntry.getDesk())) {
            throw new NoSuchElementException("New desk and location entered does not exist");
        }
        Employee nEmp = newEntry.getBase().getEmployee();
        if (!ReservationQuery.isEmployee(nEmp)) {
            ReservationQuery.addEmployee(nEmp);
        }
        PreparedStatement ps = null;
        String query = "UPDATE deskreservation " + "SET desk_id =? , location_name =? , employee =? , start_datetime =? , end_datetime =? " + "WHERE desk_id =? AND location_name =? AND start_datetime =? AND end_datetime =?";
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, newDeskID);
            ps.setString(2, newDeskLoc);
            ps.setString(3, newEmp);
            ps.setTimestamp(4, newStartTimestamp);
            ps.setTimestamp(5, newEndTimestamp);
            ps.setString(6, oldDeskID);
            ps.setString(7, oldDeskLoc);
            ps.setTimestamp(8, oldStartTimestamp);
            ps.setTimestamp(9, oldEndTimestamp);
            System.out.println(ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new SQLException("Invalid input for editing reservation entry");
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                }
            }
        }
    }

    public static boolean deskAndLocationExists(Desk desk) throws SQLException, NoSuchElementException {
        String deskID = desk.getDeskID();
        String loc = desk.getBuilding();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT EXISTS (SELECT 1 FROM desk WHERE number = ? AND location = ?)";
        int count = 0;
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, deskID);
            ps.setString(2, loc);
            System.out.println(ps);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            return count > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new SQLException("Invalid input for checking if reservation desk and location exists");
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
        }
    }
    
}
