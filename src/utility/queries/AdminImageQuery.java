package utility.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.NoSuchElementException;
import utility.DBUtility;

/**
 *
 * @author yozubear
 */
public class AdminImageQuery {

    /**
     * add mapImage to mapImage table
     *
     * @param key - the key of map
     * @param url - the url that links to the map image
     * @throws java.sql.SQLException
     */
    public static void addMapImage(String key, String url) throws SQLException {
        // throw exception if key already exists
        PreparedStatement ps = null;
        String query = "INSERT INTO mapimage values (?, ?)";
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, key);
            ps.setString(2, url);
            System.out.println(ps);
            ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLIntegrityConstraintViolationException("Map key already exists");
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for adding map image");
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
     * modify the map URL associated with the key
     *
     * @param key - the key of mapImage
     * @param newURL - the new url associated with the mapImage
     * @throws java.sql.SQLException
     */
    public static void modifyMapUrl(String key, String newURL) throws SQLException, NoSuchElementException {
        PreparedStatement ps = null;
        PreparedStatement compPS = null;
        ResultSet rs = null;
        String compQuery = "SELECT EXISTS (SELECT 1 FROM mapimage WHERE mapkey = ?)";
        String query = "UPDATE mapimage SET url = ? WHERE mapkey = ?";
        int count = 0;
        try {
            Connection connection = DBUtility.getConnection();
            compPS = connection.prepareStatement(compQuery);
            compPS.setString(1, key);
            System.out.println(compPS);
            rs = compPS.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            if (count > 0) {
                ps = connection.prepareStatement(query);
                ps.setString(1, newURL);
                ps.setString(2, key);
                System.out.println(ps);
                ps.executeUpdate();
            } else {
                throw new NoSuchElementException("Map key does not exist");
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for editing map url");
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
     * Return the image url from key
     *
     * @param key
     * @return
     * @throws java.sql.SQLException
     */
    public static String getImageURL(String key) throws SQLException, NoSuchElementException {
        // throw exception if key doesn't exist in mapImage table
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT count(url), url FROM mapimage WHERE mapkey = ?";
        int count = 0;
        String imageURL = "";
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, key);
            System.out.println(ps);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            if (count > 0) {
                imageURL = rs.getString("url");
            } else {
                throw new NoSuchElementException("Map key does not exist");
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Invalid input for getting image URL");
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
        return imageURL;
    }
    
}
