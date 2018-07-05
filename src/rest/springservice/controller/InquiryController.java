package rest.springservice.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import rest.domain.*;
import utility.DBUtility;
import utility.queries.AdminImageQuery;
import utility.queries.AdminLocationQuery;
import utility.queries.InquiryQuery;

/**
 * A controller class to inquire information
 * @author yozubear
 */
@RestController
@RequestMapping("/inquiry")
public class InquiryController {
    
    /**
     * Reached by: {url base}/inquiry/desk
     * Inquire the availability of a desk on a selected day (as captured by InquireDesk)
     * @param inqDesk - the day and desk of interest
     * @return an array of reservations of the inquired desk on the specified day
     */
    @RequestMapping(value = "/desk", method = RequestMethod.POST, produces = "application/json")
    public List<ReservationBase> inquireDesk(@RequestBody InquireDesk inqDesk) throws Exception {
        Timestamp date = DBUtility.getSQLDate(inqDesk.getDate());
        List<ReservationBase> reservations = InquiryQuery.inquireDesk(inqDesk.getDesk(), date);
        return reservations;
    }
    
    /**
     * Reached by: {url base}/inquiry/employee_reservation
     * Get all reservations made by the employee (based on employee ID) on the given date
     * @param inqEmployee
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/employee_reservation", method = RequestMethod.POST, produces = "application/json")
    public List<DeskSelection> inquireEmployee(@RequestBody InquireEmployee inqEmployee) throws Exception {
        Timestamp date = DBUtility.getSQLDate(inqEmployee.getInquireDate());
        List<DeskSelection> reservations = InquiryQuery.getReservationByEmployee(inqEmployee.getEmployeeId(), date);
        return reservations;
    }
    
    /**
     * Reached by: {url base}/inquiry/get_all_employee
     * @returnall employees stored in database
     * @throws Exception 
     */
    @RequestMapping(value = "/get_all_employee", method = RequestMethod.GET, produces = "application/json")
    public List<Employee> getAllEmployee() throws Exception{
        return InquiryQuery.getAllEmployees();
    }

    /**
     * Reached by: {url base}/inquiry/mocked/myhsbc/uservariables/{name}
     * Mocked HSBC Employee response for http://ssupdate.global.hsbc/myhsbc/uservariables.ashx
     * Currently support Jerry Jim, Julin Song, Clifford Lee and John Doe
     * @return JSONObject of the HSBC user info
     */
    @RequestMapping(value = "/mocked/myhsbc/uservariables/{name}", method = RequestMethod.GET, produces = "application/json")
    public StringObject hsbcApiUser(@PathVariable String name) throws ParseException {
        if(name == null || name.isEmpty()){
            name = "John Doe";
        }
        String hsbcUser;
        if(name.equals("Clifford Lee"))
            hsbcUser = "var staffDetails_name='Clifford Lee';var staffDetails_empid='93715639';var staffDetails_extphone='1 1 778 8743386';var staffDetails_country='CANADA';var staffDetails_jobrole='HSBC SPONSOR';var staffDetails_dept='SWD WEALTH MANAGEMENT IT GENER';var staffDetails_photourl='photos.global.hsbc/casual/square/4316/93715639.jpg'; var staffDetails_extemail='cliffordLee@hsbc.com';var staffDetails_employeeType='E';var staffDetails_location='3383GIL';var staffDetails_intphone='1-888-310-4722';var staffDetails_postaladdress='3383 Gilmore Way, Burnaby, B.C. V5G 4S1';var errormessage='';";
        else if(name.equals("Jerry Jim"))
            hsbcUser = "var staffDetails_name='Jerry Jim';var staffDetails_empid='17654324';var staffDetails_extphone='1 1 778 8743386';var staffDetails_country='CANADA';var staffDetails_jobrole='PROFESSOR OF UBC';var staffDetails_dept='UBC COMPUTER SCIENCE DEPARTMENT';var staffDetails_photourl='photos.global.hsbc/casual/square/4316/17654324.jpg'; var staffDetails_extemail='jerryjim@cs.ubc.ca';var staffDetails_employeeType='E';var staffDetails_location='3383GIL';var staffDetails_intphone='604-822-3061';var staffDetails_postaladdress='3383 Gilmore Way, Burnaby, B.C. V5G 4S1';var errormessage='';";
        else if (name.equals("Julin Song"))
            hsbcUser = "var staffDetails_name='Julin Song';var staffDetails_empid='50317593';var staffDetails_extphone='1 1 778 8743386';var staffDetails_country='CANADA';var staffDetails_jobrole='TA OF CPSC 319';var staffDetails_dept='UBC COMPUTER SCIENCE DEPARTMENT';var staffDetails_photourl='photos.global.hsbc/casual/square/4316/50317593.jpg'; var staffDetails_extemail='julinSong@cs.ubc.ca';var staffDetails_employeeType='E';var staffDetails_location='3383GIL';var staffDetails_intphone='604-822-3063';var staffDetails_postaladdress='3383 Gilmore Way, Burnaby, B.C. V5G 4S1';var errormessage='';";
        else
           hsbcUser = "var staffDetails_name='John Doe';var staffDetails_empid='43868488';var staffDetails_extphone='1 1 778 8743386';var staffDetails_country='CANADA';var staffDetails_jobrole='HEAD OF WEALTH IT CANADA';var staffDetails_dept='SWD WEALTH MANAGEMENT IT GENER';var staffDetails_photourl='photos.global.hsbc/casual/square/4316/43868488.jpg'; var staffDetails_extemail='g4c9@ugrad.cs.ubc.ca';var staffDetails_employeeType='E';var staffDetails_location='3383GIL';var staffDetails_intphone='1-888-310-4722';var staffDetails_postaladdress='3383 Gilmore Way, Burnaby, B.C. V5G 4S1';var errormessage='';"; 
        return new StringObject(hsbcUser);
    }

    /**
     * Reached by: {url base}/inquiry/get_employee
     * Convert HSBCUser to Employee type
     * @param user - HSBCUser with more information than necessary
     * @return JSONObject of the employee info
     */
    @RequestMapping(value = "/get_employee", method = RequestMethod.POST, produces = "application/json")
    public Employee getEmployee(@RequestBody HSBCUser user) {
        String id = user.getStaffDetails_empid();
        String name = user.getStaffDetails_name();
        String dept = user.getStaffDetails_dept();
        String phoneNum = user.getStaffDetails_extphone();
        String email = user.getStaffDetails_extemail();

        Employee employee = new Employee(id, name, dept, phoneNum, email);

        return employee;
    }
    
     /**
     * Reached by: {url base}/inquiry/view_all_floors_sections/{buildingName}
     * View all the floors and sections available for all buildings
     * @return buildings - a map that maps building names to its floors and their associated sections
     * @throws java.lang.Exception
     */
    @RequestMapping(value = "/view_all_floors_sections", method = RequestMethod.GET, produces = "application/json")
    public Buildings getAllFloors() throws Exception {
        
        // Dictionary for all buildings and their associated sections
        List<Location> locations = AdminLocationQuery.viewAllBuildings();
        Map<String, FloorsSections> buildings = new HashMap<>();
        
        for(Location loc : locations){
            String buildingName = loc.getOfficename();
            FloorsSections floorSections = InquiryQuery.getAllFloorSections(buildingName);
            buildings.put(buildingName, floorSections);
        }
        
        // Dictionary for section maps
        Map<String, String> sectionMaps = InquiryQuery.getSectionMapsDictionary();
        
        return new Buildings(buildings, sectionMaps);
    }
    
    /**
     * Get the section map url
     * @param section
     * @return 
     */
    @RequestMapping(value = "/view_section_mapURL", method = RequestMethod.POST, produces = "application/json")
    public StringObject viewSectionMap(@RequestBody ViewDesks section) throws Exception{
        String mapID = AdminLocationQuery.getFloorSectionMapID(section.getBuilding(), Integer.parseInt(section.getFloor()), section.getSection());
        String mapURL = AdminImageQuery.getImageURL(mapID);
        return new StringObject(mapURL);
    }
    
    @ExceptionHandler(Exception.class)
    public ExceptionObject handleEmployeeNotFoundException(HttpServletRequest request, Exception ex) {
        
        return new ExceptionObject(ex.getMessage());
    }
    
}
