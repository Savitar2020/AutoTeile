package ch.bzz.autoTeile.model;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;
import java.math.BigDecimal;
/**
 * a teil in the Autoteile
 * <p>
 * AutoTeile
 *
 * @author Jason A. Caviezel
 */
public class AutoTeile extends Hersteller {
    @FormParam("teilUUID")
    private String teilUUID;
    @FormParam("bezeichnung")
    @NotEmpty
    @Size(min=2, max=30)
    private String bezeichnung;
    @FormParam("price")
    @DecimalMax(value= "100000")
    @DecimalMin(value= "1")
    private BigDecimal preis;
    @FormParam("hersteller")
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
