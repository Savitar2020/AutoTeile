package ch.bzz.autoTeile.model;

import java.math.BigDecimal;
/**
 * a teil in the Autoteile
 * <p>
 * AutoTeile
 *
 * @author Jason A. Caviezel
 */
public class AutoTeile extends Hersteller {
    private String teilUUID;
    private String bezeichnung;
    private BigDecimal preis;
    private Hersteller hersteller;

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public BigDecimal getPreis() {
        return preis;
    }

    public void setPreis(BigDecimal preis) {
        this.preis = preis;
    }

    public Hersteller getHersteller() {
        return hersteller;
    }

    public void setHersteller(Hersteller hersteller) {
        this.hersteller = hersteller;
    }

    public void setTeilUUID(String teilUUID) {
        this.teilUUID = teilUUID;
    }
}
