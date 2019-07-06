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
    private final String ICONTEXTUREPATH;
    private final int SECONDSPERITEM;
    private final int MAXWIDTH;
    private final int MAXHEIGHT;
    private final String WINDOWCOLOR;
    private final String DOORCOLOR;
    private final String BGCOLOR;
    private final String INTERIORCOLOR;
    private final String WALLCOLOR;
    private final int MAXITEMLENGTH;
    private final byte LAYOUTINTERIOR;
    private final byte LAYOUTEXTERIOR;
    private final byte EDITORMINWALL;
    private final byte EDITORDOOR;
    private final byte EDITORMAXWINDOW;
    private final byte GAMEDOOR;
    private final byte GAMEWINDOW;
    private final byte GAMEWALL;

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
        ICONTEXTUREPATH = configMap.get("iconTexturePath");
        SECONDSPERITEM = Integer.valueOf(configMap.get("secondsPerItem"));
        MAXWIDTH = Integer.valueOf(configMap.get("maxWidth"));
        MAXHEIGHT = Integer.valueOf(configMap.get("maxHeight"));
        WINDOWCOLOR = configMap.get("windowColor");
        DOORCOLOR = configMap.get("doorColor");
        BGCOLOR = configMap.get("bgColor");
        INTERIORCOLOR = configMap.get("interiorColor");
        WALLCOLOR = configMap.get("wallColor");
        MAXITEMLENGTH = Integer.valueOf(configMap.get("maxItemLength"));
        LAYOUTINTERIOR = Byte.valueOf(configMap.get("layoutInterior"));
        LAYOUTEXTERIOR = Byte.valueOf(configMap.get("layoutExterior"));
        EDITORMINWALL = Byte.valueOf(configMap.get("editorMinWall"));
        EDITORDOOR = Byte.valueOf(configMap.get("editorDoor"));
        EDITORMAXWINDOW = Byte.valueOf(configMap.get("editorMaxWindow"));
        GAMEDOOR = Byte.valueOf(configMap.get("gameDoor"));
        GAMEWINDOW = Byte.valueOf(configMap.get("gameWindow"));
        GAMEWALL = Byte.valueOf(configMap.get("gameWall"));
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

    public String ICONTEXTUREPATH(){
        return ICONTEXTUREPATH;
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

    public byte LAYOUTINTERIORVALUE() {
        return LAYOUTINTERIOR;
    }

    public byte LAYOUTEXTERIORVALUE() {
        return LAYOUTEXTERIOR;
    }

    public byte EDITORMINWALLVALUE() {
        return EDITORMINWALL;
    }

    public byte EDITORDOORVALUE() {
        return EDITORDOOR;
    }

    public byte EDITORMAXWINDOWVALUE() {
        return EDITORMAXWINDOW;
    }

    public byte GAMEDOORVALUE() {
        return GAMEDOOR;
    }

    public byte GAMEWINDOWVALUE() {
        return GAMEWINDOW;
    }

    public byte GAMEWALLVALUE() {
        return GAMEWALL;
    }
}
