package roomieboomie.business.room;

import roomieboomie.business.highscore.HighscoreList;
import roomieboomie.business.highscore.HighscoreRecord;
import roomieboomie.business.user.User;
import roomieboomie.persistence.JsonHandler;
import roomieboomie.persistence.JsonValidatingException;

/**
 * "Vorschau" eines Rooms. Beinhaltet die wichtigsten Informationen, um einen Raum im Menü darzustellen.
 * Kann ein Komplettes Raumobjekt erstellen.
 */
public class RoomPreview {
    private String name;
    private String thumbnail;
    private int highestScore;
    private int neededScore;
    private boolean level;
    private HighscoreList highscoreList;
    private JsonHandler jsonHandler;
    private int height; //Hoehe des Raums, wird bei createLayout() gesetzt
    private int width; //Breite des Raums, wird bei createLayout() gesetzt

    /**
     * Erstellt einen neues RoomPreview-Objekt.
     * @param name Name des Raums
     * @param thumbnail Pfad zum Vorschaubild TODO ?
     * @param neededScore Score, der benoetigt wird, um den Raum zu bestehen
     * @param level true, wenn der Raum im Level-Modus spielbar ist; false, wenn im Kreativ-Modus
     * @param highscoreList Highscore-Liste fuer den Room
     */
    public RoomPreview(String name, String thumbnail, HighscoreList highscoreList, int neededScore, boolean level, JsonHandler jsonHandler) {
        this.name = name;
        this.thumbnail = thumbnail;
        this.highscoreList = highscoreList;
        this.highestScore = highscoreList.getHighestScore();
        this.neededScore = neededScore;
        this.level = level;
        this.jsonHandler = jsonHandler;
    }

    /**
     * Liest Daten aus Persistenzschicht und erstellt Informationen fuer ein ganzes Raum-Objekt.
     * Die Preview selbst wird mitgegeben, damit die hier gespeicherten Attribute weiter zugreifbar sind
     * @return
     */
    public Room getFullRoom() throws JsonValidatingException {
        //int startX = 0; //Startpunkt des Raums von links aus TODO
        //int startY = 0; //Startpunkt des Raus von oben aus TODO
        if (level){
            return jsonHandler.getLevelRoom(this.name, this);
        } else {
            return jsonHandler.getCreativeRoom(this.name, this);
        }
    }

    /**
     * @return Name des Raums
     */
    public String getName(){
        return name;
    }

    /**
     * @return Pfad zum Thumbnail-Bild //TODO ?
     */
    public String getThumbnail(){
        return thumbnail;
    }

    /**
     * @return Hoechster erreichter Score
     */
    public int getHighestScore() {
        return highestScore;
    }

    /**
     * @return Score, der benoetigt wird, um den Raum zu bestehen
     */
    public int getNeededScore() {
        return neededScore;
    }

    /**
     * @param neededScore Score, der benoetigt wird, um den Raum zu bestehen
     */
    public void setNeededScore(int neededScore) {
        this.neededScore = neededScore;
    }

    /**
     * @return HighscoreList des Rooms
     */
    public HighscoreList getHighscoreList() {
        return highscoreList;
    }

    /**
     * @return true, wenn der Raum im Level-Modus spielbar ist; false, wenn im Kreativ-Modus
     */
    public boolean isLevel(){
        return this.level;
    }

    /**
     * Aendert den Status des Raums. True, wenn der Raum im Level-Modus spielbar ist; false, wenn im Kreativ-Modus
     * @param value Wert des Raumtyps
     */
    public void setLevel(boolean value){
        this.level = value;
    }

    /**
     * @return Hoehe des Raums im layout
     */
    private int getHeight() {
        return 0; //TODO aus createLayout berechnen
    }

    /**
     * @return Breite des Raums im layout
     */
    private int getWidth() {
        return 0; //TODO aus createLayout berechnen
    }

    /**
     * Fuegt einen neuen Highscore-Eintrag ein
     */
    public void addHighscoreRecord(int time, int points, String username){
        highscoreList.addRecord(new HighscoreRecord(time, points, username));
        highestScore = highscoreList.getHighestScore();
    }

    /**
     * Kann statisch den HashCode eine RoomPreview berechnen. Somit kann ueberprueft werden, welchen Hashcode ein
     * erstelltes RoomPreview-Objekt mit diesen Attributen haben wuerde
     * @param name Name der RoomPreview
     * @param neededScore Score, der benoetigt wird, um den Raum zu bestehen
     * @param level true, wenn der Raum im Level-Modus spielbar ist; false, wenn im Kreativ-Modus
     * @param highscoreList Highscore-Liste fuer den Room
     * @return
     */
    public static int testHash(String name, int neededScore, boolean level, HighscoreList highscoreList) {
        return name.hashCode() * neededScore * Boolean.hashCode(level) * highscoreList.hashCode();
    }

    @Override
    public int hashCode() {
        return testHash(name, /*thumbnail,*/ neededScore, level, highscoreList);
    }
}
