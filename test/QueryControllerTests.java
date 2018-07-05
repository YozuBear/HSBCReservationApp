import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertTrue;
import rest.domain.Desk;
import rest.domain.DeskMap;
import rest.domain.DeskSelection;
import rest.domain.Location;
import rest.domain.Employee;
import rest.domain.FloorsSections;
import rest.domain.HSBCUser;
import rest.domain.ReservationBase;
import rest.domain.Section;
import utility.DBUtility;
import utility.queries.AdminDatabaseQuery;
import utility.queries.AdminDeskQuery;
import utility.queries.AdminImageQuery;
import utility.queries.AdminLocationQuery;
import utility.queries.AdminQuery;
import utility.queries.AdminReservationQuery;
import utility.queries.InquiryQuery;
import utility.queries.ReservationQuery;

/**
 * Unit tests for Query Controllers
 */
public class QueryControllerTests extends TestCase {
    
   @Override
   protected void setUp(){}
   

        // Test case:
        // function getAllDesks()
        // should return all 1470 initial desks
    public void testGetAllDesks() {
       try {
           List<Desk> allDesks = AdminDeskQuery.getAllDesks();
           for(int i = 0; i < allDesks.size(); ++i) {
               String building = allDesks.get(i).getBuilding();
               String deskId = allDesks.get(i).getDeskID();
               System.out.println(String.format("Building: %1$s, DeskID: %2$s", building, deskId));
           }
            int num = allDesks.size();
            String number = String.valueOf(num);
            System.out.println(String.format("We have %1$s desks available", number));
           assertTrue(allDesks != null && !allDesks.isEmpty());
       } catch(Exception ex) {
           System.out.println(ex);
       }
   }
    
    // Reservation Page
    // Test case:
    // function getDeskByNumber()
    // Get desk by desk id: 1.A.101
    public void testGetDeskByNumber() {
        try {
        String num = "1.A.ggg01";
        Desk desk = AdminDeskQuery.getDeskByNumber(num);
        String deskId = desk.getDeskID();
        String building = desk.getBuilding();
        System.out.println(String.format("GET desk id: %1$s, location: %2$s", deskId, building));
        assertTrue (desk != null);
        } catch (Exception ex) {
                System.out.println(ex);
        }
    }
        
    // Test case:
    // function isOverReserved()
    // Employee 11112222 doesn't have more than 4 reservations
    public void testIsNotOverReserved() {
        try {
            String empid = "11112222";
            int reserveLimit = 10;
            boolean b = ReservationQuery.isOverReserved(empid, reserveLimit);
            String bb = String.valueOf(b);
            System.out.println(String.format("Employee %1$s has too many open reservation? %2$s", empid, bb));
            assertFalse(b);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    // Test case:
    // function isOverReserved()
    // Employee 12345678 have more than 1 reservations
    public void testIsOverReserved() {
        try {
            String empid = "12345678";
            int reserveLimit = 1;
            boolean b = ReservationQuery.isOverReserved(empid, reserveLimit);
            String bb = String.valueOf(b);
            System.out.println(String.format("Employee %1$s has too many open reservation? %2$s", empid, bb));
            assertTrue(b);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
   
   
   // Test case:
   // function isReserveTimeOverlapped()
   // Check time 2017-04-01 07:00:00 - 2017-04-01 08:00:00
   // when user 11112222 doesn't have any active reservations
   public void testReserveTimeNotOverlapped() {
       try {
        Timestamp start = Timestamp.valueOf("2017-04-01 06:00:00");
        Timestamp end = Timestamp.valueOf("2017-04-01 07:00:00");
        String empid = "11112222";
        boolean result = ReservationQuery.isReserveTimeOverlapped(empid, start, end);
        System.out.println(String.format("Is reservation for %1$s overlapped? %2$s", empid, result));
        assertFalse(result);
       } catch (Exception ex) {
           System.out.println(ex);
       }
   }
      // Test case:
   // function isReserveTimeOverlapped()
   // Check time 2017-04-01 07:00:00 - 2017-04-01 08:00:00
   // when user 11112222 doesn't have any active reservations
   public void testReserveTimeIsOverlapped() {
       try {
        Timestamp start = Timestamp.valueOf("2017-04-01 07:00:00");
        Timestamp end = Timestamp.valueOf("2017-04-01 10:00:00");
        String empid = "11112222";
        boolean result = ReservationQuery.isReserveTimeOverlapped(empid, start, end);
        System.out.println(String.format("Is reservation for %1$s overlapped? %2$s", empid, result));
        assertTrue(result);
       } catch (Exception ex) {
           System.out.println(ex);
       }
   }
  
   // Test case:
   // function getAvailableDesks()
    // get all available desks in Vancouver Broadway Green Building, floor 1 section A
   public void testGetAvailableDesks() {
       try {
           Timestamp start = Timestamp.valueOf("2017-04-01 07:00:00");
           Timestamp end = Timestamp.valueOf("2017-04-01 10:00:00");
           List<Desk> desks;
           desks = ReservationQuery.getAvailableDesks(start, end, "Vancouver Broadway Green", 1, "A");
           for(int i = 0; i < desks.size(); ++i) {
               String building = desks.get(i).getBuilding();
               String deskId = desks.get(i).getDeskID();
               System.out.println(String.format("Available Desk is: %1$s, DeskID: %2$s", building, deskId));
           }
           assertTrue(desks != null && !desks.isEmpty());
       } catch (Exception ex) {
           System.out.println(ex);
       }
   }

   // Test case:
   // function isDeskAvailable()
   // desk "1.A.102" should be availble during 2017-04-01 07:00:00 - 2017-04-01 08:00:00
   public void testIsDeskAvailable() {
       try {
           Timestamp start = Timestamp.valueOf("2017-04-01 07:00:00");
           Timestamp end = Timestamp.valueOf("2017-04-01 08:00:00");
           Desk d = new Desk();
           d.setDeskID("1.A.102");
           boolean result = ReservationQuery.isDeskAvailable(d, start, end);
           System.out.println(String.format("Is desk available?? %1$s", result));
           assertTrue(result);
       } catch (Exception ex) {
           System.out.println(ex);
       }
   }
   
   
      public void testAddEmployeeAlreadyExits() {
       try {
           Employee emp = new Employee("12345678", "Robot", "AI", "3333", "robot@ai");
           ReservationQuery.addEmployee(emp);
           assertFalse(true);
      } catch(SQLException | NoSuchElementException e) {
           String msg = "Employee already exists";
           System.out.println(e.getMessage());
           assertEquals(msg, e.getMessage());
       }
   }
   
   public void testEmployeeAdded() {
       try {
           Employee emp = new Employee("87654321", "Robot", "AI", "3333", "robot@ai");
           boolean before = ReservationQuery.isEmployee(emp);
           assertFalse(before);
           ReservationQuery.addEmployee(emp);
           boolean after = ReservationQuery.isEmployee(emp);
           assertTrue(after);
           
           //reset
           // need to delete added employee
       } catch (SQLException | NoSuchElementException e) {
           System.out.println(e.getMessage());
       }
   }
   
          // Test case:
          // function holdDesk()
          // check a desk that is not on hold, put on hold and check if it exists in the reservation table
     public void testHoldDeskDeskNotAvailable() {
       try {
           Timestamp start = Timestamp.valueOf("2017-04-01 07:00:00");
           Timestamp end = Timestamp.valueOf("2017-04-01 08:00:00");
           Desk d = new Desk();
           d.setDeskID("1.A.101");
           d.setBuilding("Vancouver Broadway Green");
           Employee emp = new Employee("11112222","Ellie Liu","Development Team","117781234567", "ELIU@GMAIL.COM");
           ReservationQuery.holdDesk(start, end, emp, d);
       } catch (SQLException | NoSuchElementException e) {
           String msg = "Desk is not available";
           System.out.println(e.getMessage());
           assertEquals(msg, e.getMessage());
       }
    }
     
    public void testHoldDeskEmployeeNotExist() {
       try {
           Timestamp start = Timestamp.valueOf("2017-09-01 07:00:00");
           Timestamp end = Timestamp.valueOf("2017-09-01 09:00:00");
           Desk d = new Desk();
           d.setDeskID("1.A.101");
           d.setBuilding("Vancouver Broadway Green");
           Employee emp = new Employee("13345667","Ellie Liu","Development Team","117781234567", "ELIU@GMAIL.COM");
           ReservationQuery.holdDesk(start, end, emp, d);
       } catch (SQLException | NoSuchElementException e) {
           String msg = "Invalid input for holding a desk";
           System.out.println(e.getMessage());
           assertEquals(msg, e.getMessage());
       }
    }
    
    public void testHoldDesk() {
        try {
           Timestamp start = Timestamp.valueOf("2017-10-01 06:00:00");
           Timestamp end = Timestamp.valueOf("2017-10-01 07:00:00");
           Desk d = new Desk();
           d.setDeskID("1.A.101");
           d.setBuilding("Vancouver Broadway Green");
           Employee emp = new Employee("11112222","Ellie Liu","Development Team","117781234567", "ELIU@GMAIL.COM");
           ReservationQuery.holdDesk(start, end, emp, d);
        } catch (SQLException e) {
           System.out.println(e.getMessage());
        }
    } 
     
    // Inquiry Page
    
    public void testGetSectionMapsDictionary() {
       try {
           Map<String, String> dic = InquiryQuery.getSectionMapsDictionary();
           System.out.println(dic);
           assertTrue(dic.size() == 8);
       } catch (SQLException ex) {
           System.out.println(ex);
           assertFalse(true);
       }
    }
    
    
    // Test cases
    // Reservation Date: 2017-02-01 to 2017-02-03
    // Get reservation from: 2017-02-01, 2017-02-03, 2017-02-02, 2017-01-31, 2017-02-04
   
   public void testInquiryDesk() {
       try {
           Desk deskTest = new Desk("Vancouver Broadway Green", "1.A.102");
           Timestamp timeTest = Timestamp.valueOf("2017-04-01 00:12:12");
           List<ReservationBase>  rb = InquiryQuery.inquireDesk(deskTest, timeTest);
           
           for(int i = 0; i < rb.size(); ++i) {
               String empID = rb.get(i).getEmployee().getId();
               String empName = rb.get(i).getEmployee().getName();
               String empDept = rb.get(i).getEmployee().getDept();
               String empPhone = rb.get(i).getEmployee().getPhoneNum();
               String empEmail = rb.get(i).getEmployee().getEmail();
               
               String startDate = rb.get(i).getStartDate();
               String startTime = rb.get(i).getStartTime();
               String endDate = rb.get(i).getEndDate();
               String endTime = rb.get(i).getEndTime();
               
               System.out.println(String.format("EmployeeID: %1$s, StartDate: %2$s, StartTime: %3$s, EndDate: %4$s, EndTime: %5$s", empID, startDate, startTime, endDate, endTime));
               System.out.println(String.format("EmployeeName: %1$s, Dept: %2$s, Phone: %3$s, Email: %4$s", empName, empDept, empPhone, empEmail));
           }
          assertTrue(rb.size() == 1);
         
       } catch(Exception e) {
           System.out.println(e);
       }
   }
   
   
    // Inquiry Page
    public void testGetAllEmployees() {
       try {
           List<Employee> employees = InquiryQuery.getAllEmployees();
           for (int i = 0; i < employees.size(); i++) {
                String id = employees.get(i).getId();
                String name = employees.get(i).getName();
                String dept = employees.get(i).getDept();
                String phoneNum = employees.get(i).getPhoneNum();
                String email = employees.get(i).getEmail();
                System.out.println(String.format("Welcome our employee %1$s, id is %2$s", name, id));
           }
           int num = employees.size();
           String number = String.valueOf(num);
           System.out.println(number);
           assertTrue(employees != null && !employees.isEmpty());
       } catch (Exception ex) {
           System.out.println(ex);
       }
   }
    
    public void testGetReservationByEmployee() {
       String empid = "43868488";
       Timestamp date = Timestamp.valueOf("2017-04-10 09:00:00");
       try {
           List<DeskSelection>  selections = InquiryQuery.getReservationByEmployee(empid, date);
           for (int i = 0; i < selections.size(); i++) {
                String name = selections.get(i).getBase().getEmployee().getName();
                String et = selections.get(i).getBase().getEndTime();
                String ed = selections.get(i).getBase().getEndDate();
                System.out.println(String.format("Booked by employee %1$s, end time is %2$s %3$s", name, ed, et));
           }
           System.out.println();  
       } catch (Exception ex) {
           System.out.println(ex);
       }
   }
       
    public void testGetAllFloorSection() {
       try {
           FloorsSections fs = InquiryQuery.getAllFloorSections("Vancouver Broadway Green");
           System.out.println("Floor plan is " + fs.getFloors());
           assertTrue(fs.getFloors().size() == 4);
       } catch (SQLException ex) {
           System.out.println(ex);
           assertFalse(true);
       }
   }
    
   // Admin Page
   
   
   public void testIsAdmin(){
       try {
           boolean isEmpAdmin1 = AdminQuery.isAdmin("11112222");
           boolean isEmpAdmin2 = AdminQuery.isAdmin("22221111");
           assertTrue(isEmpAdmin1);
           assertFalse(isEmpAdmin2);
       } catch(Exception e) {
           System.out.println(e);
           assertFalse(true);
       }
   }
   
   //Test
   //function addAdmin
   public void testAddAdminUserAlreadyExits() {
       try {
           AdminQuery.addAdmin("11112222");
           assertFalse(true);
      } catch(SQLException | NoSuchElementException e) {
           String msg = "Employee already an admin";
           System.out.println(e.getMessage());
           assertEquals(msg, e.getMessage());
       }
   }
   
   public void testAddAdminUser() {
       try {

           AdminQuery.addAdmin("78947894");
           boolean adminAdded = AdminQuery.isAdmin("78947894");
           assertTrue(adminAdded);
           
           //reset
           AdminQuery.deleteAdmin("78947894");
           
       } catch (SQLException | NoSuchElementException e) {
           System.out.println(e.getMessage());
           assertFalse(true);
       }
   }
   
   public void testDeleteAdminUserNotExist() {
       try {
           AdminQuery.deleteAdmin("22221111");
       } catch (NoSuchElementException e) {
           String msg = "Employee not an admin";
           System.out.println(e.getMessage());
           assertEquals(msg, e.getMessage());
       }
   }
      
   public void testDeleteAdminUser(){
       
       try {
           
           AdminQuery.deleteAdmin("12345678");
           boolean anAdmin = AdminQuery.isAdmin("12345678");
           assertFalse(anAdmin);
           
           //reset
           AdminQuery.addAdmin("12345678");

       } catch (SQLException | NoSuchElementException ex) {
           System.out.println(ex.getMessage());
           assertFalse(true);
       }
    }
   
   public void testViewAdmins() {
       try {
           List<String> admins = AdminQuery.viewAdmins();
           for(int i = 0; i < admins.size(); ++i) {
               String adminName = admins.get(i);
               
               System.out.println(String.format("Admin Name: %1$s", adminName));
           }
           
           assertTrue(admins.size() >= 1);
       } catch (SQLException ex) {
           assertFalse(true);
       }
   }
   
    
   private DeskSelection modifyReservationEntrySetUp() {
       //Setup oldEntry
           DeskSelection oldDS = new DeskSelection();
           ReservationBase oldRB = new ReservationBase();
           Employee oldEmp = new Employee();
           oldEmp.setId("12345678");
           
           oldRB.setEmployee(oldEmp);
           oldRB.setStartDate("2017-05-22");
           oldRB.setStartTime("12:34:56");
           oldRB.setEndDate("2017-05-24");
           oldRB.setEndTime("21:43:55");
           
           Desk oldDesk = new Desk();
           oldDesk.setDeskID("1.A.110");
           oldDesk.setBuilding("Vancouver Broadway Green");
           
           oldDS.setBase(oldRB);
           oldDS.setDesk(oldDesk);
           
           return oldDS;
   }
   
   public void testModifyReservationEntry() {
       
       try {
           DeskSelection oldDS = modifyReservationEntrySetUp();
           //Setup newEntry
           DeskSelection newDS = new DeskSelection();
           ReservationBase newRB = new ReservationBase();
           Employee newEmp = new Employee();
           newEmp.setId("11112222");
           
           newRB.setEmployee(newEmp);
           newRB.setStartDate("2018-06-22");
           newRB.setStartTime("12:34:56");
           newRB.setEndDate("2018-06-24");
           newRB.setEndTime("21:43:55");
           
           Desk newDesk = new Desk();
           newDesk.setDeskID("2.A.201");
           newDesk.setBuilding("Richmond Westminster Red");
           
           newDS.setBase(newRB);
           newDS.setDesk(newDesk);
           
           AdminReservationQuery.modifyReservationEntry(oldDS, newDS);
           
           Timestamp startTime = Timestamp.valueOf(newRB.getStartDate() + " " + newRB.getStartTime());
           Timestamp endTime = Timestamp.valueOf(newRB.getEndDate() + " " + newRB.getEndTime());
           
           List<DeskSelection> ds = AdminReservationQuery.viewDeskSelection(newEmp.getId(), newDesk, startTime, endTime);
           assertTrue(ds.size()==1);
           
           //reset
           AdminReservationQuery.modifyReservationEntry(newDS, oldDS);
           
       } catch (SQLException | NoSuchElementException ex) {
           System.out.println(ex);
           assertFalse(true);
       }

   }
   
   public void testModifyReservationEntryDeskLocationNotExist(){
       
        try {

           DeskSelection oldDS = modifyReservationEntrySetUp();
           //Setup newEntry
           DeskSelection newDS = new DeskSelection();
           ReservationBase newRB = new ReservationBase();
           Employee newEmp = new Employee();
           newEmp.setId("11112222");
           
           newRB.setEmployee(newEmp);
           newRB.setStartDate("2018-06-22");
           newRB.setStartTime("12:34:56");
           newRB.setEndDate("2018-06-24");
           newRB.setEndTime("21:43:55");
           
           Desk newDesk = new Desk();
           newDesk.setDeskID("99.A.201");
           newDesk.setBuilding("Richmond Westminster Red");
           
           newDS.setBase(newRB);
           newDS.setDesk(newDesk);
           
           AdminReservationQuery.modifyReservationEntry(oldDS, newDS);
                    
           assertFalse(true);
           
       } catch (Exception e) {
           String msg = "New desk and location entered does not exist";
           System.out.println(e.getMessage());
           assertTrue(e.getMessage().contains(msg));
       }
   }
   
   public void testModifyReservationEntryOverlappedEntry(){
        try {

           DeskSelection oldDS = modifyReservationEntrySetUp();
           //Setup newEntry
           DeskSelection newDS = new DeskSelection();
           ReservationBase newRB = new ReservationBase();
           Employee newEmp = new Employee();
           newEmp.setId("11112222");
           
           newRB.setEmployee(newEmp);
           newRB.setStartDate("2017-04-01");
           newRB.setStartTime("12:34:56");
           newRB.setEndDate("2017-04-02");
           newRB.setEndTime("21:43:55");
           
           Desk newDesk = new Desk();
           newDesk.setDeskID("1.A.101");
           newDesk.setBuilding("Vancouver Broadway Green");
           
           newDS.setBase(newRB);
           newDS.setDesk(newDesk);
           
           AdminReservationQuery.modifyReservationEntry(oldDS, newDS);
                    
           assertFalse(true);
           
       } catch (Exception e) {
           String msg = "New reservation period overlaps with other bookings";
           System.out.println(e.getMessage());
           assertTrue(e.getMessage().contains(msg));
       }
   }
   
   public void testIsBookingOverlappedCase1() {
              
       try {
           // case 1  |-----|
           //               |----|
           String building = "Vancouver Broadway Green";
           String newStartDate = "2017-05-01";
           String newEndDate = "2017-05-01";
           String newStartTime = "08:00:00";
           String newEndTime = "11:00:00";
           String startDateTimeString = newStartDate + " " + newStartTime;
           String endDateTimeString = newEndDate + " " + newEndTime;
           
           String oldStartDate = "2016-01-01";
           String oldStartTime = "00:00:00";
           String oldString = oldStartDate + " " + oldStartTime;
           Timestamp old = Timestamp.valueOf(oldString);
           
           Timestamp startTime = Timestamp.valueOf(startDateTimeString);
           Timestamp endTime = Timestamp.valueOf(endDateTimeString);
           
           Desk desk = new Desk();
           desk.setBuilding(building);
           desk.setDeskID("1.B.101");
           
           boolean result = ReservationQuery.isBookingOverlapped("11112222", desk, startTime, endTime, old);
           System.out.println("Booking Overlapped: " + result);
           assertFalse(result);
       } catch (SQLException ex) {
           assertFalse(true);
       }
              
   }
   
   public void testIsBookingOverlappedCase2(){
       
       try {
           // case 2    |---|
           //         |-------|
           
           String building = "Vancouver Broadway Green";
           String newStartDate = "2017-05-02";
           String newEndDate = "2017-05-04";
           String newStartTime = "11:00:00";
           String newEndTime = "16:00:00";
           String startDateTimeString = newStartDate + " " + newStartTime;
           String endDateTimeString = newEndDate + " " + newEndTime;
           
           String oldStartDate = "2017-04-01";
           String oldStartTime = "11:00:00";
           String oldString = oldStartDate + " " + oldStartTime;
           Timestamp old = Timestamp.valueOf(oldString);
           
           Timestamp startTime = Timestamp.valueOf(startDateTimeString);
           Timestamp endTime = Timestamp.valueOf(endDateTimeString);
           
           Desk desk = new Desk();
           desk.setBuilding(building);
           desk.setDeskID("1.B.101");
           
           boolean result = ReservationQuery.isBookingOverlapped("11112222",desk, startTime, endTime, old);
           System.out.println("Booking Overlapped: " + result);
           assertTrue(result);
       } catch (SQLException ex) {
           assertFalse(true);
       }
       
   }
   
   public void testIsBookingOverlappedCase3(){
       
       try {
           // case 3  |-------|
           //           |---|
           
           String building = "Vancouver Broadway Green";
           String newStartDate = "2017-04-30";
           String newEndDate = "2017-05-06";
           String newStartTime = "08:00:00";
           String newEndTime = "11:00:00";
           String startDateTimeString = newStartDate + " " + newStartTime;
           String endDateTimeString = newEndDate + " " + newEndTime;
           
           String oldStartDate = "2016-01-01";
           String oldStartTime = "00:00:00";
           String oldString = oldStartDate + " " + oldStartTime;
           Timestamp old = Timestamp.valueOf(oldString);
           
           Timestamp startTime = Timestamp.valueOf(startDateTimeString);
           Timestamp endTime = Timestamp.valueOf(endDateTimeString);
           
           Desk desk = new Desk();
           desk.setBuilding(building);
           desk.setDeskID("1.B.101");
           
           boolean result = ReservationQuery.isBookingOverlapped("11112222",desk, startTime, endTime, old);
           System.out.println("Booking Overlapped: " + result);
           assertTrue(result);
       } catch (SQLException ex) {
           assertFalse(true);
       }
       
   }
   
   public void testIsBookingOverlappedCase4() {
       
       try {
           // case 4  |-------|
           //             |-------|
           
           String building = "Vancouver Broadway Green";
           String newStartDate = "2017-04-28";
           String newEndDate = "2017-05-03";
           String newStartTime = "09:00:00";
           String newEndTime = "18:00:00";
           String startDateTimeString = newStartDate + " " + newStartTime;
           String endDateTimeString = newEndDate + " " + newEndTime;
           
           String oldStartDate = "2016-01-01";
           String oldStartTime = "00:00:00";
           String oldString = oldStartDate + " " + oldStartTime;
           Timestamp old = Timestamp.valueOf(oldString);
           
           Timestamp startTime = Timestamp.valueOf(startDateTimeString);
           Timestamp endTime = Timestamp.valueOf(endDateTimeString);
           
           Desk desk = new Desk();
           desk.setBuilding(building);
           desk.setDeskID("1.B.101");
           
           boolean result = ReservationQuery.isBookingOverlapped("11112222", desk, startTime, endTime, old);
           System.out.println("Booking Overlapped: " + result);
           assertTrue(result);
       } catch (SQLException ex) {
           assertFalse(true);
       }
       
   }
   
   public void testIsBookingOverlappedCase5() {
       
       try {
           // case 5      |-------|
           //         |------|
           
           String building = "Vancouver Broadway Green";
           String newStartDate = "2017-05-05";
           String newEndDate = "2017-05-08";
           String newStartTime = "10:00:00";
           String newEndTime = "11:00:00";
           String startDateTimeString = newStartDate + " " + newStartTime;
           String endDateTimeString = newEndDate + " " + newEndTime;
           
           String oldStartDate = "2016-01-01";
           String oldStartTime = "00:00:00";
           String oldString = oldStartDate + " " + oldStartTime;
           Timestamp old = Timestamp.valueOf(oldString);
           
           Timestamp startTime = Timestamp.valueOf(startDateTimeString);
           Timestamp endTime = Timestamp.valueOf(endDateTimeString);
           
           Desk desk = new Desk();
           desk.setBuilding(building);
           desk.setDeskID("1.B.101");
           
           boolean result = ReservationQuery.isBookingOverlapped("11112222", desk, startTime, endTime, old);
           System.out.println("Booking Overlapped: " + result);
           assertTrue(result);
       } catch (SQLException ex) {
           assertFalse(true);
       }
       
   }
   
   public void testDeskAndLocationExists() {
       
       try {
           Desk fakeDesk = new Desk();
           fakeDesk.setBuilding("test");
           fakeDesk.setDeskID("1.A.101");
           
           Desk realDesk = new Desk();
           realDesk.setBuilding("Vancouver Broadway Green");
           realDesk.setDeskID("1.A.101");
           
           boolean fake = AdminReservationQuery.deskAndLocationExists(fakeDesk);
           boolean real = AdminReservationQuery.deskAndLocationExists(realDesk);
           
           assertFalse(fake);
           assertTrue(real);
       } catch (SQLException ex) {
           assertFalse(true);
       }
   }
   
   public void testDeleteReservationEntryNotExists(){
       DeskSelection oldDS = new DeskSelection();
       ReservationBase oldRB = new ReservationBase();
       Employee oldEmp = new Employee();
       oldEmp.setId("11112222");
       
       oldRB.setEmployee(oldEmp);
       oldRB.setStartDate("2017-06-02");
       oldRB.setStartTime("03:00:00");
       oldRB.setEndDate("2017-06-05");
       oldRB.setEndTime("09:00:00");
       
       Desk oldDesk = new Desk();
       oldDesk.setDeskID("1.A.111");
       oldDesk.setBuilding("Vancouver Broadway Green");
       
       oldDS.setBase(oldRB);
       oldDS.setDesk(oldDesk);
       
       try {
            AdminReservationQuery.deleteReservationEntry(oldDS);
            assertFalse(true);
       } catch(Exception e) {
           String msg = "Reservation does not exist";
           System.out.println(e.getMessage());
           assertTrue(msg.equals(e.getMessage()));
       }
       
   }
   
   public void testDeleteReservationEntry() {
       
       try {
           // Setup
           DeskSelection oldDS = new DeskSelection();
           ReservationBase oldRB = new ReservationBase();
           Employee oldEmp = new Employee();
           oldEmp.setId("11112222");
           
           oldRB.setEmployee(oldEmp);
           oldRB.setStartDate("2017-06-02");
           oldRB.setStartTime("03:00:00");
           oldRB.setEndDate("2017-06-05");
           oldRB.setEndTime("09:00:00");
           
           Desk oldDesk = new Desk();
           oldDesk.setDeskID("1.A.104");
           oldDesk.setBuilding("Vancouver Broadway Green");
           
           oldDS.setBase(oldRB);
           oldDS.setDesk(oldDesk);
           
           Timestamp startTime = Timestamp.valueOf("2017-06-02 03:00:00");
           Timestamp endTime = Timestamp.valueOf("2017-06-05 09:00:00");
           
           AdminReservationQuery.deleteReservationEntry(oldDS);
           
           List<DeskSelection> ds = AdminReservationQuery.viewDeskSelection(oldEmp.getId(), oldDesk, startTime, endTime);
           
           assertTrue(ds.isEmpty());
           
           //reset
           ReservationQuery.holdDesk(startTime, endTime, oldEmp, oldDesk);
           ReservationQuery.reserveDesk(oldEmp.getId(), oldDesk, startTime, endTime);
       } catch (SQLException ex) {
           assertFalse(true);
       }
   }
   
   public void testViewDeskSelection() {
         Desk desk = new Desk();
         desk.setBuilding("Vancouver Broadway Green");
         desk.setDeskID("3.A.105");
         Timestamp start = Timestamp.valueOf("2017-07-01 09:00:00");
         Timestamp end = Timestamp.valueOf("2017-07-01 16:00:00");
       try {
           List<DeskSelection> res = AdminReservationQuery.viewDeskSelection("12345678", desk, start, end);
           assertTrue(res.size() == 1);
       } catch (SQLException ex) {
           assertFalse(true);
       }
       
   }
   
   public void testExistFloorSection() {
       String building = "Vancouver Broadway Green";
       int floor = 1;
       String section = "A";
       try {
           boolean exist = AdminLocationQuery.existFloorSection(building, floor, section);
           assertTrue(exist);
       } catch (SQLException ex) {
       }       
   }
   
   public void testViewAllFloorSections() {
       try {
           String building = "Vancouver Broadway Green";
           List<Section> sections = AdminLocationQuery.viewAllFloorSections(building);
           assertTrue(sections != null);
           for (int i = 0; i < sections.size(); i++) {
               int floor = sections.get(i).getFloor();
               String section = sections.get(i).getSection();
               String url = sections.get(i).getMapURL();
               System.out.println(String.format("Floor section is %1$s %2$s %3$s", Integer.toString(floor), section, url));
           }
       } catch (SQLException ex) {
       }
   }
   
   
   public void testAddBuilding() {
       try {

            String officeName = "Burnaby Kingsway Blue";
            String officeAddress = "3777 Kingsway, Burnaby, BC V5H 3Z7";
            
            List<Location> buildingsBefore = AdminLocationQuery.viewAllBuildings();

            AdminLocationQuery.addBuilding(officeName, officeAddress);
            List<Location> buildingsAfter = AdminLocationQuery.viewAllBuildings();
            Location loc = new Location();
            loc.setOfficename(officeName);
            loc.setAddress(officeAddress);
            
            assertTrue(buildingsAfter.size() == buildingsBefore.size() + 1);
            
            //Teardown
             AdminLocationQuery.deleteBuilding(officeName, officeAddress);

        } catch (SQLIntegrityConstraintViolationException ex) {
           Logger.getLogger(QueryControllerTests.class.getName()).log(Level.SEVERE, null, ex);
           assertFalse(true);
       } catch (SQLException ex) {
           Logger.getLogger(QueryControllerTests.class.getName()).log(Level.SEVERE, null, ex);
           assertFalse(true);
       }
       
   }
   
   public void testAddBuildingAlreadyExists(){
      String buildingName = "Vancouver Broadway Green";
      String address = "2910 Virtual Way, Vancouver BC, V5M 0B2";
      try {
           AdminLocationQuery.addBuilding(buildingName, address);
           assertFalse(true);
      } catch(SQLException | NoSuchElementException e) {
           String msg = "Location already exists";
           System.out.println(e.getMessage());
           assertEquals(msg, e.getMessage());
       }
   }
   
   public void testDeleteBuilding() {
       try {
           //Setup
           String buildingName = "test should not show";
           String address = "test address should not show";
           try {
               AdminLocationQuery.addBuilding(buildingName, address);
               
           } catch (SQLException ex) {
               Logger.getLogger(QueryControllerTests.class.getName()).log(Level.SEVERE, null, ex);
           }
           List<Location> before = AdminLocationQuery.viewAllBuildings();
           AdminLocationQuery.deleteBuilding(buildingName, address);
           List<Location> after = AdminLocationQuery.viewAllBuildings();
           assertTrue(before.size() ==  after.size() + 1);
       } catch (SQLException ex) {
           assertFalse(true);
       }
   }
   
   public void testModifyBuilding() {
       String buildingName = "Richmond Westminster Red";
       String newAddress = "testing address update shouldn't show up";
       String originalAddress = "5991 Jacombs Rd, Richmond, BC V6V 2C2";
       boolean result = false;
       try {
           AdminLocationQuery.modifyBuildingAddress(buildingName, newAddress);
           
           List<Location> locs = AdminLocationQuery.viewAllBuildings();
           for(int i = 0; i < locs.size(); ++i) {
                String addr = locs.get(i).getAddress();
                if (addr.equals(newAddress)){
                    result = true;
                }
            }
           
           assertTrue(result);
           
           //reset
           AdminLocationQuery.modifyBuildingAddress(buildingName, originalAddress);
       } catch (SQLException ex) {
           assertFalse(true);
       }
   }
   
   public void testModifyBuildingNotExist(){
       String buildingName = "La La Land";
       String newAddress = "throw exception";
       
       try {
           AdminLocationQuery.modifyBuildingAddress(buildingName, newAddress);
           assertFalse(true);
       } catch (Exception e) {
           String msg = "Location does not exist";
           System.out.println(e.getMessage());
           assertTrue(e.getMessage().contains(msg));
       }
   }
   
   public void testViewBuildings(){
       
       try {
           List<Location> rb = AdminLocationQuery.viewAllBuildings();
           
           for(int i = 0; i < rb.size(); ++i) {
               String officeName = rb.get(i).getOfficename();
               String address = rb.get(i).getAddress();
               
               System.out.println(String.format("OfficeName: %1$s, Address: %2$s", officeName, address));
           }
           
           assertTrue(rb.size() >= 1);
       } catch (SQLException ex) {
           assertFalse(true);
       }

   }
   
   public void testGetDeskMapID(){
       try {
           Desk desk = new Desk();
           desk.setBuilding("Vancouver Broadway Green");
           desk.setDeskID("1.A.101");
           String mapID = AdminDeskQuery.getDeskMapID(desk);
           
           assertEquals("1.A.B", mapID);
       } catch (SQLException ex) {
           assertFalse(true);
       }
   }
   
   public void testSetDeskMapID() {
       try {
           Desk desk = new Desk();
           desk.setBuilding("Vancouver Broadway Green");
           desk.setDeskID("1.A.101");
           String newmapID = "1.A.B";
           AdminDeskQuery.setDeskMapID(desk, newmapID);
       } catch (SQLException ex) {
           assertFalse(true);
       }
   }
   public void testAddDesks(){
       List<String> deskIDs = new ArrayList<>();
       String buildingName = "Vancouver Broadway Green";
       deskIDs.add("7.C.101");
       deskIDs.add("7.C.102");
       
       try {
           List<DeskMap> deskBefore = AdminDeskQuery.viewDesks(buildingName, "7", "C");
           AdminDeskQuery.addDesks(buildingName, 7, "C", "1.A.A", deskIDs);
           
           List<DeskMap> deskAfter = AdminDeskQuery.viewDesks(buildingName, "7", "C");
           System.out.println("Desk number before: " + deskBefore.size() + " Desk number after: " + deskAfter.size());
           assertTrue(deskAfter.size() == deskBefore.size() + 2);
           
           //Reset
           AdminDeskQuery.deleteDesks(buildingName, deskIDs);
           
       } catch (Exception ex) {
           Logger.getLogger(QueryControllerTests.class.getName()).log(Level.SEVERE, null, ex);
           assertFalse(true);
       }
    }
   
    public void testAddDesksAlreadyExists(){
        String buildingName = "Vancouver Broadway Green";
        List<String> deskIDs = new ArrayList<>();
        deskIDs.add("1.A.110");
       try {
           AdminDeskQuery.addDesks(buildingName, 1, "A", "A", deskIDs);
           assertFalse(true);
       } catch (Exception e) {
           String msg = "Desk(s) already exists";
           System.out.println(e.getMessage());
           assertTrue(e.getMessage().contains(msg));
       }
    }
    
    public void testAddDeskBuildingLocationNotExists() {
        
        String buildingName = "La La Land";
        List<String> deskIDs = new ArrayList<>();
        deskIDs.add("1.A.110");
       try {
           AdminDeskQuery.addDesks(buildingName, 1, "A", "A", deskIDs);
           assertFalse(true);
       } catch (Exception e) {
           String msg = "Location does not exist";
           System.out.println(e.getMessage());
           assertTrue(e.getMessage().contains(msg));
       }
    }
    
   
   public void testDeleteDesks(){
       try {
           String buildingName = "Vancouver Broadway Green";
           List<String> deskIDS = new ArrayList<>();
           deskIDS.add("9.F.999");
           deskIDS.add("9.F.998");
           
           AdminDeskQuery.addDesks(buildingName, 9, "F", "1.A.A", deskIDS);
           
           List<DeskMap> before = AdminDeskQuery.viewDesks(buildingName, "9", "F");
           AdminDeskQuery.deleteDesks(buildingName, deskIDS);
           List<DeskMap> after = AdminDeskQuery.viewDesks(buildingName, "9", "F");
           assertTrue(before.size() == after.size() + 2);
       } catch (SQLException ex) {
           Logger.getLogger(QueryControllerTests.class.getName()).log(Level.SEVERE, null, ex);
       } catch (Exception ex) {
           Logger.getLogger(QueryControllerTests.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
   
   public void testEditDesk(){
       Desk oldDesk = new Desk();
       String buildingName = "Vancouver Broadway Green";
       oldDesk.setBuilding(buildingName);
       oldDesk.setDeskID("4.B.266");
       
       Desk newDesk = new Desk();
       newDesk.setBuilding(buildingName);
       newDesk.setDeskID("4.B.300");
       
       boolean result = false;
       
       try {
           AdminDeskQuery.editDesk(oldDesk, newDesk);
           List<DeskMap> desks = AdminDeskQuery.viewDesks(buildingName, "4", "B");
           for(int i = 0; i < desks.size(); ++i) {
                String deskID = desks.get(i).getDesk().getDeskID();
                if (deskID.equals(newDesk.getDeskID())){
                    result = true;
                }
            }
           
           assertTrue(result);
           
           //reset
           AdminDeskQuery.editDesk(newDesk, oldDesk);
       } catch (SQLException ex) {
           Logger.getLogger(QueryControllerTests.class.getName()).log(Level.SEVERE, null, ex);
           assertFalse(true);
       }      
       
    }
   
   public void testViewDesks(){
       try {
           String building = "Vancouver Broadway Green";
           String floor = "1";
           String section = "A";
           List<DeskMap> desks = AdminDeskQuery.viewDesks(building, floor, section);
           
           for(int i = 0; i < desks.size(); ++i) {
               String build = desks.get(i).getDesk().getBuilding();
               String deskId = desks.get(i).getDesk().getDeskID();
               System.out.println(String.format("Building: %1$s, DeskID: %2$s", build, deskId));
           }
           
           assertTrue(desks.size() == 96);
       } catch (SQLException ex) {
           assertFalse(true);
       }
   }
   
   public void testAddMapImage(){
       String key = "test";
       String url = "test url";
       try {
           AdminImageQuery.addMapImage(key, url);
           String imageURL = AdminImageQuery.getImageURL(key);
           assertEquals(url, imageURL);
           
           //reset
           deleteMapImage(key);
       } catch (Exception ex) {
           Logger.getLogger(QueryControllerTests.class.getName()).log(Level.SEVERE, null, ex);
           assertFalse(true);
       }
   }
   
   private void deleteMapImage(String key){
        Connection connection = DBUtility.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "DELETE FROM mapimage WHERE mapkey = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, key);
            ps.executeUpdate();

       } catch(SQLException e) {
           System.out.println(e);
       } finally {
            if (ps != null) { try { ps.close();} catch (SQLException ex) {} }
            if (rs != null) { try { rs.close();} catch (SQLException ex) {} }
            if (connection != null) { try { connection.close();} catch (SQLException ex) {} }
       }
   }
   
   public void testAddMapImageAlreadyExists(){
       String key = "1.A.B";
       String url = "https://s3.ca-central-1.amazonaws.com/hsbc-maps/vancouver_broadway_green/floor_1/1.A.B.jpg";
       try {
           AdminImageQuery.addMapImage(key, url);
           assertFalse(true);
       } catch (SQLException ex) {
           String msg = "Map key already exists";
           System.out.println(ex.getMessage());
           assertEquals(msg, ex.getMessage());
       }
   }
   
   public void testGetImageURL(){
       String key = "1.A.A";
       String expectedURL = "https://s3.ca-central-1.amazonaws.com/hsbc-maps/vancouver_broadway_green/floor_1/1.A.A.jpg";
       try {
           String imageURL = AdminImageQuery.getImageURL(key);
           assertEquals (expectedURL, imageURL);
       } catch (Exception ex) {
           Logger.getLogger(QueryControllerTests.class.getName()).log(Level.SEVERE, null, ex);
           assertFalse(true);
       }
   }
   
   public void testGetImageURLNotExist() {
       String key = "9.9.9";
       
       try {
           AdminImageQuery.getImageURL(key);
           assertFalse(true);
       } catch (Exception ex) {
           String msg = "Map key does not exist";
           System.out.println(ex.getMessage());
           assertEquals(msg, ex.getMessage());
       }
       
   }
   
   public void testGetAllFloors() {
       try {
           List<Integer> floors = InquiryQuery.getAllFloors("Vancouver Broadway Green");
           assertEquals(4, floors.size());
       } catch (SQLException ex) {
           System.out.println(ex);
           assertFalse(true);
       }
   }
   
   public void testGetAllFloorsLocationNotExist(){
       try {
           InquiryQuery.getAllFloors("La la land");
           assertFalse(true);
       } catch (SQLException | NoSuchElementException ex) {
           String msg = "Location does not exist";
           System.out.println(ex.getMessage());
           assertEquals(msg, ex.getMessage());
       }
   }
   
      public void testGetAllSection() {
       try {
           List<String> sections = InquiryQuery.getAllSections("Vancouver Broadway Green");
           assertEquals(2, sections.size());
       } catch (SQLException | NoSuchElementException ex) {
           System.out.println(ex);
           assertFalse(true);
       }
   }
   
   public void testGetAllSectionLocationNotExist(){
       try {
           InquiryQuery.getAllSections("La la land");
           assertFalse(true);
       } catch (SQLException | NoSuchElementException ex) {
           String msg = "Location does not exist";
           System.out.println(ex.getMessage());
           assertEquals(msg, ex.getMessage());
       }
   }
   
   public void testAddSectionAlreadyExists(){
       String building = "Vancouver Broadway Green";
       int floor = 1;
       String section = "A";
       String mapID = "1.A";
       try {
           AdminLocationQuery.addSection(building, floor, section, mapID);
           assertFalse(true);
       } catch (SQLException ex) {
           String msg = "Section already exists for this location";
           assertEquals(msg, ex.getMessage());
       }
   }
   
   public void testAddDeleteSection() {
       String building = "Vancouver Broadway Green";
       int floor = 9;
       String section = "F";
       String mapID = "1.A";
       try {
           List<String> sectionsBefore = InquiryQuery.getAllSections(building);
           AdminLocationQuery.addSection(building, floor, section, mapID);
           List<String> sectionsAfter = InquiryQuery.getAllSections(building);
           assertTrue(sectionsAfter.size() == sectionsBefore.size() + 1);
           
           //reset
           AdminLocationQuery.deleteSection(building, floor, section);
           List<String> secAfterDel = InquiryQuery.getAllSections(building);
           assertTrue(secAfterDel.size() == sectionsBefore.size());
       } catch (SQLException ex) {
           System.out.println(ex);
           assertFalse(true);
       }
   }
   
   
   public void testDeleteSectionNotExist() {
       String building = "Vancouver Broadway Green";
       int floor = 1;
       String section = "C";
       try {
           AdminLocationQuery.deleteSection(building, floor, section);
           assertFalse(true);
       } catch (SQLException | NoSuchElementException ex) {
           String msg = "Section does not exist";
           assertEquals(msg, ex.getMessage());
       }
   }
   
   public void testGetFloorSectionMapID(){
       String building = "Vancouver Broadway Green";
       String section = "A";
       int floor = 1;
       
       try {
           String mapID = AdminLocationQuery.getFloorSectionMapID(building, floor, section);
           assertEquals("1.A", mapID);
       } catch (Exception ex) {
           System.out.println(ex);
           assertFalse(true);
       }
       
   }
   
   public void testGetFloorSectionMapIDNotExist() {
       String building = "Vancouver Broadway Green";
       String section = "F";
       int floor = 1;
       
       try {
           String mapID = AdminLocationQuery.getFloorSectionMapID(building, floor, section);
           assertFalse(true);
       } catch (Exception ex) {
           String msg = "MapID for this floor and section does not exist";
           System.out.println(ex.getMessage());
           assertEquals(msg, ex.getMessage());
       }
   }
   
   public void testModifyMapUrlKeyNotExist(){
       String key = "1.F.F";
       String url = "test url";
       
       try {
           AdminImageQuery.modifyMapUrl(key, url);
           assertFalse(true);
       } catch (SQLException | NoSuchElementException ex) {
           String msg = "Map key does not exist";
           System.out.println(ex.getMessage());
           assertEquals(msg, ex.getMessage());
       }
   }
   
   public void testModifyMapUrl(){
       String key = "1.A";
       String newURL = "test url";
       String oldURL = "https://s3.ca-central-1.amazonaws.com/hsbc-maps/vancouver_broadway_green/floor_1/1.A.jpg";
       
       try {
           AdminImageQuery.modifyMapUrl(key, newURL);
           String nURL = AdminImageQuery.getImageURL(key);
           assertEquals(newURL, nURL);
           
           AdminImageQuery.modifyMapUrl(key, oldURL);
           String oURL = AdminImageQuery.getImageURL(key);
           assertEquals(oldURL, oURL);
       } catch (SQLException ex) {
           System.out.println(ex);
           assertFalse(true);
       }
   }
   
   // uncomment out to test archive booking; currently uncommented to prevent
   //   archive from removing test data
   /* 
   public void testArchiveInactiveBookings()
   {
       try {
           Desk d = new Desk();
           d.setDeskID("");
           d.setBuilding("");
           Timestamp start = Timestamp.valueOf("2000-04-01 08:00:00");
           Timestamp end = Timestamp.valueOf("2020-06-01 08:00:00");
           List<DeskSelection> resBefore = AdminReservationQuery.viewDeskSelection("", d, start, end);
           System.out.println(resBefore.size());
           AdminDatabaseQuery.archiveInactiveBookings();
           Timestamp timestamp = new Timestamp(System.currentTimeMillis());
           List<DeskSelection> resAfter = AdminReservationQuery.viewDeskSelection("", d, start, timestamp);
           System.out.println(resAfter.size());
           assertTrue(resBefore.size() > resAfter.size());
       } catch (SQLException ex) {
           System.out.println(ex);
           assertFalse(true);
       }
   } */
}
