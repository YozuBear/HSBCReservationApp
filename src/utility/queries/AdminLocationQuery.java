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
import rest.domain.Location;
import rest.domain.Section;
import utility.DBUtility;

/**
 *
 * @author yozubear
 */
public class AdminLocationQuery {

    /**
     *
     * @return all building names and addresses
     * @throws java.sql.SQLException
     */
    public static List<Location> viewAllBuildings() throws SQLException {
        List<Location> listOfLoc = new ArrayList<>();
        Statement s = null;
        ResultSet rs = null;
        String query = "SELECT * FROM location";
        try {
            Connection connection = DBUtility.getConnection();
            s = connection.createStatement();
            rs = s.executeQuery(query);
            while (rs.next()) {
                Location loc = new Location();
                loc.setOfficename(rs.getString("officename"));
                loc.setAddress(rs.getString("address"));
                listOfLoc.add(loc);
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Error in retrieving locations list");
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
        return listOfLoc;
    }
    //--------------------------------------------------------------------------
    //
    //      Admin Page - Modify Buildings
    //
    //--------------------------------------------------------------------------
    /**
     * Modify building address
     *
     * @param buildingName - the building to modify
     * @param newAddress - the new building address
     * @throws java.sql.SQLException
     */
    public static void modifyBuildingAddress(String buildingName, String newAddress) throws SQLException, NoSuchElementException {
        PreparedStatement ps = null;
        PreparedStatement compPS = null;
        ResultSet rs = null;
        ResultSet compRS = null;
        String compQuery = "SELECT EXISTS (SELECT 1 FROM location WHERE officename = ?)";
        String query = "UPDATE location SET address = ? WHERE officename = ?";
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
                ps = connection.prepareStatement(query);
                ps.setString(1, newAddress);
                ps.setString(2, buildingName);
                System.out.println(ps);
                ps.executeUpdate();
            } else {
                throw new NoSuchElementException("Location does not exist");
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for editing location");
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
    }

    /**
     * Add new building location to database
     *
     * @param buildingName - building name
     * @param address - building address
     * @throws java.sql.SQLException
     */
    public static void addBuilding(String buildingName, String address) throws SQLException {
        PreparedStatement ps = null;
        String query = "INSERT INTO location values (?, ?)";
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, buildingName);
            ps.setString(2, address);
            System.out.println(ps);
            ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLIntegrityConstraintViolationException("Location already exists");
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input adding location");
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
     * Delete building location from database
     *
     * @param buildingName - building name
     * @param address - building address
     * @throws java.sql.SQLException
     */
    public static void deleteBuilding(String buildingName, String address) throws SQLException {
        PreparedStatement ps = null;
        PreparedStatement compPS = null;
        ResultSet rs = null;
        String compQuery = "SELECT EXISTS (SELECT 1 FROM location WHERE officename = ? AND address = ?)";
        String query = "DELETE FROM location WHERE officename = ? AND address = ?";
        try {
            Connection connection = DBUtility.getConnection();
            compPS = connection.prepareStatement(compQuery);
            compPS.setString(1, buildingName);
            compPS.setString(2, address);
            System.out.println(compPS);
            rs = compPS.executeQuery();
            if (rs.next()) {
                connection = DBUtility.getConnection();
                ps = connection.prepareStatement(query);
                ps.setString(1, buildingName);
                ps.setString(2, address);
                System.out.println(ps);
                ps.executeUpdate();
            } else {
                throw new NoSuchElementException("Location does not exist");
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for deleting location");
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
    //      Admin Page - Modify Floor Sections
    //
    //--------------------------------------------------------------------------

    /**
     * Add section to floorSection table
     *
     * @param building
     * @param floor
     * @param section
     * @param mapID
     * @throws java.sql.SQLException
     */
    public static void addSection(String building, int floor, String section, String mapID) throws SQLException {
        PreparedStatement ps = null;
        String query = "INSERT INTO floorSection values (?, ?, ?, ?)";
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, building);
            ps.setInt(2, floor);
            ps.setString(3, section);
            ps.setString(4, mapID);
            System.out.println(ps);
            ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLIntegrityConstraintViolationException("Section already exists for this location");
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for adding section");
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
     * Delete floor section from floorSection table
     *
     * @param building
     * @param floor
     * @param section
     * @throws java.sql.SQLException
     */
    public static void deleteSection(String building, int floor, String section) throws SQLException, NoSuchElementException {
        PreparedStatement ps = null;
        PreparedStatement compPS = null;
        ResultSet rs = null;
        String compQuery = "SELECT EXISTS (SELECT 1 FROM floorSection WHERE section = ?)";
        String query = "DELETE FROM floorSection WHERE building = ? AND floor = ? AND section = ?";
        int count = 0;

        try {
            Connection connection = DBUtility.getConnection();
            compPS = connection.prepareStatement(compQuery);
            compPS.setString(1, section);
            System.out.println(compPS);
            rs = compPS.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
            if (count > 0) {
                connection = DBUtility.getConnection();
                ps = connection.prepareStatement(query);
                ps.setString(1, building);
                ps.setInt(2, floor);
                ps.setString(3, section);
                System.out.println(ps);
                ps.executeUpdate();
            } else {
                throw new NoSuchElementException("Section does not exist");
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for deleting section");
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
    
    /**
     * Check if the given floor and section exists in the floorSection table
     * @param building
     * @param floor
     * @param section
     * @return true if floor section exists
     */
    public static boolean existFloorSection(String building, int floor, String section) throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT COUNT(*) FROM floorSection WHERE building =? AND floor =? AND section =?";
        int count;
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, building);
            ps.setInt(2, floor);
            ps.setString(3, section);
            System.out.println(ps);
            rs = ps.executeQuery();
            rs.last();
            count = rs.getInt("COUNT(*)");
            rs.beforeFirst();
            return count > 0;
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for checking if floor section exist");
        } finally {
            if (ps != null) { try { ps.close(); } catch (SQLException ex) {} }
            if (rs != null) { try { rs.close();} catch (SQLException ex) { } }
        }
    }
    
    
    /**
     * Return all the sections and their associated mapURL of the given building
     * @param buildingName
     * @return 
     */
     public static List<Section> viewAllFloorSections(String buildingName) throws SQLException{
         PreparedStatement ps = null;
         ResultSet rs = null;
         String query = "SELECT * FROM floorSection JOIN mapimage ON floorSection.mapID = mapimage.mapkey WHERE building=?";
         List<Section> sections = new ArrayList();
         try {
             Connection connection = DBUtility.getConnection();
             ps = connection.prepareStatement(query);
             ps.setString(1, buildingName);
             System.out.println(ps);
             rs = ps.executeQuery();
             while (rs.next()) {
                 Section s = new Section();
                 s.setBuilding(rs.getString("building"));
                 s.setFloor(rs.getInt("floor"));
                 s.setMapURL(rs.getString("url"));
                 s.setSection(rs.getString("section"));
                 sections.add(s);
             }
             return sections;
         } catch (SQLException e) {
             System.out.println(e);
             throw new SQLException("Invalid input for viewing all floor sections");
         } finally {
             if (ps != null) { try { ps.close(); } catch (SQLException ex) {} }
             if (rs != null) { try { rs.close();} catch (SQLException ex) { } }
         }
     }
 
    /**
     * Return the mapID of the given building. floor and section
     *
     * @param building
     * @param floor
     * @param section
     * @return
     * @throws java.sql.SQLException
     */
    public static String getFloorSectionMapID(String building, int floor, String section) throws SQLException, NoSuchElementException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT mapID FROM floorSection WHERE building =? AND floor =? AND section =?";
        String mapID = "";
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, building);
            ps.setInt(2, floor);
            ps.setString(3, section);
            System.out.println(ps);
            rs = ps.executeQuery();
            if (rs.next()) {
                mapID = rs.getString("mapID");
            } else {
                throw new NoSuchElementException("MapID for this floor and section does not exist");
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for getting floor section mapID");
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
        return mapID;
    }
    
}
