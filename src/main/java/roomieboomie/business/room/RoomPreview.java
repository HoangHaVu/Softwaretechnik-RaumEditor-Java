package roomieboomie.business.room;

import roomieboomie.business.highscore.HighscoreList;
import roomieboomie.persistence.JsonHandler;

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
    private HighscoreList highscoreList = new HighscoreList();
    private JsonHandler jsonHandler;
    private int height; //Hoehe des Raums, wird createLayout gesetzt
    private int width; //Breite des Raums, wird createLayout gesetzt

    /**
     * Erstellt einen neues RoomPreview-Objekt.
     * @param name Name des Raums
     * @param thumbnail Pfad zum Vorschaubild TODO ?
     * @param highestScore Hoechster erreichter Score
     * @param neededScore Score, der benoetigt wird, um den Raum zu bestehen
     * @param level true, wenn der Raum im Level-Modus spielbar ist; false, wenn im Kreativ-Modus
     * @param highscoreList Highscore-Liste fuer den Room
     */
    public RoomPreview(String name, String thumbnail, int highestScore, int neededScore, boolean level, HighscoreList highscoreList) {
        this.name = name;
        this.thumbnail = thumbnail;
        this.highestScore = highestScore;
        this.neededScore = neededScore;
        this.level = level;
        this.highscoreList = highscoreList;
    }

    /**
     * Liest Daten aus Persistenzschicht und erstellt Informationen fuer ein ganzes Raum-Objekt.
     * Die Preview selbst wird mitgegeben, damit die hier gespeicherten Attribute weiter zugreifbar sind
     * @return
     */
    public Room getFullRoom(){
        int startX = 0; //Startpunkt des Raums von links aus TODO
        int startY = 0; //Startpunkt des Raus von oben aus TODO
        return new Room(this, createLayout(), getHeight(), getWidth(), startX, startY);
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
     * @param highestScore Hoechster erreichter Score
     */
    public void setHighestScore(int highestScore) {
        //TODO das muesste aus der HighscoreList berechnet werden, wenn neuer Eintrag
        this.highestScore = highestScore;
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
     * Liest Daten aus Persistenzschicht und erstellt Informationen fuer den Raum-Grundriss.
     * @return 2D-Byte-Array mit Grundirssinformationen
     */
    private byte[][] createLayout() {
        //TODO ueber jsonHandler laden und umrechnen
        return null;
    }

    /**
     * Fuegt einen neuen Highscore-Eintrag ein
     */
    public void addHighscoreRecord(){
        //TODO highscoreList. ...()
        setHighestScore(0); //TODO
    }
}
