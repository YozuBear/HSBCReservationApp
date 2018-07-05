package rest.domain;

/**
 * The section displayed on admin page
 * @author yozubear
 */
public class Section {
    String building;
    int floor;
    String section;
    String mapURL;

    public Section(){}

    public Section(String building, int floor, String sectoin, String mapURL) {
        this.building = building;
        this.floor = floor;
        this.section = sectoin;
        this.mapURL = mapURL;
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

    public String getMapURL() {
        return mapURL;
    }

    public void setMapURL(String mapURL) {
        this.mapURL = mapURL;
    }
}
