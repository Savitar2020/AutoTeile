package ch.bzz.autoTeile.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import ch.bzz.autoTeile.model.AutoTeile;
import ch.bzz.autoTeile.model.Hersteller;
import ch.bzz.autoTeile.model.Lager;
import ch.bzz.autoTeile.service.Config;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * ch.bzz.autoTeile.data handler for reading and writing the csv files
 * <p>
 *
 * @author Jason A. Caviezel
 */

public class DataHandler {
    private static final DataHandler instance = new DataHandler();
    private static Map<String, AutoTeile> autoTeileMap;
    private static Map<String, Hersteller> herstellerMap;
    private static List<Lager> lagerList;

    /**
     * default constructor: defeat instantiation
     */
    private DataHandler() {
        autoTeileMap = new HashMap<>();
        herstellerMap = new HashMap<>();
        lagerList = new ArrayList<>();
        readJSON();
    }

    public static List<Lager> getLagerList() {
        if (lagerList == null) {
            lagerList = new ArrayList<>();
            readJSON();
        }
        return lagerList;
    }

    public static Lager findAutoteilByHersteller(String bezeichnung) {
        for (Lager lager : getLagerList()) {
            for (AutoTeile teil : lager.getLagerList()) {
                if (teil.getBezeichnung().equals(bezeichnung))
                    return lager;
            }
        }
        return null;
    }

    public static Lager readLager(String lagerUUID) {
        Lager lager = new Lager();
        if (getLagerList().contains(lagerUUID)) {
            lager = getLagerList().get(Integer.parseInt(lagerUUID));
        }
        return lager;
    }

    public static void insertLager(Lager lager) {
        getLagerList().add(Integer.parseInt(lager.getLagerUUID()), lager);
        writeJSON();
    }

    public static void updateLager() {
        writeJSON();
    }

    /**
     * reads a single book identified by its uuid
     * @param bezeichnung  the identifier
     * @return AutoTeile-object
     */
    public static AutoTeile readTeil(String bezeichnung) {
        AutoTeile teil = new AutoTeile();
        if (getAutoTeileMap().containsKey(bezeichnung)) {
            teil = getAutoTeileMap().get(bezeichnung);
        }
        return teil;
    }

    /**
     * saves a part
     * @param teil
     */
    public static void insertTeil(AutoTeile teil) {
        getAutoTeileMap().put(teil.getBezeichnung(), teil);
        writeJSON();
    }

    /**
     * updates the Teilmap
     * @param teil
     */
    public static boolean updateTeil(AutoTeile teil) {
        boolean found = false;
        for (Map.Entry<String, Hersteller> entry : getHerstellerMap().entrySet()) {
            Hersteller hersteller = entry.getValue();
            if (hersteller.getHerstellerUUID().equals(teil.getHerstellerUUID())) {
                hersteller.setHerstellerName("");
                found = true;
            }
        }
        writeJSON();
        return found;
    }

    /**
     * removes a teil from the TeilMap
     *
     * @param teilUUID the uuid of the teil to be removed
     * @return success
     */
    public static boolean deleteTeil(String teilUUID) {
        if (getAutoTeileMap().remove(teilUUID) != null) {
            writeJSON();
            return true;
        } else
            return false;
    }

    /**
     * removes a Autoteil from the Autoteilelist
     *
     * @param teilUUID the UUID of the Autoteil to be removed
     * @return success
     */
    public static boolean deleteLager(String teilUUID) {
        if (getLagerList().remove(teilUUID) != true) {
            writeJSON();
            return true;
        } else
            return false;
    }

    /**
     * @param herstellerName  the identifier
     * @return hersteller-object
     */
    public static Hersteller readHersteller(String herstellerName) {
        Hersteller hersteller = new Hersteller();
        if (getHerstellerMap().containsKey(herstellerName)) {
            hersteller = getHerstellerMap().get(herstellerName);
        }
        return hersteller;
    }

    /**
     * saves a hersteller
     * @param hersteller  the hersteller to be saved
     */
    public static void insertHersteller(Hersteller hersteller) {
        AutoTeile autoTeile = new AutoTeile();
        autoTeile.setTeilUUID(UUID.randomUUID().toString());
        autoTeile.setBezeichnung("");
        autoTeile.setHersteller(hersteller);
        insertTeil(autoTeile);
        writeJSON();
    }

    /**
     * removes a Hersteller from the HerstellerMap
     *
     * @param herstellerUUID the index of the Autoteil to be removed
     * @return success
     */
    public static int deleteHersteller(String herstellerUUID) {
        int errorcode = 1;
        for (Map.Entry<String, AutoTeile> entry : getAutoTeileMap().entrySet()) {
            AutoTeile teil = entry.getValue();
            if (teil.getHersteller().getHerstellerUUID().equals(herstellerUUID)) {
                if (teil.getBezeichnung() == null || teil.getBezeichnung().equals("")) {
                    deleteAutoteil(teil.getHerstellerUUID());
                    errorcode = 0;
                } else {
                    return -1;
                }
            }
        }
        writeJSON();
        return errorcode;
    }

    /**
     * removes a teil from the Autoteilemap
     *
     * @param teilUUID the uuid of the teil to be removed
     * @return success
     */
    public static boolean deleteAutoteil(String teilUUID) {
        if (getAutoTeileMap().remove(teilUUID) != null) {
            writeJSON();
            return true;
        } else
            return false;
    }

    public static boolean updateHersteller(Hersteller hersteller) {
        boolean found = false;
        for (Map.Entry<String, AutoTeile> entry : getAutoTeileMap().entrySet()) {
            AutoTeile teil = entry.getValue();
            if (teil.getHersteller().getHerstellerUUID().equals(hersteller.getHerstellerUUID())) {
                teil.setHersteller(hersteller);
                found = true;
            }
        }
        writeJSON();
        return found;
    }

    /**
     * gets the autoTeileMap
     * @return the autoTeileMap
     */
    public static Map<String, AutoTeile> getAutoTeileMap() {
        return autoTeileMap;
    }

    /**
     * gets the herstellerMap
     * @return the herstellerMap
     */
    public static Map<String, Hersteller> getHerstellerMap() {
        return herstellerMap;
    }

    public static void setHerstellerMap(Map<String, Hersteller> herstellerMap) {
        DataHandler.herstellerMap = herstellerMap;
    }

    /**
     * reads the auto teile and hersteller
     */
    private static void readJSON() {
        try {
            byte[] jsonData = Files.readAllBytes(Paths.get(Config.getProperty("autoTeileJSON")));
            ObjectMapper objectMapper = new ObjectMapper();
            Lager[] lagerlist = objectMapper.readValue(jsonData, Lager[].class);
            AutoTeile[] teile = objectMapper.readValue(jsonData, AutoTeile[].class);
            for (Lager lager : lagerlist) {
                getLagerList().add(lager);
                for (AutoTeile teil : teile) {
                    String herstellerName = teil.getHersteller().getHerstellerName();
                    Hersteller hersteller;
                    if (getHerstellerMap().containsKey(herstellerName)) {
                        hersteller = getHerstellerMap().get(herstellerName);
                    } else {
                        hersteller = teil.getHersteller();
                        getHerstellerMap().put(herstellerName, hersteller);
                    }
                    teil.setHersteller(hersteller);
                    getHerstellerMap().put(teil.getBezeichnung(), teil);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     *
     */
    private static void writeJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        Writer writer;
        FileOutputStream fileOutputStream = null;

        String teilPath = Config.getProperty("autoTeileJSON");
        try {
            fileOutputStream = new FileOutputStream(teilPath);
            writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8));
            objectMapper.writeValue(writer, getLagerList().indexOf(lagerList));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Lager readList(Lager lager) {
        Lager lagers = new Lager();
        if (getLagerList().contains(lager)) {
            lagers = getLagerList().get(1);
        }
        return lager;
    }
}
