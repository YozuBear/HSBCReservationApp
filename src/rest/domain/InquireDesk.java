package rest.domain;

/**
 * Inquire Desk class to inquire reservation info on the given day
 * @author yozubear
 */
public class InquireDesk {
    private Desk desk;
    private String date; // mm/dd/yyyy

    public InquireDesk(){}
    public InquireDesk(Desk desk, String date) {
        this.desk = desk;
        this.date = date;
    }

    public Desk getDesk() {
        return desk;
    }

    public void setDesk(Desk desk) {
        this.desk = desk;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
