/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.domain;

/**
 *
 * @author yozubear
 */
public class DeskMap {
    private Desk desk;
    private String mapUrl;

    public DeskMap() {}

    public DeskMap(Desk desk, String mapUrl) {
        this.desk = desk;
        this.mapUrl = mapUrl;
    }

    public Desk getDesk() {
        return desk;
    }

    public void setDesk(Desk desk) {
        this.desk = desk;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }
}
