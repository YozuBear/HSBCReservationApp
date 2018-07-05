package utility.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import rest.domain.Desk;
import rest.domain.DeskMap;
import utility.DBUtility;

/**
 *
 * @author yozubear
 */
public class AdminDeskQuery {

    /**
     * Return all desks filtered by following fields. Empty string means no
     * filter for that field
     *
     * @param building
     * @param floor
     * @param section
     * @return array of desks
     * @throws java.sql.SQLException
     */
    public static List<DeskMap> viewDesks(String building, String floor, String section) throws SQLException {
        List<DeskMap> listOfDeskMaps = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM desk JOIN mapimage ON mapimage.mapkey=desk.mapID WHERE location LIKE ? AND "
                + "floor LIKE ? AND section LIKE ?";
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, building + "%");
            ps.setString(2, floor + "%");
            ps.setString(3, section + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                Desk desk = new Desk();
                desk.setDeskID(rs.getString("number"));
                desk.setBuilding(rs.getString("location"));
                DeskMap dm = new DeskMap();
                dm.setDesk(desk);
                dm.setMapUrl(rs.getString("url"));
                listOfDeskMaps.add(dm);
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for filtering desks");
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
        return listOfDeskMaps;
    }

    /**
     * delete the range of deskIDs from given building
     *
     * @param building - the building where the desks reside
     * @param deskIDs - range of deskIDs to be deleted
     * @throws java.sql.SQLException
     */
    public static void deleteDesks(String building, List<String> deskIDs) throws SQLException, NoSuchElementException {
        PreparedStatement ps = null;
        PreparedStatement compPS = null;
        ResultSet rs = null;
        String compQuery = "SELECT EXISTS (SELECT 1 FROM desk WHERE number = ? AND location = ?)";
        String query = "DELETE FROM desk WHERE number = ? AND location = ?";
        List<String> nonExists = new ArrayList<>();
        int count = 0;
        try {
            Connection connection = DBUtility.getConnection();
            compPS = connection.prepareStatement(compQuery);
            for (String deskID : deskIDs) {
                compPS.setString(1, deskID);
                compPS.setString(2, building);
                System.out.println(compPS);
                rs = compPS.executeQuery();
                if (rs.next()) {
                    count = rs.getInt(1);
                }
                if (count == 0) {
                    nonExists.add(deskID);
                }
            }
            ps = connection.prepareStatement(query);
            ps.setString(2, building);
            for (String deskID : deskIDs) {
                ps.setString(1, deskID);
                System.out.println(ps);
                ps.executeUpdate();
            }
            if (nonExists.size() > 0) {
                throw new NoSuchElementException("Desk(s) that does not exist and therefore not deleted: " + nonExists);
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for deleting desk(s)");
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
     * Return the desk object using given desk number
     *
     * @param num - desk number (e.g: 1.A.101)
     * @return desk object if exist
     * @throws java.sql.SQLException
     */
    public static Desk getDeskByNumber(String num) throws SQLException {
        Desk desk = new Desk();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement("select * from desk where number=?");
            ps.setString(1, num);
            rs = ps.executeQuery();
            if (rs.next()) {
                desk.setDeskID(rs.getString("number"));
                desk.setBuilding(rs.getString("location"));
            } else {
                throw new NoSuchElementException("Desk number does not exist");
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Cannot get desk by number");
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
        return desk;
    }

    /**
     * Add the range of desks to database, they share common building, floor
     * section and mapID
     *
     * @param building - common building for all desks
     * @param floor - common floor for all desks
     * @param section - common section for all desks
     * @param mapID - common mapID for all added desks
     * @param deskIDs - the list of desk IDs to be added
     * @throws java.lang.Exception
     */
    public static void addDesks(String building, int floor, String section, String mapID, List<String> deskIDs) throws Exception {
        PreparedStatement ps = null;
        String query = "INSERT INTO desk values (?, ?, ?, ?, ?)";
        List<String> dupDesks = new ArrayList<>();
        String dupDeskID = "";
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(2, building);
            ps.setInt(3, floor);
            ps.setString(4, section);
            ps.setString(5, mapID);
            for (String deskID : deskIDs) {
                try {
                    ps.setString(1, deskID);
                    System.out.println(ps);
                    dupDeskID = deskID;
                    ps.executeUpdate();
                } catch (SQLIntegrityConstraintViolationException e) {
                    System.out.println(e.getMessage());
                    if (e.getMessage().contains("location")) {
                        throw new Exception("Location does not exist");
                    }
                    if (e.getMessage().contains("mapID")) {
                        throw new Exception("Map ID does not exist");
                    }
                    dupDesks.add(dupDeskID);
                }
            }
            if (dupDesks.size() > 0) {
                throw new Exception("Desk(s) already exists: " + dupDesks);
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for adding desk(s)");
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
     * Modify desk
     *
     * @param oldDesk
     * @param newDesk
     * @throws java.sql.SQLException
     */
    public static void editDesk(Desk oldDesk, Desk newDesk) throws SQLException, NoSuchElementException {
        String oldDeskID = oldDesk.getDeskID();
        String oldDeskLoc = oldDesk.getBuilding();
        String newDeskID = newDesk.getDeskID();
        String newDeskLoc = newDesk.getBuilding();
        PreparedStatement ps = null;
        PreparedStatement compPS = null;
        ResultSet rs = null;
        String compQuery = "SELECT EXISTS (SELECT 1 FROM desk WHERE number = ? AND location = ?)";
        String query = "UPDATE desk SET number = ?, location =?" + " WHERE number = ? AND location = ?";
        int count = 0;
        try {
            Connection connection = DBUtility.getConnection();
            compPS = connection.prepareStatement(compQuery);
            compPS.setString(1, oldDeskID);
            compPS.setString(2, oldDeskLoc);
            System.out.println(compPS);
            rs = compPS.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            if (count > 0) {
                connection = DBUtility.getConnection();
                ps = connection.prepareStatement(query);
                ps.setString(1, newDeskID);
                ps.setString(2, newDeskLoc);
                ps.setString(3, oldDeskID);
                ps.setString(4, oldDeskLoc);
                System.out.println(ps);
                ps.executeUpdate();
            } else {
                throw new NoSuchElementException("Desk does not exist");
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for editing desk");
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
     * Returns all desks of given building
     *
     * @return
     * @throws SQLException
     */
    public static List<Desk> getAllDesks() throws SQLException {
        List<Desk> desks = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement("select * from desk");
            rs = ps.executeQuery();
            while (rs.next()) {
                Desk desk = new Desk();
                desk.setDeskID(rs.getString("number"));
                desk.setBuilding(rs.getString("location"));
                desks.add(desk);
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Cannot get all desks");
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

    //--------------------------------------------------------------------------
    //
    //      Admin Page - Modify Desks
    //
    //--------------------------------------------------------------------------
    /**
     * Return the Map ID associated with the desk
     *
     * @param desk
     * @return map ID
     * @throws java.sql.SQLException
     */
    public static String getDeskMapID(Desk desk) throws SQLException, NoSuchElementException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT mapID FROM desk WHERE number =? AND location =?";
        String mapID = "";
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, desk.getDeskID());
            ps.setString(2, desk.getBuilding());
            System.out.println(ps);
            rs = ps.executeQuery();
            if (rs.next()) {
                mapID = rs.getString("mapID");
            } else {
                throw new NoSuchElementException("Desk does not exist");
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for getting desk mapID");
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
    
    /**
     * Set new map ID for the given desk
     * @param desk
     * @param newMapID 
     * @throws java.sql.SQLException 
     */
    public static void setDeskMapID(Desk desk, String newMapID) throws SQLException{
        String deskid = desk.getDeskID();
        String building = desk.getBuilding();
        PreparedStatement ps = null;
        PreparedStatement mapPS = null;
        PreparedStatement compPS = null;
        ResultSet rs = null;
        String mapQuery = "SELECT EXISTS (SELECT 1 FROM mapimage WHERE mapkey=?)";
        String compQuery = "SELECT EXISTS (SELECT 1 FROM desk WHERE number = ? AND location = ?)";
        String query = "UPDATE desk SET mapID=?" + " WHERE number = ? AND location = ?";
        int count = 0;
        try {
            Connection connection = DBUtility.getConnection();
            compPS = connection.prepareStatement(compQuery);
            compPS.setString(1, deskid);
            compPS.setString(2, building);
            System.out.println(compPS);
            rs = compPS.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            if (count > 0) {
                connection = DBUtility.getConnection();
                mapPS = connection.prepareStatement(mapQuery);
                mapPS.setString(1, newMapID);
                System.out.println(mapQuery);
                rs = mapPS.executeQuery();
                if (rs.next()) {
                    count = rs.getInt(1);
                }
                if (count > 0) {
                    connection = DBUtility.getConnection();
                    ps = connection.prepareStatement(query);
                    ps.setString(1, newMapID);
                    ps.setString(2, deskid);
                    ps.setString(3, building);
                    System.out.println(ps);
                    ps.executeUpdate();
                }
                else {
                    throw new NoSuchElementException("MapID does not exist");
                }
            } else {
                throw new NoSuchElementException("Desk does not exist");
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for editing desk");
        } finally {
            if (ps != null) {
                try { ps.close(); } catch (SQLException ex) {} }
            if (mapPS != null) {
                try { mapPS.close();} catch (SQLException ex) {} }           
            if (compPS != null) {
                try { compPS.close();} catch (SQLException ex) {} }
            if (rs != null) {
                try { rs.close(); } catch (SQLException ex) {} }
        }
    }

    
}
