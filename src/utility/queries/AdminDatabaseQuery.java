package utility.queries;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import utility.DBUtility;

/**
 * This class deals with database management 
 * (backup current database and archive inactive booking)
 * @author yozubear
 */
public class AdminDatabaseQuery {
    
    /**
     * Backup current data contained in hsbcbooking.sql
     * Throw exception if unsuccessful
     * @return the file location where the backup file is saved
     * @throws java.lang.Exception
     */
    public static String backupDatabase() throws Exception {
        
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String fileName = "/databaseBackup-" + timeStamp + ".sql";
        PreparedStatement ps = null;
        ResultSet rs = null;
        String pathQuery = "SELECT @@basedir";
        String MySQLPath = "";
        
        //path for backup file
        String dir = "/tmp";
                
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement(pathQuery);
            rs = ps.executeQuery();
            System.out.println(ps);
            
            while (rs.next()){
                MySQLPath = rs.getString(1);
            }
 
            System.out.println("MySQLPath is: " +MySQLPath);
            
            Properties prop = new Properties();
            InputStream in = DBUtility.class.getClassLoader().getResourceAsStream("config.properties");
            prop.load(in);
            
            String user = prop.getProperty("user");
            String password = prop.getProperty("password");
            
            
            //backup command
            String[] cmdArray = new String[6];
            cmdArray[0] = MySQLPath + "/bin/mysqldump";
            cmdArray[1] = "-u"+user;
            cmdArray[2] = "-p"+password;
            cmdArray[3] = "hsbcbooking";
            cmdArray[4] = "-r";
            cmdArray[5] = dir+fileName;
            
            System.out.println(Arrays.toString(cmdArray));
            
            Process exec = Runtime.getRuntime().exec(cmdArray);
                
            if(exec.waitFor() == 0){
                 //normally terminated, a way to read the output
                InputStream inputStream = exec.getInputStream();
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);

                String str = new String(buffer);
                System.out.println(str);
            }
            else
            {
                // abnormally terminated, there was some problem
                //a way to read the error during the execution of the command
                InputStream errorStream = exec.getErrorStream();
                byte[] buffer = new byte[errorStream.available()];
                errorStream.read(buffer);

                String str = new String(buffer);
                System.out.println(str);
                
                throw new Exception(str);
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
        return dir+fileName;
    }
    
    /**
     * Archive the inactive bookings (bookings in the past)
     * Throw exception if unsuccessful
     * @throws java.sql.SQLException
     */
    public static void archiveInactiveBookings() throws SQLException{
        
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        String query = "INSERT INTO archive(emp_id, desk_id, location_name, create_datetime, start_datetime, end_datetime, archive_datetime) " +
                       "SELECT employee, desk_id, location_name, create_datetime, start_datetime, end_datetime, NOW() " +
                       "FROM deskreservation WHERE end_datetime < now();";
        
        String query2 = "DELETE FROM deskreservation WHERE end_datetime < now();";
        
        try {
            Connection connection = DBUtility.getConnection();
            ps = connection.prepareStatement(query);
            System.out.println(ps);
            ps.executeUpdate();
            System.out.println(ps2);
            ps2 = connection.prepareStatement(query2);
            ps2.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLIntegrityConstraintViolationException("Bookings already exist in archive table");
        } catch (SQLException e) {
            System.out.println(e);
            throw new SQLException("Error with offloading inactive bookings");
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                }
            }
            if (ps2 != null) {
                try {
                    ps2.close();
                } catch (SQLException ex) {
                }
            }
        }
        
    }
    
}
