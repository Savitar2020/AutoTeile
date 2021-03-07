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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * reads a single book identified by its uuid
     * @param bezeichnung  the identifier
     * @return AutoTeile-object
     */
    public static AutoTeile readBook(String bezeichnung) {
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
    public static void saveTeil(AutoTeile teil) {
        getAutoTeileMap().put(teil.getBezeichnung(), teil);
        writeJSON();
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
    public static void savePublisher(Hersteller hersteller) {
        getHerstellerMap().put(hersteller.getHerstellerName(), hersteller);
        writeJSON();
    }

    /**
     * gets the autoTeileMap
     * @return the bookMap
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

    public static void setPublisherMap(Map<String, Hersteller> publisherMap) {
        DataHandler.herstellerMap = publisherMap;
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
            objectMapper.writeValue(writer, getAutoTeileMap().values());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
