package rest.domain;

/**
 * Class for admin to delete existing entry
 * @author yozubear
 */
public class DeleteReservation {
    private DeskSelection reserveEntry;
    private Employee admin;             // admin who deleted the entry

    public DeleteReservation(){}

    public DeleteReservation(DeskSelection reserveEntry, Employee admin) {
        this.reserveEntry = reserveEntry;
        this.admin = admin;
    }

    public DeskSelection getReserveEntry() {
        return reserveEntry;
    }

    public void setReserveEntry(DeskSelection reserveEntry) {
        this.reserveEntry = reserveEntry;
    }

    public Employee getAdmin() {
        return admin;
    }

    public void setAdmin(Employee admin) {
        this.admin = admin;
    }
}
