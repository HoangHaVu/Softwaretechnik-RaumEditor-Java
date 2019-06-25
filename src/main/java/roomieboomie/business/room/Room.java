package roomieboomie.business.room;

import roomieboomie.business.item.layout.LayoutItem;
import roomieboomie.business.item.placable.PlacableItem;

import java.util.List;

/**
 * Raum mit allen Informationen inklusive Grundriss-Array in layout
 */
public class Room {
    private RoomPreview roomPreview;
    private List<PlacableItem> itemList;
    private List<LayoutItem> walls;
    private List<LayoutItem> windows;
    private List<LayoutItem> doors;
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
     * @param height Hoehe des Raums im layout
     * @param width Breite des Raums im layout
     * @param startX Index, an der der Raum im layout von links aus gesehen beginnt
     * @param startY Index, an der der Raum im layout von oben aus gesehen beginnt
     */
    public Room(RoomPreview roomPreview, byte[][] layout, int height, int width, int startX, int startY) {
        this.roomPreview = roomPreview;
        this.layout = layout;
        this.height = height;
        this.width = width;
        this.startWidth = startX;
        this.startHeight = startY;
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
    public void setItemList(List<PlacableItem> itemList) {
        this.itemList = itemList;
    }

    /**
     * @return Liste mit allen LayoutItems des Typs LayoutItemType.WALL
     */
    public List<LayoutItem> getWalls() {
        return walls;
    }

    /**
     * Setzt die Liste der Wand-Objekte
     * @param walls Liste mit LayoutItems des Typs LayoutItemType.WALL
     */
    public void setWalls(List<LayoutItem> walls) {
        this.walls = walls;
    }

    /**
     * @return Liste mit allen LayoutItems des Typs LayoutItemType.WINDOW
     */
    public List<LayoutItem> getWindows() {
        return windows;
    }

    /**
     * Setzt die Liste der Window-Objekte
     * @param windows Liste mit LayoutItems des Typs LayoutItemType.WINDOW
     */
    public void setWindows(List<LayoutItem> windows) {
        this.windows = windows;
    }

    /**
     * @return Liste mit allen LayoutItems des Typs LayoutItemType.DOOR
     */
    public List<LayoutItem> getDoors() {
        return doors;
    }

    /**
     * Setzt die Liste der Door-Objekte
     * @param doors Liste mit LayoutItems des Typs LayoutItemType.DOOR
     */
    public void setDoors(List<LayoutItem> doors) {
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

}
