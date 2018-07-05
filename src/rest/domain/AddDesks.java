package rest.domain;

import org.springframework.web.multipart.MultipartFile;

/**
 * Add desks by Admin
 * @author yozubear
 */
public class AddDesks {
    private String building;  
    private int floor;
    private String section; 
   private String imageUrl; 
    private int from;       //start desk number
    private int to;         // end desk number

    public AddDesks(){}

    public AddDesks(String building, int floor, String section, String imageUrl, int from, int to) {
        this.building = building;
        this.floor = floor;
        this.section = section;
        this.imageUrl = imageUrl;
        this.from = from;
        this.to = to;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }
}
