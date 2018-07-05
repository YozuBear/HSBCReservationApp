package rest.domain;

/**
 * Created by yozubear on 2017-02-07.
 * Desk selection class
 */
public class DeskSelection {
    private ReservationBase base;
    private Desk desk;

    public DeskSelection(){}

    public DeskSelection(ReservationBase base, Desk desk) {
        this.base = base;
        this.desk = desk;
    }

    public ReservationBase getBase() {
        return base;
    }

    public Desk getDesk() {
        return desk;
    }

    public void setBase(ReservationBase base) {
        this.base = base;
    }

    public void setDesk(Desk desk) {
        this.desk = desk;
    }

    @Override
    public String toString() {
        return this.base + "\n" + this.desk + "\n";
    }
}
