package rest.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contain all the floors and sections of a particular Location (building)
 * @author yozubear
 */
public class FloorsSections {
    Map<Integer, List<String>> floors;

    public FloorsSections(){}

    public FloorsSections(Map<Integer, List<String>> floors) {
        this.floors = floors;
    }

    public Map<Integer, List<String>> getFloors() {
        return floors;
    }

    public void setFloors(Map<Integer, List<String>> floors) {
        this.floors = floors;
    }
}
