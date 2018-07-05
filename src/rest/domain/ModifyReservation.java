package rest.domain;

/**
 * Class for admin to modify existing reservation entries
 * @author yozubear
 */
public class ModifyReservation {
    private DeskSelection oldEntry;
    private DeskSelection newEntry;
    private Employee admin;        // Admin who made the modification

    public ModifyReservation(){}

    public ModifyReservation(DeskSelection oldEntry, DeskSelection newEntry, Employee admin) {
        this.oldEntry = oldEntry;
        this.newEntry = newEntry;
        this.admin = admin;
    }

    public DeskSelection getOldEntry() {
        return oldEntry;
    }

    public void setOldEntry(DeskSelection oldEntry) {
        this.oldEntry = oldEntry;
    }

    public DeskSelection getNewEntry() {
        return newEntry;
    }

    public void setNewEntry(DeskSelection newEntry) {
        this.newEntry = newEntry;
    }

    public Employee getAdmin() {
        return admin;
    }

    public void setAdmin(Employee admin) {
        this.admin = admin;
    }
}
