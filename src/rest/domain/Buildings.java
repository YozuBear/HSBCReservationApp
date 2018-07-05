package rest.domain;

import java.util.Map;

/**
 *
 * @author yozubear
 */
public class Buildings {
    private Map<String, FloorsSections> buildings;
    private Map<String, String> sectionMaps;

    public Buildings() {}

    public Buildings(Map<String, FloorsSections> buildings, Map<String, String> sectionMaps) {
        this.buildings = buildings;
        this.sectionMaps = sectionMaps;
    }

    public Map<String, FloorsSections> getBuildings() {
        return buildings;
    }

    public void setBuildings(Map<String, FloorsSections> buildings) {
        this.buildings = buildings;
    }

    public Map<String, String> getSectionMaps() {
        return sectionMaps;
    }

    public void setSectionMaps(Map<String, String> sectionMaps) {
        this.sectionMaps = sectionMaps;
    }
}
