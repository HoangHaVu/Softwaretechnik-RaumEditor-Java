package roomieboomie.business.room;

import roomieboomie.business.highscore.HighscoreList;
import roomieboomie.business.item.layout.LayoutItem;
import roomieboomie.business.item.placable.PlacableItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Raum mit allen Informationen inklusive Grundriss-Array in layout
 */
public class Room {
    private RoomPreview roomPreview;
    private ArrayList<PlacableItem> itemList;
    private ArrayList<LayoutItem> walls;
    private ArrayList<LayoutItem> windows;
    private ArrayList<LayoutItem> doors;
    private byte[][] layout;
    private int height;
    private int width;
    private int startWidth;
    private int startHeight;

    /**
     * Erstellt einen neuen Room aus einer RoomPreview heraus. Diese berechnet layout und wird fuer
     * das Verwalten weiterer Attribute mitgegeben.
     * @param roomPreview roomPreview des Rooms
     * @param layout 2D-Array des Grundrisses
     * @param //height Hoehe des Raums im layout
     * @param //width Breite des Raums im layout
     * @param //startX Index, an der der Raum im layout von links aus gesehen beginnt
     * @param //startY Index, an der der Raum im layout von oben aus gesehen beginnt
     */
    public Room(RoomPreview roomPreview, byte[][] layout, ArrayList<PlacableItem> itemList/*, int height, int width, int startX, int startY*/) {
        this.roomPreview = roomPreview;
        this.layout = layout;
        this.itemList = itemList;
        /*this.height = height;
        this.width = width;
        this.startWidth = startX;
        this.startHeight = startY;*/
    }

    /**
     * @return RoomPreview des Rooms
     */
    public RoomPreview getRoomPreview() {
        return roomPreview;
    }

    /**
     * Liste mit allen PlacableItems, die im Raum platziert werden muessen
     * @return List mit PlacalbeItem-Objekten
     */
    public List<PlacableItem> getItemList() {
        return itemList;
    }

    /**
     * Setzt die Liste mit allen PlacableItems, die im Raum platziert werden muessen
     * @param itemList List mit PlacalbeItems
     */
    public void setItemList(ArrayList<PlacableItem> itemList) {
        this.itemList = itemList;
    }

    /**
     * @return Liste mit allen LayoutItems des Typs LayoutItemType.WALL
     */
    public ArrayList<LayoutItem> getWalls() {
        return walls;
    }

    /**
     * Setzt die Liste der Wand-Objekte
     * @param walls Liste mit LayoutItems des Typs LayoutItemType.WALL
     */
    public void setWalls(ArrayList<LayoutItem> walls) {
        this.walls = walls;
    }

    /**
     * @return Liste mit allen LayoutItems des Typs LayoutItemType.WINDOW
     */
    public ArrayList<LayoutItem> getWindows() {
        return windows;
    }

    /**
     * Setzt die Liste der Window-Objekte
     * @param windows Liste mit LayoutItems des Typs LayoutItemType.WINDOW
     */
    public void setWindows(ArrayList<LayoutItem> windows) {
        this.windows = windows;
    }

    /**
     * @return Liste mit allen LayoutItems des Typs LayoutItemType.DOOR
     */
    public ArrayList<LayoutItem> getDoors() {
        return doors;
    }

    /**
     * Setzt die Liste der Door-Objekte
     * @param doors Liste mit LayoutItems des Typs LayoutItemType.DOOR
     */
    public void setDoors(ArrayList<LayoutItem> doors) {
        this.doors = doors;
    }

    /**
     * @return 2D-Byte-Array mit den Grundriss-Informationen
     */
    public byte[][] getLayout() {
        return layout;
    }

    /**
     * Setzt das Grundriss-Layout
     * @param layout 2D-Byte-Array mit den Grundriss-Informationen
     */
    public void setLayout(byte[][] layout) {
        this.layout = layout;
    }

    /**
     * @return Hoehe des Raums im layout
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return Breite des Raums im layout
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return Name des Raums
     */
    public String getName(){
        return this.roomPreview.getName();
    }

    /**
     * @return true, wenn der Raum im Level-Modus spielbar ist; false, wenn im Kreativ-Modus
     */
    public boolean isLevel(){
        return roomPreview.isLevel();
    }

    /**
     * Aendert den Status des Raums. True, wenn der Raum im Level-Modus spielbar ist; false, wenn im Kreativ-Modus
     * @param value Wert des Raumtyps
     */
    public void setLevel(boolean value){
        this.roomPreview.setLevel(value);
    }

    @Override
    public int hashCode() {
        return testHash(layout, itemList);
    }

    /**
     * Kann statisch den HashCode eines Room-Objektes berechnen. Somit kann ueberprueft werden, welchen Hashcode ein
     * erstelltes Room-Objekt mit diesen Attributen haben wuerde
     * @param layout Byte-Array mit dem Grundriss
     * @param itemList ArrayList mit PlacableItems, die in dem Raum platziert werden sollen
     * @return HashCode
     */
    public static int testHash(byte[][] layout, List<PlacableItem> itemList){
        return Arrays.deepHashCode(layout) * itemList.hashCode();
    }

    /**
     * @return Pfad zum Thumbnail-Bild //TODO ?
     */
    public String getThumbnail(){
        return roomPreview.getThumbnail();
    }

    /**
     * @return Hoechster erreichter Score
     */
    public int getHighestScore() {
        return roomPreview.getHighestScore();
    }

    /**
     * @return Score, der benoetigt wird, um den Raum zu bestehen
     */
    public int getNeededScore() {
        return roomPreview.getNeededScore();
    }

    /**
     * @return HighscoreList des Rooms
     */
    public HighscoreList getHighscoreList() {
        return roomPreview.getHighscoreList();
    }
}
