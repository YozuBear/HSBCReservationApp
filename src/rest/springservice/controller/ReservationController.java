package rest.springservice.controller;

/**
 *
 * @author yozubear
 */
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.portlet.ModelAndView;
import rest.domain.*;
import utility.DBUtility;
import utility.Email;
import utility.queries.AdminDeskQuery;
import utility.queries.AdminImageQuery;
import utility.queries.ReservationQuery;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    /**
     * Reached by: {url base}/reservation/floor_preference Client specifies
     * preferred time and location, get back list of available desks
     *
     * @param fp - floor location and time preference
     * @return an array of available desks during preferred time at specified
     * section
     * @throws java.lang.Exception
     */
    @RequestMapping(value = "/floor_preference", method = RequestMethod.POST, produces = "application/json")
    public List<Desk> getAvailableDesks(@RequestBody FloorPreference fp) throws Exception {

        ReservationBase base = fp.getBase();
        String employeeID = base.getEmployee().getId();
        Timestamp startTime = DBUtility.getSQLTimeStamp(base.getStartDate(), base.getStartTime());
        Timestamp endTime = DBUtility.getSQLTimeStamp(base.getEndDate(), base.getEndTime());
        Timestamp currentTime = DBUtility.getCurrentTimestamp();

        // Check selected start time is in the futuDBUtility.occursAfter(startTime, endTime)
        if (currentTime.compareTo(startTime) > 0) {
            throw new Exception("Start time is in the past\nCurrent time: " + currentTime.toString() 
                    + "\nStart time: " + startTime.toString());
        }

        // Check end time happens after start time
        if (endTime.compareTo(startTime) < 1) {
            throw new Exception("End time is before start time");
        }

        // Check if selected time frame is more than 5 days
        if (DBUtility.getDayDifference(startTime, endTime) > 5) {
            throw new Exception("Reservation period cannot be more than 5 days");
        }

        // Check if selected hours is more than 30 days in advance
        if (DBUtility.getDayDifference(currentTime, startTime) > 30) {
            throw new Exception("Reservation cannot be made more than 30 days in advance");
        }

        this.checkReservation(employeeID, startTime, endTime);

        List<Desk> availableDesks
                = ReservationQuery.getAvailableDesks(startTime, endTime, fp.getBuilding(), fp.getFloor(), fp.getSection());

        return availableDesks;
    }
    
    /**
     * Check if reservation should proceed
     * @param employeeID
     * @param startTime
     * @param endTime 
     */
    private void checkReservation(String employeeID, Timestamp startTime,Timestamp endTime) throws Exception{
        // Check if user has reserve entries greater than reservation limit
        int reserveLimit = 10;
        boolean isOverReserved = ReservationQuery.isOverReserved(employeeID, reserveLimit);
        if (isOverReserved) {
            throw new Exception("Reservation limit of " + reserveLimit + " has been reached");
        }

        // Check if user's reservation time overlaps with previous reservations
        boolean overlap = ReservationQuery.isReserveTimeOverlapped(employeeID, startTime, endTime);
        if (overlap) {
            throw new Exception("Reservation time overlaps with your previous reservations");
        }
    }

    /**
     * Reached by: {url base}/reservation/hold Client specifies desired desk to
     * hold
     *
     * @param ds: desk selection
     * @throws java.lang.Exception
     */
    @RequestMapping(value = "/hold", method = RequestMethod.POST, produces = "application/json")
    public StringObject holdDesk(@RequestBody DeskSelection ds) throws Exception {

        ReservationBase base = ds.getBase();
        Timestamp startTime = DBUtility.getSQLTimeStamp(base.getStartDate(), base.getStartTime());
        Timestamp endTime = DBUtility.getSQLTimeStamp(base.getEndDate(), base.getEndTime());
        Desk desk = ds.getDesk();
        Employee emp = base.getEmployee();

        this.checkReservation(emp.getId(), startTime, endTime);
        
        // Check if desk is available
        // desk is rarely unavailable as it was checked during preference stage that this desk is available
        boolean deskAvailable = ReservationQuery.isDeskAvailable(ds.getDesk(), startTime, endTime);
        if (!deskAvailable) {
            throw new Exception("Desk " + desk.getDeskID() + "is unavailable");
        }
        
        if (!ReservationQuery.isEmployee(emp)) {
            ReservationQuery.addEmployee(emp);
        }
        // hold desk
        ReservationQuery.holdDesk(startTime, endTime, base.getEmployee(), desk);
        
        return new StringObject("Desk successfully held");
    }

    /**
     * Delete hold when 10 min. on hold has passed or user exits without confirmation.
     * @param ds
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/delete_hold", method = RequestMethod.POST, produces = "application/json")
    public StringObject deleteHold(@RequestBody DeskSelection ds) throws Exception {
        ReservationBase base = ds.getBase();
        Timestamp startTime = DBUtility.getSQLTimeStamp(base.getStartDate(), base.getStartTime());
        Timestamp endTime = DBUtility.getSQLTimeStamp(base.getEndDate(), base.getEndTime());
        Desk desk = ds.getDesk();

        ReservationQuery.deleteHold(startTime, endTime, base.getEmployee(), desk);

        return new StringObject("successfully deleted hold entry");
    }
    
    /**
     * Return the map URL for the given desk
     * @param desk
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/get_map_url", method = RequestMethod.POST, produces = "application/json")
    public StringObject getMapURL(@RequestBody Desk desk) throws Exception{
        String mapID = AdminDeskQuery.getDeskMapID(desk);
        String mapURL = AdminImageQuery.getImageURL(mapID);
        return new StringObject(mapURL);
    }

    /**
     * Reached by: {url base}/reservation/reserve Reserve the desk that's been
     * previously held
     *
     * @param confirm
     * @return
     * @throws java.lang.Exception
     */
    @RequestMapping(value = "/reserve", method = RequestMethod.POST, produces = "application/json")
    public StringObject reserve(@RequestBody Confirm confirm) throws Exception {

        ReservationBase base = confirm.getBase();
        Timestamp startTime = DBUtility.getSQLTimeStamp(base.getStartDate(), base.getStartTime());
        Timestamp endTime = DBUtility.getSQLTimeStamp(base.getEndDate(), base.getEndTime());
        Desk desk = confirm.getDesk();
        Employee employee = base.getEmployee();
        String returnMsg = "Reservation is made successfully.\n";

        boolean deskHeldByEmployee = ReservationQuery.isEmployeeHoldsDesk(employee, desk, startTime, endTime);
        if (!deskHeldByEmployee) {
            throw new Exception("reserve desk has not been held by employee");
        }

        // Officially reserve desk
        ReservationQuery.reserveDesk(employee.getId(), desk, startTime, endTime);
        
        // Get map url associated with desk
        String deskMapID = AdminDeskQuery.getDeskMapID(desk);
        String mapURL = AdminImageQuery.getImageURL(deskMapID);

        //Send email if email me is checked
//        if (confirm.isEmailMe()) {
//            String email = employee.getEmail();
//            String emailContent = "Desk Reservation Confirmation\n"
//                    + "\n employee ID: " + employee.getId()
//                    + "\n employee Name: " + employee.getName()
//                    + "\n employee phone: " + employee.getPhoneNum()
//                    + "\n employee email: " + employee.getEmail()
//                    + "\n building: " + desk.getBuilding()
//                    + "\n desk ID: " + desk.getDeskID()
//                    + "\n start time: " + startTime.toString()
//                    + "\n end time: " + endTime.toString() +
//                    "\n\n";
//            
//            // Send map image if map url is available
//            if(!mapURL.isEmpty())
//                emailContent += "View desk map at: " + mapURL;
//            
//            Email.send(email, "Reservation Confirmation", emailContent);
//            returnMsg += "Confirmation email sent to " + email;
//        }

        return new StringObject(returnMsg);
    }

    @ExceptionHandler(Exception.class)
    public ExceptionObject handleEmployeeNotFoundException(HttpServletRequest request, Exception ex) {
        return new ExceptionObject(ex.getMessage());
    }

}
