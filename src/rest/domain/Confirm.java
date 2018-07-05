package rest.domain;

/**
 * Created by yozubear on 2017-02-07.
 * Confirm reservation class
 */
public class Confirm {
    private ReservationBase base;
    private Desk desk;
    private boolean emailMe;
    private boolean confirm;

    public Confirm(ReservationBase base, Desk desk, boolean emailMe, boolean confirm) {
        this.base = base;
        this.desk = desk;
        this.emailMe = emailMe;
        this.confirm = confirm;
    }

    public Confirm(){}

    public ReservationBase getBase() {
        return base;
    }

    public Desk getDesk() {
        return desk;
    }

    public boolean isEmailMe() {
        return emailMe;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public void setBase(ReservationBase base) {
        this.base = base;
    }

    public void setDesk(Desk desk) {
        this.desk = desk;
    }

    public void setEmailMe(boolean emailMe) {
        this.emailMe = emailMe;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }
}
