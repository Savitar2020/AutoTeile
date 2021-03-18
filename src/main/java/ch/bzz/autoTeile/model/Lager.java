package ch.bzz.autoTeile.model;

import java.util.Date;
import java.util.List;

public class Lager {
    private List<AutoTeile> lagerList;
    private Date eintragsdatum;
    public String lagerUUID;

    public Date getEintragsdatum() {
        return eintragsdatum;
    }

    public void setEintragsdatum(Date eintragsdatum) {
        this.eintragsdatum = eintragsdatum;
    }

    public List<AutoTeile> getLagerList() {
        return lagerList;
    }

    public void setLagerList(List<AutoTeile> partsList) {
        this.lagerList = partsList;
    }

    public String getLagerUUID() {
        return lagerUUID;
    }

    public void setLagerUUID(String lagerUUID) {
        this.lagerUUID = lagerUUID;
    }
}
