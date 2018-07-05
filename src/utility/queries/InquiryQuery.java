package utility.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import rest.domain.Desk;
import rest.domain.DeskSelection;
import rest.domain.Employee;
import rest.domain.FloorsSections;
import rest.domain.HSBCUser;
import rest.domain.ReservationBase;
import rest.domain.Section;
import utility.DBUtility;

/**
 *
 * @author yozubear
 */
public class InquiryQuery {

    /**
     * Return a dictionary with section key mapping to their image url
     * @return section map
     * @throws java.sql.SQLException
     */
    public static Map<String, String> getSectionMapsDictionary() throws SQLException{
        Map<String, String> dic = new HashMap<>();
         PreparedStatement ps = null;
         ResultSet rs = null;
         String query = "SELECT * FROM floorSection JOIN mapimage ON floorSection.mapID = mapimage.mapkey";
         String key;
         String value;
         try {
             Connection connection = DBUtility.getConnection();
             ps = connection.prepareStatement(query);
             System.out.println(ps);
             rs = ps.executeQuery();
             while (rs.next()) {
                 key = rs.getString("building") + "." 
                         + Integer.toString(rs.getInt("floor")) + "." + rs.getString("section");
                 System.out.println(key);
                 value = rs.getString("url");
                 dic.put(key, value);
             }
         } catch (SQLException e) {
             System.out.println(e);
             throw new SQLException("Invalid input for section dictionary");
         } finally {
             if (ps != null) { try { ps.close(); } catch (SQLException ex) {} }
             if (rs != null) { try { rs.close();} catch (SQLException ex) { } }
         }
        return dic;
    }
    
    /**
     * Return all floors and their associated sections of the given building in the form of map
     * @param buildingName
     * @return
     * @throws SQLException 
     */
    public static FloorsSections getAllFloorSections(String buildingName) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String floorquery = "select DISTINCT(floor) from floorSection where building=?";
        String query = "SELECT * FROM floorSection WHERE building=?";
        FloorsSections fs;
        Map<Integer, List<String>> floors = new HashMap<Integer, List<String>>();
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement(floorquery);
            ps.setString(1, buildingName);
            rs = ps.executeQuery();
            int floor;
            String section;
            connection = DBUtility.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, buildingName);
            rs = ps.executeQuery();
            
            // Add sections to floors
            while (rs.next()) {
                floor = rs.getInt("floor");
                section = rs.getString("section");
                if(!floors.containsKey(floor)){
                    List<String> sections = new ArrayList<>();
                    sections.add(section);
                    floors.put(floor, sections);
                }else{
                    List<String> sections = floors.get(floor);
                    sections.add(section);
                }
            }
            fs = new FloorsSections(floors);
        
        } finally {
            if (ps != null) { try { ps.close(); } catch (SQLException ex) {} }
            if (rs != null) { try { rs.close();} catch (SQLException ex) { } }
        }
        return fs;
    }
    
    /**
     * Return all floors of given building to display floor options on UI
     *
     * @param buildingName
     * @return
     * @throws java.sql.SQLException
     */
    public static List<Integer> getAllFloors(String buildingName) throws SQLException {
        // Query from the floorSection table
        List<Integer> listOfFloors = new ArrayList<>();
        PreparedStatement ps = null;
        PreparedStatement compPS = null;
        ResultSet compRS = null;
        ResultSet rs = null;
        String compQuery = "SELECT EXISTS (SELECT 1 FROM location WHERE officename = ?)";
        String query = "SELECT DISTINCT floor FROM floorSection WHERE building = ?";
        int count = 0;
        try {
            Connection connection = DBUtility.getConnection();
            compPS = connection.prepareStatement(compQuery);
            compPS.setString(1, buildingName);
            System.out.println(compPS);
            compRS = compPS.executeQuery();
            if (compRS.next()) {
                count = compRS.getInt(1);
            }
            if (count > 0) {
                connection = DBUtility.getConnection();
                ps = connection.prepareStatement(query);
                ps.setString(1, buildingName);
                System.out.println(ps);
                rs = ps.executeQuery();
                while (rs.next()) {
                    listOfFloors.add(rs.getInt("floor"));
                }
            } else {
                throw new NoSuchElementException("Location does not exist");
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Error in retrieving floor list");
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
            if (compRS != null) {
                try {
                    compRS.close();
                } catch (SQLException ex) {
                }
            }
        }
        return listOfFloors;
    }

    /**
     * Return all sections of given building to display section options on UI
     *
     * @param buildingName
     * @return
     * @throws java.sql.SQLException
     */
    public static List<String> getAllSections(String buildingName) throws SQLException {
        // Query from the floorSection table
        List<String> listOfSections = new ArrayList<>();
        PreparedStatement ps = null;
        PreparedStatement compPS = null;
        ResultSet compRS = null;
        ResultSet rs = null;
        String compQuery = "SELECT EXISTS (SELECT 1 FROM location WHERE officename = ?)";
        String query = "SELECT DISTINCT section FROM floorSection WHERE building = ?";
        int count = 0;
        try {
            Connection connection = DBUtility.getConnection();
            compPS = connection.prepareStatement(compQuery);
            compPS.setString(1, buildingName);
            System.out.println(compPS);
            compRS = compPS.executeQuery();
            if (compRS.next()) {
                count = compRS.getInt(1);
            }
            if (count > 0) {
                connection = DBUtility.getConnection();
                ps = connection.prepareStatement(query);
                ps.setString(1, buildingName);
                System.out.println(ps);
                rs = ps.executeQuery();
                while (rs.next()) {
                    listOfSections.add(rs.getString("section"));
                }
            } else {
                throw new NoSuchElementException("Location does not exist");
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Error in retrieving section list");
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
            if (compRS != null) {
                try {
                    compRS.close();
                } catch (SQLException ex) {
                }
            }
        }
        return listOfSections;
    }

    //--------------------------------------------------------------------------
    //
    //      Inquiry Page
    //
    //--------------------------------------------------------------------------
    /**
     * Inquire the availability of a desk on a selected day
     *
     * @param desk - the desk to inquire
     * @param date - the day to inquire
     * @return an array of reservations of the inquired desk on the specified
     * day
     * @throws java.sql.SQLException
     */
    public static List<ReservationBase> inquireDesk(Desk desk, Timestamp date) throws SQLException {
        List<ReservationBase> resBaseList = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String newDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        String query = "SELECT desk_id, employee, start_datetime, end_datetime, id, emp_name, emp_dept, phoneNum, email " +
                "FROM deskreservation JOIN employee ON deskreservation.employee = employee.id " + 
                "WHERE desk_id LIKE ? AND status='reserved' AND ? BETWEEN DATE(start_datetime) AND DATE(end_datetime)" + "ORDER BY start_datetime";

        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, desk.getDeskID());
            ps.setString(2, newDate);
            System.out.println(ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                ReservationBase res = new ReservationBase();
                Employee emp = new Employee();
                emp.setDept(rs.getString("emp_dept"));
                emp.setEmail(rs.getString("email"));
                emp.setId(rs.getString("id"));
                emp.setName(rs.getString("emp_name"));
                emp.setPhoneNum(rs.getString("phoneNum"));
                res.setEmployee(emp);
                res.setStartDate(rs.getDate("start_datetime").toString());
                res.setStartTime(rs.getTime("start_datetime").toString());
                res.setEndDate(rs.getDate("end_datetime").toString());
                res.setEndTime(rs.getTime("end_datetime").toString());
                resBaseList.add(res);
            }
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
        return resBaseList;
    }
    
    //--------------------------------------------------------------------------
    //
    //      Inquiry Page
    //
    //--------------------------------------------------------------------------
    /**
     * Inquire the availability of a desk on a selected day
     *
     * @return an array of employees in the database
     * @throws java.sql.SQLException
     */
    public static List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * from employee";
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Employee emp = new Employee();
                emp.setDept(rs.getString("emp_dept"));
                emp.setEmail(rs.getString("email"));
                emp.setId(rs.getString("id"));
                emp.setName(rs.getString("emp_name"));
                emp.setPhoneNum(rs.getString("phoneNum"));
                employees.add(emp);                
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for view all employees");
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
        return employees;
    }
    
    /**
     * Inquire all reservations made by employee with employee id
     * on the given date
     * @param employeeID the employee id being inquired 
     * @param date
     * @return list of reservation entries by the inquired employee
     * @throws java.sql.SQLException
     */
    public static List<DeskSelection> getReservationByEmployee(String employeeID, Timestamp date) throws SQLException{
        List<DeskSelection> deskSelectionList = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String newDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        System.out.println(newDate);
        String query = "SELECT desk_id, location_name, employee, start_datetime, end_datetime, id, emp_name, emp_dept, phoneNum, email " +
            "FROM deskreservation JOIN employee ON deskreservation.employee = employee.id " +
            "WHERE status = 'reserved' AND employee.id = ? AND ? BETWEEN DATE(start_datetime) AND DATE(end_datetime)";
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, employeeID);
            ps.setString(2, newDate);
            System.out.println(ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                ReservationBase res = new ReservationBase();
                Desk desk = new Desk();
                DeskSelection ds = new DeskSelection();
                Employee emp = new Employee();
                emp.setDept(rs.getString("emp_dept"));
                emp.setEmail(rs.getString("email"));
                emp.setId(rs.getString("id"));
                emp.setName(rs.getString("emp_name"));
                emp.setPhoneNum(rs.getString("phoneNum"));
                res.setEmployee(emp);
                res.setStartDate(rs.getDate("start_datetime").toString());
                res.setStartTime(rs.getTime("start_datetime").toString());
                res.setEndDate(rs.getDate("end_datetime").toString());
                res.setEndTime(rs.getTime("end_datetime").toString());
                desk.setDeskID(rs.getString("desk_id"));
                desk.setBuilding(rs.getString("location_name"));
                ds.setBase(res);
                ds.setDesk(desk);
                deskSelectionList.add(ds);
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for inquiring reservation by employee id");
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
        return deskSelectionList;
    }
    
}
