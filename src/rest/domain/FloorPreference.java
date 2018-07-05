package rest.domain;

/**
 * Created by yozubear on 2017-02-07.
 * Client's preferred reservation location and time
 */
public class FloorPreference {
    private ReservationBase base;
    private String building;
    private int floor;
    private String section;

    public FloorPreference(){}
    public FloorPreference(ReservationBase base, String building, int floor, String section) {
        this.base = base;
        this.floor = floor;
        this.section = section;
    }

    public ReservationBase getBase() {
        return base;
    }

    public String getBuilding() {
        return building;
    }
    public int getFloor() {
        return floor;
    }

    public String getSection() {
        return section;
    }

    public void setBase(ReservationBase base) {
        this.base = base;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void setSection(String section) {
        this.section = section;
    }

}
