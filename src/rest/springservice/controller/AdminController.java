package rest.springservice.controller;

import com.mysql.jdbc.ConnectionProperties;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rest.domain.AddDesks;
import rest.domain.AdminReservationEntries;
import rest.domain.DeleteDesks;
import rest.domain.DeleteReservation;
import rest.domain.Desk;
import rest.domain.DeskMap;
import rest.domain.DeskSelection;
import rest.domain.Employee;
import rest.domain.ExceptionObject;
import rest.domain.Location;
import rest.domain.ModifyDesk;
import rest.domain.ModifyReservation;
import rest.domain.Section;
import rest.domain.StringObject;
import rest.domain.ViewDesks;
import utility.DBUtility;
import utility.Email;
import utility.queries.AdminDatabaseQuery;
import utility.queries.AdminDeskQuery;
import utility.queries.AdminImageQuery;
import utility.queries.AdminLocationQuery;
import utility.queries.AdminQuery;
import utility.queries.AdminReservationQuery;

/**
 * Controller class for Admin pages
 *
 * @author yozubear
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    //--------------------------------------------------------------------------
    //
    //      Admin Page - Admins
    //
    //--------------------------------------------------------------------------
    /**
     * Reached by: {url base}/admin/add_admin/{employeeID} add admin to database
     * by employeeID
     *
     * @param employeeID - Employee.id
     * @return success/failure message
     */
    @RequestMapping(value = "/add_admin/{employeeID}", method = RequestMethod.GET, produces = "application/json")
    public StringObject addAdmin(@PathVariable String employeeID) throws Exception {

        AdminQuery.addAdmin(employeeID);

        return new StringObject("admin " + employeeID + " successfully added");
    }

    /**
     * Reached by: {url base}/admin/delete_admin/{employeeID} delete admin from
     * database by employeeID
     *
     * @param employeeID - Employee.id
     * @return success/failure message
     */
    @RequestMapping(value = "/delete_admin/{employeeID}", method = RequestMethod.GET, produces = "application/json")
    public StringObject deleteAdmin(@PathVariable String employeeID) {

        AdminQuery.deleteAdmin(employeeID);

        return new StringObject("admin " + employeeID + " successfully deleted");
    }

    /**
     * Reached by: {url base}/admin/isAdmin/{employeeID} Return true if given
     * employee is admin, dalse otherwise
     *
     * @param employeeID - Employee.id
     * @return
     */
    @RequestMapping(value = "/isAdmin/{employeeID}", method = RequestMethod.GET, produces = "application/json")
    public boolean isAdmin(@PathVariable String employeeID) throws Exception {

        return AdminQuery.isAdmin(employeeID);
    }

    /**
     * Reached by: {url base}/admin/verify_admin_password/{pw} Verify admin
     * password is correct during initialization stage
     *
     * @param pw - admin password
     * @return true if password is correct.
     */
    @RequestMapping(value = "/verify_admin_password/{pw}", method = RequestMethod.GET, produces = "application/json")
    public boolean verifyAdminPassWord(@PathVariable String pw) throws Exception {

        // match pw with the admin password from a file (pw.txt)
        // so HSBC admin can easily change this pw in future
        // proposed password: HSBCAdmin2017
        String currentFilePath = new File("").getAbsolutePath();
        String file = currentFilePath + "/pw.txt";

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        while ((line = br.readLine()) != null) {

            if (pw.equals(line)) {
                return true;
            }
        }
        br.close();

        return false;
    }

    /**
     * Reached by: {url base}/admin/viewAdmins
     *
     * @return All Admin IDs
     */
    @RequestMapping(value = "/view_admins", method = RequestMethod.GET, produces = "application/json")
    public List<String> viewAdmins() throws Exception {

        return AdminQuery.viewAdmins();
    }

    //--------------------------------------------------------------------------
    //
    //      Admin Page - Reservations
    //
    //--------------------------------------------------------------------------
    /**
     * Reached by: {url base}/admin/reservation_entry/view
     *
     * See all desk selections filtered by resEntries
     *
     * @param resEntries - the filters, empty strings if no filter is applied,
     * return all entries
     * @return all reservations after resEntries filter
     * @throws java.lang.Exception
     */
    @RequestMapping(value = "/reservation_entry/view", method = RequestMethod.POST, produces = "application/json")
    public List<DeskSelection> viewReservationEntries(@RequestBody AdminReservationEntries resEntries) throws Exception {
        String startDate = resEntries.getStartDate();
        String startTime = resEntries.getStartTime();
        String endDate = resEntries.getEndDate();
        String endTime = resEntries.getEndTime();
        Timestamp start = null;
        Timestamp end = null;

        if (startDate.isEmpty()) {
            start = new Timestamp(0);
        } else if (startTime.isEmpty()) {
            // If start date is not empty, it should be formatted as mm/dd/yyyy
            start = DBUtility.getSQLDate(startDate);
        } else {
            start = DBUtility.getSQLTimeStamp(startDate, startTime);
        }

        if (endDate.isEmpty()) {
            end = DBUtility.getSQLDate("01/01/9999");
        } else if (endTime.isEmpty()) {
            // If start date is not empty, it should be formatted as mm/dd/yyyy
            end = DBUtility.getSQLDate(endDate);
        } else {
            end = DBUtility.getSQLTimeStamp(endDate, endTime);
        }

        Desk desk = new Desk(resEntries.getBuilding(), resEntries.getDeskID());
        List<DeskSelection> deskSelections = AdminReservationQuery.viewDeskSelection(resEntries.getEmployeeID(), desk, start, end);

        return deskSelections;
    }

    /**
     * Reached by: {url base}/admin/reservation_entry/delete
     *
     * Delete reservation entry
     *
     * @param deleteReserve - includes reservation time frame, desk to be
     * deleted and admin who deleted the entry
     * @return message, whether entry is successfully deleted and email sent
     * @postCondition send email notification to employee who reserved the desk
     * of the deletion
     */
    @RequestMapping(value = "/reservation_entry/delete", method = RequestMethod.POST, produces = "application/json")
    public StringObject deleteReservationEntry(@RequestBody DeleteReservation deleteReserve) throws Exception {
        Employee admin = deleteReserve.getAdmin();
        DeskSelection re = deleteReserve.getReserveEntry();

        // delete reservation entry
        AdminReservationQuery.deleteReservationEntry(re);

        // Sends email to employee about deleted entry
        String mailContent = "Hi " + deleteReserve.getReserveEntry().getBase().getEmployee().getName() + ",\n"
                + "Following desk reservation entry has been deleted:\n\n" + re.toString() + "\n\nReservation deleted by: \n" + admin.toString();
        //Email.send(re.getBase().getEmployee().getEmail(), "Reservation Entry Deleted", mailContent);

        return new StringObject("Successfully deleted reservation entry.");
    }

    /**
     * Reached by: {url base}/admin/reservation_entry/modify
     *
     * Modify reservation entry and send emails to reserver of the change
     *
     * @param modReserve - old, new reservation entries and admin who made the
     * modification
     * @return a message whether entry is successfully modified, and email sent
     * @postCondition sends email notification to employee who reserved the desk
     * of the modification
     */
    @RequestMapping(value = "/reservation_entry/modify", method = RequestMethod.POST, produces = "application/json")
    public StringObject modifyReservationEntry(@RequestBody ModifyReservation modReserve) throws Exception {
        Employee admin = modReserve.getAdmin();
        DeskSelection oldEntry = modReserve.getOldEntry();
        DeskSelection newEntry = modReserve.getNewEntry();

        // need to check if new entry will cause an overlap bookings
        // delete reservation entry
        AdminReservationQuery.modifyReservationEntry(oldEntry, newEntry);

        // Sends email to employee about updated entry
        String mailContent = "Hi " + oldEntry.getBase().getEmployee().getName() + ",\n"
                + "Following desk reservation entry has been updated: \n\nOld entry: \n"
                + oldEntry.toString()
                + "\nNew entry: \n"
                + newEntry.toString()
                + "\n\nContact Admin for questions: \n" + admin.toString();
        //Email.send(oldEntry.getBase().getEmployee().getEmail(), "Modified Reservation Entries", mailContent);

        return new StringObject("Successfully modified reservation entry.");
    }

    //--------------------------------------------------------------------------
    //
    //      Admin Page - Modify Buildings
    //
    //--------------------------------------------------------------------------
    /**
     * Reached by: {url base}/admin/add_building Add new building to database
     *
     * @param loc - building name and address
     * @return operation message
     */
    @RequestMapping(value = "/add_building", method = RequestMethod.POST, produces = "application/json")
    public StringObject addBuilding(@RequestBody Location loc) throws Exception {

        String building = loc.getOfficename();
        AdminLocationQuery.addBuilding(building, loc.getAddress());

        return new StringObject("Successfully added " + building + " to database");
    }

    /**
     * Reached by: {url base}/admin/delete_building Delete building to database
     *
     * @param loc - building name and address
     * @return operation message
     */
    @RequestMapping(value = "/delete_building", method = RequestMethod.POST, produces = "application/json")
    public StringObject deleteBuilding(@RequestBody Location loc) throws Exception {

        String building = loc.getOfficename();
        AdminLocationQuery.deleteBuilding(building, loc.getAddress());

        return new StringObject("Successfully deleted " + building + " from database");
    }

    /**
     * Modify the building address to new address
     *
     * @param loc - consisted of building to modify and the new address
     * @return
     */
    @RequestMapping(value = "/modify_building", method = RequestMethod.POST, produces = "application/json")
    public StringObject modifyBuildingAddress(@RequestBody Location loc) throws SQLException {

        String building = loc.getOfficename();
        String newAddress = loc.getAddress();

        AdminLocationQuery.modifyBuildingAddress(building, loc.getAddress());

        return new StringObject("Successfully modify " + building + "'s address to " + newAddress);
    }

    /**
     * Reached by: {url base}/admin/view_buildings
     *
     * @return all buildings
     */
    @RequestMapping(value = "/view_buildings", method = RequestMethod.GET, produces = "application/json")
    public List<Location> viewBuildings() throws Exception {
        return AdminLocationQuery.viewAllBuildings();
    }
    
    //--------------------------------------------------------------------------
    //
    //      Admin Page - Modify Floor Sections
    //
    //--------------------------------------------------------------------------
    @RequestMapping(value = "/add_new_section", method = RequestMethod.POST, produces = "application/json")
    public void addSections(@RequestBody Section newSection) throws Exception {
        
        String building = newSection.getBuilding();
        String floor = String.valueOf(newSection.getFloor());
        String section = newSection.getSection();
        
        // Add map URL
        String mapID = building + "_" + floor + "_" + section + "_" + Instant.now().getEpochSecond();
        String mapURL = newSection.getMapURL();
        if(mapURL == null)
            mapURL = "";
        AdminImageQuery.addMapImage(mapID, mapURL);
        
        // Add section
        AdminLocationQuery.addSection(building, newSection.getFloor(), section, mapID);
        
    }
    
    @RequestMapping(value = "/delete_section", method = RequestMethod.POST, produces = "application/json")
    public void deleteSection(@RequestBody ViewDesks section) throws Exception {
        // Delete section
        String building = section.getBuilding();
        int floor = Integer.parseInt(section.getFloor());
        String sectionStr = section.getSection();
        
        AdminLocationQuery.deleteSection(building, floor ,sectionStr);
    
    }
    
    @RequestMapping(value = "/modify_section_map", method = RequestMethod.POST, produces = "application/json")
    public void modifySectionMap(@RequestBody Section section) throws Exception {
        
        // get map ID associated with the section
        String mapID = AdminLocationQuery.getFloorSectionMapID(section.getBuilding(), section.getFloor(), section.getSection());
        
        // Update map URL
        AdminImageQuery.modifyMapUrl(mapID, section.getMapURL());

    }
    
    
    @RequestMapping(value = "/view_all_sections/{buildingName}", method = RequestMethod.GET, produces = "application/json")
    public List<Section> viewSections(@PathVariable String buildingName) throws Exception {
        return AdminLocationQuery.viewAllFloorSections(buildingName);
    }
    

    //--------------------------------------------------------------------------
    //
    //      Admin Page - Modify Desks
    //
    //--------------------------------------------------------------------------
    /**
     * add desks
     *
     * @param properties
     * @param ad
     * @return operation msg
     * @throws java.lang.Exception
     */
    @RequestMapping(value = "/add_desks", method = RequestMethod.POST, produces = "application/json")
    public StringObject addDesks(@RequestBody AddDesks ad) throws Exception {
        List<String> deskIDs = new ArrayList<>();
        String building = ad.getBuilding();
        int floor = ad.getFloor();
        String floorStr = String.valueOf(floor);
        String section = ad.getSection();
        int from = ad.getFrom();
        int to = ad.getTo();

        for (int i = from; i <= to; i++) {
            String deskID = floorStr + "." + section + "." + String.valueOf(i);
            deskIDs.add(deskID);
        }

        // if no desk is added, then the desk range is invalid
        if (deskIDs.isEmpty()) {
            throw new Exception("Invalid desk range");
        }

        
        // generate a key for the future image
        String mapImageKey = building + floorStr + section + Instant.now().getEpochSecond();
        String mapURL = ad.getImageUrl();
        if(mapURL == null)
            mapURL = "";
        AdminImageQuery.addMapImage(mapImageKey, mapURL);
        
//        MultipartFile image = ad.getImage();
//        // If image is uploaded
//        if (image != null) {
//            // add image to aws s3
//            FileArchiveService s3Service = new FileArchiveService();
//
//            MapImage mapImage = s3Service.saveFileToS3(image);
//            mapImageKey = mapImage.getKey();
//
//            // Add Map Image to database
//            QueryController.addMapImage(mapImage.getKey(), mapImage.getUrl());
//        }else{
//            
//        }


        // Check if section already exists
        boolean sectionExists = AdminLocationQuery.existFloorSection(building, floor, section);
        if(!sectionExists){
            AdminLocationQuery.addSection(building, floor, section, mapImageKey);
        }
        
        // Add Desks to database
        AdminDeskQuery.addDesks(building, floor, section, mapImageKey, deskIDs);

        return new StringObject("Desks sucessfully added");
    }

    /**
     * Delete desks
     *
     * @param dd
     * @return operation msg
     * @throws java.lang.Exception
     */
    @RequestMapping(value = "/delete_desks", method = RequestMethod.POST, produces = "application/json")
    public StringObject deleteDesks(@RequestBody DeleteDesks dd) throws Exception {
        List<String> deskIDs = new ArrayList<>();
        String floorStr = String.valueOf(dd.getFloor());
        String section = dd.getSection();
        int from = dd.getFrom();
        int to = dd.getTo();

        for (int i = from; i <= to; i++) {
            String deskID = floorStr + "." + section + "." + String.valueOf(i);
            deskIDs.add(deskID);
        }

        // if no desk is added, then the desk range is invalid
        if (deskIDs.isEmpty()) {
            throw new Exception("Invalid desk range");
        }

        AdminDeskQuery.deleteDesks(dd.getBuilding(), deskIDs);

        return new StringObject("Desks sucessfully deleted");
    }

    @RequestMapping(value = "/modify_desks", method = RequestMethod.POST, produces = "application/json")
    public StringObject modifyDesks(@RequestBody ModifyDesk md) throws Exception {
        Desk oldDesk = md.getOldDesk();
        Desk newDesk = md.getNewDesk();
        if(!oldDesk.equals(newDesk))
            AdminDeskQuery.editDesk(oldDesk, newDesk);
        
        // Change map URL
        String deskID = AdminDeskQuery.getDeskMapID(newDesk);
        String oldMapURL = AdminImageQuery.getImageURL(deskID).trim();
        String newMapURL = md.getMapURL().trim();
        if(!oldMapURL.equals(newMapURL)){
            String newMapID = newDesk.getBuilding() + newDesk.getDeskID() + Instant.now().getEpochSecond();
            AdminImageQuery.addMapImage(newMapID, newMapURL);
            AdminDeskQuery.setDeskMapID(newDesk, newMapID);
        }
        

        return new StringObject("Successfully modify " + oldDesk + " to " + newDesk);

    }

    /**
     * View desks after filtered by vd
     *
     * @param vd
     * @return array of desks
     */
    @RequestMapping(value = "/view_desks", method = RequestMethod.POST, produces = "application/json")
    public List<DeskMap> viewDesks(@RequestBody ViewDesks vd) throws Exception {
        return AdminDeskQuery.viewDesks(vd.getBuilding(), vd.getFloor(), vd.getSection());
    }
    
    //--------------------------------------------------------------------------
    //
    //      Admin Page - Database Management
    //
    //--------------------------------------------------------------------------
    @RequestMapping(value = "/backup_database", method = RequestMethod.GET, produces = "application/json")
    public StringObject backupDatabase() throws Exception {
        String backupLocation = AdminDatabaseQuery.backupDatabase();
        return new StringObject("Successfully backed up database, sql file saved at: " + backupLocation);
    }
    
    @RequestMapping(value = "/archive_inactive_bookings", method = RequestMethod.GET, produces = "application/json")
    public StringObject archiveInactiveBookings() throws Exception {
        AdminDatabaseQuery.archiveInactiveBookings();
        return new StringObject("Successfully transferred inactive bookings to archive table");
    }
    

    @ExceptionHandler(Exception.class)
    public ExceptionObject handleEmployeeNotFoundException(HttpServletRequest request, Exception ex) {

        return new ExceptionObject(ex.getMessage());
    }
}
