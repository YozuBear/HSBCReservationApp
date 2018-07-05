import junit.framework.TestCase;
import utility.Email;
/**
 *
 * @author yozubear
 */
public class EmailTests extends TestCase {
    
    @Override
   protected void setUp(){}
   
   // test by checking mailbox if email has been successfully sent
   public void testSendEmail(){
      try{
          String sendTo = "g4c9@ugrad.cs.ubc.ca";
          String subject = "test send email from EmailTests.java";
          String content = "email content";
          Email.send(sendTo, subject, content);
          
          System.out.print("successfully sent");
      }catch(Exception e){
          fail();
      }
   }
   
}

