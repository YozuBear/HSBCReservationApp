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
public class ExceptionObject {
    String exception;

    public ExceptionObject(){}
    public ExceptionObject(String exception) {
        this.exception = exception;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}
