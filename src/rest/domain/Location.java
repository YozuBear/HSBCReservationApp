package rest.domain;

/**
 * Created by Ellie on 2017-02-28.
 * Location class defining the HSBC building locations
 */
public class Location {
    private String officename;
    private String address;
    

    public Location (){}
    
    public Location(String officename, String address, String floor) {
        this.officename = officename;
        this.address = address;
    }

    public String getOfficename() {
        return officename;
    }

    public String getAddress() {
        return address;
    }

    public void setOfficename(String officename) {
        this.officename = officename;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
