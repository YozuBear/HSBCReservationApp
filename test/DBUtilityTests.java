import java.sql.Timestamp;
import junit.framework.*;
import utility.DBUtility;
/**
 *
 * @author yozubear
 */
public class DBUtilityTests extends TestCase {
    
    @Override
   protected void setUp(){}
   
   // test if time stamp is correct
   public void testGetSQLTimeStamp(){
      String date = "12/21/2014";
      String time = "23:00";
        try {
            Timestamp ts = DBUtility.getSQLTimeStamp(date, time);
            Timestamp expectedTime = Timestamp.valueOf("2014-12-21 23:00:00");
            assertTrue(ts.equals(expectedTime));
        } catch (Exception ex) {
            System.out.print(ex);
        }
   }
   
   public void testGetCurrentTimestamp(){
       Timestamp currentTime = DBUtility.getCurrentTimestamp();
       String timeString = currentTime.toString();
       assertTrue(currentTime != null && !timeString.isEmpty() );
   }
}
