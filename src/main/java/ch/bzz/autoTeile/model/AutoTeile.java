package ch.bzz.autoTeile.model;

public class AutoTeile extends Hersteller {
    private String teilUUID;
    private String bezeichnung;
    private float preis;
    private Hersteller hersteller;

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public float getPreis() {
        return preis;
    }

    public void setPreis(float preis) {
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
