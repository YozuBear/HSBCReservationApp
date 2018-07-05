/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.domain;

/**
 *
 * @author Jennifer Ling
 */
public class StringObject {
    private String string;

    public StringObject(){}

    public StringObject(String str) {
        this.string = str;
    }

    public String getString() {
        return string;
    }

    public void setString(String str) {
        this.string = str;
    }
    
    @Override
    public String toString() {
        return string ;
    }
}
