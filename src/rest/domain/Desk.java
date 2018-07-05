package rest.domain;

/**
 * Created by yozubear on 2017-02-07.
 * A desk at HSBC building
 */
public class Desk {
    private String building;// e.g. "Vancouver Broadway Green"
    private String deskID;  // e.g. "1.A.101"

    public Desk(){}

    public Desk(String building, String deskID) {
        this.building = building;
        this.deskID = deskID;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getDeskID() {
        return deskID;
    }

    public void setDeskID(String deskID) {
        this.deskID = deskID;
    }

    @Override
    public String toString() {
        return "building: " + building + ", deskID: " + deskID ;
    }
    
    @Override
    public boolean equals(Object desk) {
        if(desk instanceof Desk){
            return this.deskID.equals(((Desk) desk).deskID) && 
                    this.building.equals(((Desk) desk).building);
        
        }else{
            return false;
        }
        
    }
}
