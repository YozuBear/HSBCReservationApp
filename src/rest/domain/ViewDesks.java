package rest.domain;

/**
 * View all desks filtered by building, floor and section.
 * If field is an empty string, then no filter is applied on that field
 * @author yozubear
 */
public class ViewDesks {
    private String building; 
    private String floor; 
    private String section;

    public ViewDesks(){}
    public ViewDesks(String building, String floor, String section) {
        this.building = building;
        this.floor = floor;
        this.section = section;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
