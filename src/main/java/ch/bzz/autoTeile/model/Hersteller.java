package ch.bzz.autoTeile.model;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;

/**
 * a hersteller of Autoteile
 * <p>
 * AutoTeile
 *
 * @author Jason A. Caviezel
 */
public class Hersteller {
    @FormParam("herstellerName")
    @NotEmpty
    @Size(min=2, max=30)
    private String herstellerName;
    @FormParam("telephonnummer")
    @DecimalMax(value= 9999999999)
    @DecimalMin(value= 0)
    private long telephonnummer;
    @FormParam("herstellerUUID")
    private String herstellerUUID;

    public String getHerstellerName() {
        return herstellerName;
    }

    public void setHerstellerName(String herstellerName) {
        this.herstellerName = herstellerName;
    }

    public long getTelephonnummer() {
        return telephonnummer;
    }

    public void setTelephonnummer(long telephonnummer) {
        this.telephonnummer = telephonnummer;
    }

    public String getHerstellerUUID() {
        return herstellerUUID;
    }

    public void setHerstellerUUID(String herstellerUUID) {
        this.herstellerUUID = herstellerUUID;
    }
}
