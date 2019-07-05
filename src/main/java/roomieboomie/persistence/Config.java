package roomieboomie.persistence;

import roomieboomie.persistence.exception.JsonLoadingException;

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
    private JsonHandler jsonHandler = new JsonHandler();
    private final String USERPATH;
    private final String LEVELROOMPATH;
    private final String CREATIVEROOMPATH;
    private final String LEVELTHUMBNAILPATH;
    private final String CREATIVETHUMBNAILPATH;
    private final int SECONDSPERITEM;
    private final int MAXWIDTH;
    private final int MAXHEIGHT;
    private final String WINDOWCOLOR;
    private final String DOORCOLOR;
    private final String BGCOLOR;
    private final String INTERIORCOLOR;
    private final String WALLCOLOR;
    private final int MAXITEMLENGTH;

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
        LEVELTHUMBNAILPATH = configMap.get("levelThumbnailPath");
        CREATIVETHUMBNAILPATH = configMap.get("creativeThumbnailPath");
        SECONDSPERITEM = Integer.valueOf(configMap.get("secondsPerItem"));
        MAXWIDTH = Integer.valueOf(configMap.get("maxWidth"));
        MAXHEIGHT = Integer.valueOf(configMap.get("maxHeight"));
        WINDOWCOLOR = configMap.get("windowColor");
        DOORCOLOR = configMap.get("doorColor");
        BGCOLOR = configMap.get("bgColor");
        INTERIORCOLOR = configMap.get("interiorColor");
        WALLCOLOR = configMap.get("wallColor");
        MAXITEMLENGTH = Integer.valueOf(configMap.get("maxItemLength"));
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

    public String CREATIVETHUMBNAILPATH(){
        return CREATIVETHUMBNAILPATH;
    }

    public String LEVELTHUMBNAILPATH(){
        return LEVELTHUMBNAILPATH;
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

    public String WINDOWCOLOR() {
        return WINDOWCOLOR;
    }

    public String DOORCOLOR() {
        return DOORCOLOR;
    }

    public String BGCOLOR() {
        return BGCOLOR;
    }

    public String INTERIORCOLOR() {
        return INTERIORCOLOR;
    }

    public String WALLCOLOR() {
        return WALLCOLOR;
    }

    public int MAXITEMLENGTH() {
        return MAXITEMLENGTH;
    }
}
