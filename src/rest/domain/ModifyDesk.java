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
public class ModifyDesk {
    private Desk oldDesk;
    private Desk newDesk;
    private String mapURL;

    public ModifyDesk(){}

    public ModifyDesk(Desk oldDesk, Desk newDesk, String mapURL) {
        this.oldDesk = oldDesk;
        this.newDesk = newDesk;
        this.mapURL = mapURL;
    }

    public Desk getOldDesk() {
        return oldDesk;
    }

    public Desk getNewDesk() {
        return newDesk;
    }

    public String getMapURL() {
        return mapURL;
    }
}
