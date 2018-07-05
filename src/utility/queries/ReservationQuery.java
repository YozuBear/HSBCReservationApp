package utility.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import rest.domain.Desk;
import rest.domain.Employee;
import utility.DBUtility;

/**
 *
 * @author yozubear
 */
public class ReservationQuery {

    /**
     * Client specifies preferred time and location, get back an array of
     * available desks
     *
     * @param startTime - reservation start time
     * @param endTime - reservation end time
     * @param building - building to search
     * @param floor - preferred floor
     * @param section - preferred section
     * @return an array of available desks during preferred time at specified
     * section, empty if none available
     * @throws java.sql.SQLException
     */
    public static List<Desk> getAvailableDesks(Timestamp startTime, Timestamp endTime, String building, int floor, String section) throws SQLException {
        List<Desk> desks = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement("select * from desk where " + "location=? AND floor=? AND section=? AND number NOT IN " + "(select desk_id from deskreservation where " + "((start_datetime <?) AND (end_datetime>?)) OR (start_datetime>=? AND start_datetime<?))");
            ps.setString(1, building);
            ps.setInt(2, floor);
            ps.setString(3, section);
            ps.setTimestamp(4, startTime);
            ps.setTimestamp(5, startTime);
            ps.setTimestamp(6, startTime);
            ps.setTimestamp(7, endTime);
            rs = ps.executeQuery();
            while (rs.next()) {
                Desk desk = new Desk();
                desk.setDeskID(rs.getString("number"));
                desk.setBuilding(rs.getString("location"));
                desks.add(desk);
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for available desks");
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
        return desks;
    }

    /**
     * Check whether Employee id exist in database
     *
     * @param emp - the Employee object trying to reserve
     * @return whether the given employeeID exists
     * @throws java.sql.SQLException
     */
    public static boolean isEmployee(Employee emp) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT EXISTS (SELECT 1 FROM employee WHERE id = ?)";
        int count = 0;
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement(query);
            String id = emp.getId();
            ps.setString(1, id);
            System.out.println(ps);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new SQLException("Invalid input for checking employee");
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
        return false;
    }


    public static boolean isBookingOverlapped(String empID, Desk desk, Timestamp startTime, Timestamp endTime, Timestamp oldStartTime) throws SQLException {
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        ResultSet rs = null;
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement("Select COUNT(*) from deskreservation where start_datetime != ? AND (start_datetime < ? AND ? < end_datetime)" + 
                                                    "AND ((desk_id=? AND location_name =?) OR employee = ? )");
            ps.setString(4, desk.getDeskID());
            ps.setString(5, desk.getBuilding());
            ps.setTimestamp(1, oldStartTime);
            ps.setTimestamp(2, endTime);
            ps.setTimestamp(3, startTime);
            
            ps.setString(6, empID);
            rs = ps.executeQuery();
            
            rs.last();
            int result = rs.getInt("COUNT(*)");
            rs.beforeFirst();

            if (result == 0) {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for checking if bookings overlap");
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
        return true;
    }

    /**
     * Reserve desk: change the entry status from "hold" to "reserve" in
     * database
     *
     * @param EmployeeID
     * @param desk
     * @param startTime
     * @param endTime
     * @throws java.sql.SQLException
     * @precondition - isEmployeeHoldsDesk == true
     */
    public static void reserveDesk(String EmployeeID, Desk desk, Timestamp startTime, Timestamp endTime) throws SQLException {
        PreparedStatement ps = null;
        String desk_id = desk.getDeskID();
        String location_name = desk.getBuilding();
        String insQuery = "UPDATE deskreservation SET create_datetime=NOW(), status='reserved' WHERE " + "employee=? AND desk_id=? AND location_name=? AND start_datetime=? AND end_datetime=?";
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement(insQuery);
            ps.setString(1, EmployeeID);
            ps.setString(2, desk_id);
            ps.setString(3, location_name);
            ps.setTimestamp(4, startTime);
            ps.setTimestamp(5, endTime);
            System.out.println(ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for reserving a desk");
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                }
            }
        }
    }

    /**
     * Put the desk on hold
     *
     * @param startTime
     * @param endTime
     * @param employee
     * @param desk
     * @throws java.sql.SQLException
     * @precondition - isDeskAvailable == true (checked in controller)
     */
    public static void holdDesk(Timestamp startTime, Timestamp endTime, Employee employee, Desk desk) throws SQLException {
        PreparedStatement ps = null;
        String id = employee.getId();
        String desk_id = desk.getDeskID();
        String location_name = desk.getBuilding();
        String query = "INSERT deskreservation (employee, desk_id, location_name, status, create_datetime, start_datetime, end_datetime) VALUES " + "(?, ?, ?, 'onhold', NOW(), ?, ?)";
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, id);
            ps.setString(2, desk_id);
            ps.setString(3, location_name);
            ps.setTimestamp(4, startTime);
            ps.setTimestamp(5, endTime);
            System.out.println(ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for holding a desk");
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                }
            }
        }
    }

    /**
     * Return true if inquired desk's status is not on hold or reserved during
     * given period
     *
     * @param desk
     * @param startTime
     * @param endTime
     * @return whether desk is available for hold
     * @throws java.sql.SQLException
     */
    public static boolean isDeskAvailable(Desk desk, Timestamp startTime, Timestamp endTime) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int result;
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement("select COUNT(*) from deskreservation where desk_id=? " + "AND (((start_datetime <?) AND (end_datetime>?)) OR (start_datetime>=? AND start_datetime<?))");
            ps.setString(1, desk.getDeskID());
            ps.setTimestamp(2, startTime);
            ps.setTimestamp(3, startTime);
            ps.setTimestamp(4, startTime);
            ps.setTimestamp(5, endTime);
            rs = ps.executeQuery();
            rs.last();
            result = rs.getInt("COUNT(*)");
            rs.beforeFirst();
            if (result > 0) {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for desk availability");
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
        return true;
    }

    /**
     * Checks if the given desk is held by the employee during the given time
     * frame
     *
     * @param emp - employee object
     * @param desk
     * @param startTime
     * @param endTime
     * @return true if desk is held by given employee, false otherwise
     * @throws java.sql.SQLException
     */
    public static boolean isEmployeeHoldsDesk(Employee emp, Desk desk, Timestamp startTime, Timestamp endTime) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String EmployeeID = emp.getId();
        String DeskID = desk.getDeskID();
        int result;
        try {
            if (!isEmployee(emp)) {
                throw new NoSuchElementException("Employee does not exist");
            } else {
                Connection connection = DBUtility.getConnection();
                ps = connection.prepareStatement("select COUNT(*) from deskreservation where desk_id=? " + "AND start_datetime=? AND end_datetime=? " + "AND employee=? AND status='onhold'");
                ps.setString(1, DeskID);
                ps.setTimestamp(2, startTime);
                ps.setTimestamp(3, endTime);
                ps.setString(4, EmployeeID);
                rs = ps.executeQuery();
                rs.last();
                result = rs.getInt("COUNT(*)");
                rs.beforeFirst();
                if (result > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for checking employee");
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
        return false;
    }

    /**
     * Same employee cannot reserve for more than 1 desk over the same period
     *
     * @param employeeID - the employee ID of Employee in question
     * @param startTime - the start time of proposed reservation
     * @param endTime - the end time of proposed reservation
     * @return true if the employee already has a reservation during the
     * specified period (including all kinds of overlaps)
     * @throws java.sql.SQLException
     */
    public static boolean isReserveTimeOverlapped(String employeeID, Timestamp startTime, Timestamp endTime) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement("select COUNT(*) from deskreservation where employee=? " + "AND (((start_datetime <?) AND (end_datetime>?)) OR (start_datetime>=? AND start_datetime<?))");
            ps.setString(1, employeeID);
            ps.setTimestamp(2, startTime);
            ps.setTimestamp(3, startTime);
            ps.setTimestamp(4, startTime);
            ps.setTimestamp(5, endTime);
            rs = ps.executeQuery();
            rs.last();
            int result = rs.getInt("COUNT(*)");
            rs.beforeFirst();
            if (result == 0) {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for time overlapped");
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
        return true;
    }

    //--------------------------------------------------------------------------
    //
    //      Reservation Page
    //
    //--------------------------------------------------------------------------
    /**
     * Return whether the employee has reserved over the reserveLimit
     *
     * @param employeeID - the employee ID of Employee in question
     * @param reserveLimit - reservation limit (e.g. 10)
     * @return boolean indicating if reserve limit of the employee has been
     * reached
     * @throws java.sql.SQLException
     */
    public static boolean isOverReserved(String employeeID, int reserveLimit) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement("select COUNT(*) from deskreservation where employee=?");
            ps.setString(1, employeeID);
            rs = ps.executeQuery();
            rs.last();
            int result = rs.getInt("COUNT(*)");
            rs.beforeFirst();
            if (result >= reserveLimit) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for over reserved");
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
        return false;
    }

    /**
     * Change the reserved desk status from "hold" to available
     *
     * @param startTime
     * @param endTime
     * @param employee
     * @param desk
     * @throws java.sql.SQLException
     */
    public static void deleteHold(Timestamp startTime, Timestamp endTime, Employee employee, Desk desk) throws SQLException, NoSuchElementException {
        // e.g. if hold entry does not exist or its status isn't "hold"
        PreparedStatement ps = null;
        String id = employee.getId();
        String desk_id = desk.getDeskID();
        String query = "DELETE FROM deskreservation where desk_id=? AND start_datetime=? AND end_datetime=? " + "AND employee=?AND status='onhold'";
        try {
            if (!isEmployeeHoldsDesk(employee, desk, startTime, endTime)) {
                throw new NoSuchElementException("No such hold entry exist");
            }
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, desk_id);
            ps.setTimestamp(2, startTime);
            ps.setTimestamp(3, endTime);
            ps.setString(4, id);
            System.out.println(ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for deleting a hold");
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                }
            }
        }
    }

    /**
     * Add employee to the database Doesn't consider the case when employee info
     * changed
     *
     * @param emp employee to be added
     * @throws java.sql.SQLException
     */
    public static void addEmployee(Employee emp) throws SQLException{
        PreparedStatement ps = null;
        PreparedStatement insPS = null;
        ResultSet rs = null;
        String id = emp.getId();
        String name = emp.getName();
        String dept = emp.getDept();
        String phoneNum = emp.getPhoneNum();
        String email = emp.getEmail();
        String query = "SELECT COUNT(*) FROM employee WHERE id = ?";
        String insQuery = "INSERT INTO employee values (?, ?, ?, ?, ?)";
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, id);
            rs = ps.executeQuery();
            rs.last();
            int result = rs.getInt("COUNT(*)");
            rs.beforeFirst();
            if (result <= 0) {
                // add employee
                insPS = connection.prepareStatement(insQuery);
                insPS.setString(1, id);
                insPS.setString(2, name);
                insPS.setString(3, dept);
                insPS.setString(4, phoneNum);
                insPS.setString(5, email);
                System.out.println(insPS);
                insPS.executeUpdate();
             }
            else {
                throw new SQLIntegrityConstraintViolationException();
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLIntegrityConstraintViolationException("Employee already exists");
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for adding employee");
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                }
            }
            if (insPS != null) {
                try {
                    insPS.close();
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
