package utility.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import utility.DBUtility;

/**
 *
 * @author yozubear
 */
public class AdminQuery {

    /**
     *
     * @return all admins, empty if array if none
     * @throws java.sql.SQLException
     */
    public static List<String> viewAdmins() throws SQLException {
        List<String> adminList = new ArrayList<>();
        Statement s = null;
        ResultSet rs = null;
        String query = "SELECT * FROM admin";
        try {
            Connection connection = DBUtility.getConnection();
            s = connection.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                adminList.add(rs.getString("adminid"));
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Error in retrieving admin list");
        } finally {
            if (s != null) {
                try {
                    s.close();
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
        return adminList;
    }

    //--------------------------------------------------------------------------
    //
    //      Admin Page
    //
    //--------------------------------------------------------------------------
    /**
     *
     * @param employeeID - the employee ID of an Employee obj
     * @return whether the given employee is an admin
     * @throws java.sql.SQLException
     */
    public static boolean isAdmin(String employeeID) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT EXISTS (SELECT 1 FROM admin WHERE adminid = ?)";
        int count = 0;
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, employeeID);
            System.out.println(ps);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            return count > 0;
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for verifying if user is Admin");
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

    /**
     * Add admin to the database
     *
     * @param employeeID of admin to be added
     * @throws java.sql.SQLException
     */
    public static void addAdmin(String employeeID) throws SQLException {
        PreparedStatement ps = null;
        String query = "INSERT INTO admin values (?)";
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, employeeID);
            System.out.println(ps);
            ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLIntegrityConstraintViolationException("Employee already an admin");
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for adding admin");
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
     * Delete admin from the database
     *
     * @param employeeID - employeeID of the admin to be deleted
     */
    public static void deleteAdmin(String employeeID) throws NoSuchElementException {
        PreparedStatement ps = null;
        String query = "DELETE FROM admin WHERE adminid = ?";
        try {
            if (isAdmin(employeeID)) {
                Connection connection = DBUtility.getConnection();
                ps = connection.prepareStatement(query);
                ps.setString(1, employeeID);
                System.out.println(ps);
                ps.executeUpdate();
            } else {
                throw new NoSuchElementException("Employee not an admin");
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                }
            }
        }
    }
    
}
