package utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Properties;
import java.util.TimeZone;

/**
 * Database Utility Class bridging backend controllers to MySQL
 * 
 */
public class DBUtility {
 private static Connection connection = null;

    /**
     * Return a Connection to mySQL schema. Create a new connection
     * if none exists, property file (config.properties) for 
     * connecting to database is stored under src.
     * @return Connection
     */
   public static Connection getConnection() {
        if (connectionIsOpen())
            return connection;
        else {
            try {
             Properties prop = new Properties();
                InputStream inputStream = DBUtility.class.getClassLoader().getResourceAsStream("config.properties");
                prop.load(inputStream);
                String driver = prop.getProperty("driver");
                String url = prop.getProperty("url");
                String user = prop.getProperty("user");
                String password = prop.getProperty("password");
                Class.forName(driver);
                connection = DriverManager.getConnection(url, user, password);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return connection;
        }

    }
    
    private static boolean connectionIsOpen() {
        try {
            return connection != null && !connection.isClosed();
        }catch(Exception e) {
            return false;
        }
    }
    
    /**
     * Return Timestamp in SQL standard format for querying from 
     * date and time strings.
     * @precondition - date and time strings must be formated as specified below
     * @param date - in the format of mm/dd/yyyy
     * @param time - in the format of hh:00 (e.g. 00:00 to 23:00),
     *               note: minutes are always 0, hours are 24-hr clock
     * @return java.sql.Timestamp
     */
    public static Timestamp getSQLTimeStamp(String date, String time) throws Exception{
        // Check correct date format: mm/dd/yyyy
        date = date.trim();
        if (!date.matches("\\d{2}/\\d{2}/\\d{4}")) {
            throw new Exception("Improper date format, should be: mm/dd/yyyy");
        }
        
        // Check correct time format: hh:00
        time = time.trim();
        if (!time.matches("([0-9]|0[0-9]|1[0-9]|2[0-3]):00")) {
            throw new Exception("Improper time format, should be: hh:00");
        }
        
        // Set dates
        String[] dateArr = date.split("/");
        assert dateArr.length == 3;
        int year = Integer.parseInt(dateArr[2]);
        int month = Integer.parseInt(dateArr[0]);
        int dateInt = Integer.parseInt(dateArr[1]);
        
        // Set Time
        String[] timeArr = time.split(":");
        assert timeArr.length == 2;
        int hour = Integer.parseInt(timeArr[0]);
        Timestamp ts = Timestamp.valueOf(String.format("%04d-%02d-%02d %02d:00:00", 
                                                year, month, dateInt, hour));
        return ts;
    }
    
    /**
     * Return Timestamp in SQL standard format for querying from date string
     * @precondition - date must be formated as specified below
     * @param date - in the format of mm/dd/yyyy
     * @return java.sql.Timestamp
     */
    public static Timestamp getSQLDate(String date) throws Exception{
        // Check correct date format: mm/dd/yyyy
        date = date.trim();
        if (!date.matches("\\d{2}/\\d{2}/\\d{4}")) {
            throw new Exception("Improper date format, should be: mm/dd/yyyy");
        }
        
        // Set dates
        String[] dateArr = date.split("/");
        assert dateArr.length == 3;
        int year = Integer.parseInt(dateArr[2]);
        int month = Integer.parseInt(dateArr[0]);
        int dateInt = Integer.parseInt(dateArr[1]);
        Timestamp ts = Timestamp.valueOf(String.format("%04d-%02d-%02d 00:00:00", 
                                                year, month, dateInt));
        return ts;
    }
    
    public static Timestamp getCurrentTimestamp(){
        // aws's time stamp is faster by 7 hrs...
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT-8:00"));
        long currentTime = (long)(calendar.getTimeInMillis() - 2.52*(Math.pow(10, 7)));
        Timestamp currentTimestamp = new java.sql.Timestamp(currentTime);
       
        return currentTimestamp;
    }
    
    /**
     * Return true if end time is after start time
     * @param start
     * @param end
     * @return 
     */
    public static boolean occursAfter(Timestamp start, Timestamp end){
        int milliSecondsDifference = (int)(end.getTime() - start.getTime());
        return milliSecondsDifference > 0;
    }
    
    // Given two timestamp, return the different in day
    // Negative if end happens before start
    public static int getDayDifference(Timestamp start, Timestamp end){
        
        int milliSecondsDifference = (int)(end.getTime() - start.getTime());
        int dayDifference = milliSecondsDifference / (24*60*60*1000);
        
        return dayDifference;
    }

}