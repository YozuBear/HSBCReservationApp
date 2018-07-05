package rest.domain;

/**
 * Delete Desks by Admin
 * @author yozubear
 */
public class DeleteDesks {
    private String building;  
    private int floor;
    private String section;
    private int from;       //start desk number
    private int to;         // end desk number

    public DeleteDesks(){}
    public DeleteDesks(String building, int floor, String section, int from, int to) {
        this.building = building;
        this.floor = floor;
        this.section = section;
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
