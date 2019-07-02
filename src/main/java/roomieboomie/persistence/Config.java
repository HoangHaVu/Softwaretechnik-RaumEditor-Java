package roomieboomie.persistence;

import java.util.HashMap;

/**
 * Gibt den Wert eines Attributs aus der Konfigurationsdatei config.json zurueck
 * @param attributeName Name des Attributes
 * @return Wert des Attributs als String
 */

/**
 * Klasse, die Konfigurationsattribute beinhaltet. Instanz ist per Config.get() aufrufbar.
 * Attribute nachach ueber ihren Namen in Caps
 */
public class Config {
    private final String USERPATH;
    private final String LEVELROOMPATH;
    private final String CREATIVEROOMPATH;
    private final int SECONDSPERITEM;
    private final int MAXWIDTH;
    private final int MAXHEIGHT;
    private JsonHandler jsonHandler = new JsonHandler();

    /**
     * Privater Konstruktor, um Singleton zu gewaehren
     */
    private Config(){
        HashMap<String,String> configMap = null;
        try {
            configMap = jsonHandler.getConfigMap();
        } catch (JsonLoadingException e) {
            System.err.println("Fehler beim Laden der configMap.\n" + e.getMessage());
        }
        USERPATH = configMap.get("userPath");
        LEVELROOMPATH = configMap.get("levelRoomPath");
        CREATIVEROOMPATH = configMap.get("creativeRoomPath");
        SECONDSPERITEM = Integer.valueOf(configMap.get("secondsPerItem"));
        MAXWIDTH = Integer.valueOf(configMap.get("maxWidth"));
        MAXHEIGHT = Integer.valueOf(configMap.get("maxHeight"));
    }

    private static Config instance;

    /**
     * @return Instanz von Config, ueber die anschliessend Attribute abfragbar sind.
     */
    public static Config get(){
        if (instance == null){
            instance = new Config();
        }
        return instance;
    }

    public String USERPATH() {
        return USERPATH;
    }

    public String LEVELROOMPATH() {
        return LEVELROOMPATH;
    }

    public String CREATIVEROOMPATH() {
        return CREATIVEROOMPATH;
    }

    public int SECONDSPERITEM() {
        return SECONDSPERITEM;
    }

    public int MAXWIDTH() {
        return MAXWIDTH;
    }

    public int MAXHEIGHT() {
        return MAXHEIGHT;
    }
}
