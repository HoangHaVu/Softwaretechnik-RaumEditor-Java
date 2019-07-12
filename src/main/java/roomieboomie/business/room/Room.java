package roomieboomie.business.room;

import javafx.scene.image.Image;
import roomieboomie.business.highscore.HighscoreList;
import roomieboomie.business.item.layout.LayoutItem;
import roomieboomie.business.item.layout.LayoutItemType;
import roomieboomie.business.item.placeable.PlaceableItem;
import roomieboomie.persistence.Config;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Raum mit allen Informationen inklusive Grundriss-Array in layout
 */
public class Room {
    private RoomPreview roomPreview;
    private ArrayList<PlaceableItem> placeableItemList;
    private ArrayList<LayoutItem> walls;
    private ArrayList<LayoutItem> windows;
    private ArrayList<LayoutItem> doors;
    private byte[][] layout;
    private int height;
    private int width;
    private int startX;
    private int startY;

    byte editorDoor = Config.get().EDITORDOORVALUE();
    byte layoutInterior = Config.get().LAYOUTINTERIORVALUE();
    byte layoutExterior = Config.get().LAYOUTEXTERIORVALUE();
    byte maxWindow = Config.get().EDITORMAXWINDOWVALUE();
    byte minWall = Config.get().EDITORMINWALLVALUE();

    /**
     * Erstellt einen neuen Room aus einer RoomPreview heraus. Diese berechnet layout und wird fuer
     * das Verwalten weiterer Attribute mitgegeben.
     * @param roomPreview roomPreview des Rooms
     */
    public Room(RoomPreview roomPreview, byte[][] layout, int startX, int startY, ArrayList<PlaceableItem> placeableItemList,
                ArrayList<LayoutItem> walls, ArrayList<LayoutItem> windows, ArrayList<LayoutItem> doors) {
        this.roomPreview = roomPreview;
        this.layout = layout;
        this.startX = startX;
        this.startY = startY;
        this.width = layout[0].length;
        this.height = layout.length;
        this.placeableItemList = placeableItemList;
        this.walls = walls;
        this.windows = windows;
        this.doors = doors;
    }

    /**
     * Erstellt neuen Raum aus übergebener Gesamtlänge / -breite
     * (nicht die Größe des Raumes, sondern die des gesamten editierbaren Bereiches)
     * @param totalHeight Hoehe des editierbaren Bereiches
     * @param totalWidth Breite des editierbaren Bereiches
     */
    public Room(int totalHeight, int totalWidth, RoomPreview roomPreview){
        this.roomPreview = roomPreview;
        this.layout = new byte[totalHeight][totalWidth];
        placeableItemList = new ArrayList<>();
        walls = new ArrayList<>();
        doors = new ArrayList<>();
        windows = new ArrayList<>();
        for (int i = 0; i < totalHeight; i++){
            for (int j = 0; j < totalWidth; j++){
                this.layout[i][j] = layoutExterior;
            }
        }
    }

    /**
     * Setter für die RoomPreview
     * @param roomPreview RoomPreview
     */
    public void setRoomPreview(RoomPreview roomPreview){
        this.roomPreview = roomPreview;
    }

    /**
     * @return RoomPreview des Rooms
     */
    public RoomPreview getRoomPreview(){
        return this.roomPreview;
    }

    /**
     * Liste mit allen PlaceableItems, die im Raum platziert werden muessen
     * @return List mit PlacealbeItem-Objekten
     */
    public ArrayList<PlaceableItem> getPlaceableItemList() {
        return placeableItemList;
    }

    /**
     * Setzt die Liste mit allen PlaceableItems, die im Raum platziert werden muessen
     * @param placeableItemList List mit PlacealbeItems
     */
    public void setPlaceableItemList(ArrayList<PlaceableItem> placeableItemList) {
        this.placeableItemList = placeableItemList;
    }

    /**
     * Fuegt dem Layout ein neues Item hinzu. Jedes Objekt hat eine eigene Nummer. Jede Wand hat eine Nummer > 0,
     * Es gibt nur eine Tür, welche die Nummer -2 bekommt.
     * Jedes Fenster hat eine Nummer < -2
     * @param item
     */
    public void addItem(LayoutItem item){
        int y = item.getY();
        int x = item.getX();
        int endY = y + item.getWidth();
        int endX = x + item.getLength();
        byte size;

        if (item.getOrientation().isVertical()){
            endY = y + item.getLength();
            endX = x + item.getWidth();
        }

        if (item.getType() == LayoutItemType.WALL) {
            size = (byte) (this.walls.size() + 1);
            this.walls.add(item);
        } else if (item.getType() == LayoutItemType.DOOR){
            size = (byte) - (this.doors.size() + 2);
            if (this.doors.size() > 0){
                return;
            }
            this.doors.add(item);
        } else if (item.getType() == LayoutItemType.WINDOW){
            size = (byte) - (this.windows.size() + 3);
            this.windows.add(item);
        } else {
            return;
        }

        for (int i = y; i < endY; i++) {
            for (int j = x; j < endX; j++) {
                this.layout[i][j] = size;
            }
        }

    }

    /**
     * Fuegt dem Room ein PlaceableItem hinzu
     * @param placeableItem PlaceableItem
     */
    public void addPlaceableItem(PlaceableItem placeableItem) {
        placeableItemList.add(placeableItem);
    }

    /**
     * löscht Item anhand der Nummer welche im layout an gewünschter Stelle zu finden ist
     * @param layoutNumber Itemnummer
     */
    public void deleteItem(byte layoutNumber){
        if (layoutNumber == layoutExterior || layoutNumber == layoutInterior) return;

        LayoutItem item = null;
        byte replaceNumber;

        if (layoutNumber == editorDoor){
            item = this.doors.get(0);
        } else if (layoutNumber <= maxWindow){
            item = this.windows.get( -layoutNumber - 3 );
        } else if (layoutNumber >= minWall){
            item = this.walls.get(layoutNumber - 1);
        }

        if (item.getType() == LayoutItemType.DOOR || item.getType() == LayoutItemType.WINDOW){

            if (item.getOrientation().isVertical()){
                replaceNumber = layout [ item.getY() - 1 ] [ item.getX()];
            } else{
                replaceNumber = layout [ item.getY()] [ item.getX() -1];
            }
        } else {
            replaceNumber = layoutExterior;
        }

        int y = item.getY();
        int x = item.getX();
        int endY = y + item.getWidth();
        int endX = x + item.getLength();
        if (item.getOrientation().isVertical()){
            endY = y + item.getLength();
            endX = x + item.getWidth();
        }

        if (item.getType() != LayoutItemType.WALL){
            for (int i = y; i < endY; i++) {
                for (int j = x; j < endX; j++) {
                    layout[i][j] = replaceNumber;
                }
            }
        }

        if (item.getType() == LayoutItemType.WALL){

            for (int i = y; i < endY; i++) {
                for (int j = x; j < endX; j++) {
                    if (layout[i][j] != layoutNumber){
                        deleteItem(layout[i][j]);
                    }
                    layout[i][j] = replaceNumber;
                }
            }

            for (int i = 0; i < layout.length; i++){
                for (int j = 0; j < layout[0].length; j++){
                    if (layout[i][j] > layoutNumber){
                        layout[i][j] -= 1;
                    }
                }
            }

            this.walls.remove(item);

        } else if (item.getType() == LayoutItemType.WINDOW){
            for (int i = 0; i < layout.length; i++){
                for (int j = 0; j < layout[0].length; j++){
                    if (layout[i][j] < layoutNumber){
                        layout[i][j] += 1;
                    }
                }
            }

            this.windows.remove(item);
        } else if (item.getType() == LayoutItemType.DOOR){
            this.doors.clear();
        }
    }

    /**
     * Setzt die Liste der Wand-Objekte
     * @param walls Liste mit LayoutItems des Typs LayoutItemType.WALL
     */
    public void setWalls(ArrayList<LayoutItem> walls) {
        this.walls = walls;
    }

    /**
     * @return Liste mit allen LayoutItems des Typs LayoutItemType.WALL
     */
    public ArrayList<LayoutItem> getWalls() {
        return walls;
    }

    /**
     * Setzt die Liste der Window-Objekte
     * @param windows Liste mit LayoutItems des Typs LayoutItemType.WINDOW
     */
    public void setWindows(ArrayList<LayoutItem> windows) {
        this.windows = windows;
    }

    /**
     * @return Liste mit allen LayoutItems des Typs LayoutItemType.WINDOW
     */
    public ArrayList<LayoutItem> getWindows() {
        return windows;
    }

    /**
     * Setzt die Liste der Door-Objekte
     * @param doors Liste mit LayoutItems des Typs LayoutItemType.DOOR
     */
    public void setDoors(ArrayList<LayoutItem> doors) {
        this.doors = doors;
    }

    /**
     * @return Liste mit allen LayoutItems des Typs LayoutItemType.DOOR
     */
    public ArrayList<LayoutItem> getDoors() {
        return doors;
    }

    /**
     * Setzt das Grundriss-Layout
     * @param layout 2D-Byte-Array mit den Grundriss-Informationen
     */
    public void setLayout(byte[][] layout) {
        this.layout = new byte[layout.length][];
        for(int i = 0; i < layout.length; i++){
            this.layout[i] = Arrays.copyOf(layout[i], layout[i].length);
        }
    }

    /**
     * @return 2D-Byte-Array mit den Grundriss-Informationen
     */
    public byte[][] getLayout() {
        byte[][] array = new byte[layout.length][];
        for(int i = 0; i < layout.length; i++){
            array[i] = Arrays.copyOf(layout[i], layout[i].length);
        }
        return array;
    }

    /**
     * Gibt den eigentlichen Grundriss ohne leere Flaeche des editierbaren Breiches zurueck
     * @return "ausgeschnittenes" 2D-Byte-Array aus dem Layout
     */
    public byte[][] getEffectiveLayout(){
        byte[][] effectiveLayout = new byte[height][width];
        for(int i = 0; i < height; i++) {
            int x = 0;
            for (int j = 0; j < width; j++) {
                effectiveLayout[i][j] = layout[startY + i][startX + j];
            }
        }
        return effectiveLayout;
    }

    /**
     * Setzt Hoehe des Raumes innerhalb des editierbaren Bereiches
     * @param height Zu setzende Hoehe
     */
    public void setHeight(int height){
        this.height = height;
    }

    /**
     * Gibt die Hoehe des des Raumes innerhalb des editierbaren Bereiches zurueck
     * @return Hoehe in "Indizes"
     */
    public int getHeight() {
        return height;
    }

    /**
     * Setzt Breite des des Raumes innerhalb des editierbaren Bereichen
     * @param width zu setzende Breite
     */
    public void setWidth (int width){
        this.width = width;
    }

    /**
     * Gibt die Breite des des Raumes innerhalb des editierbaren Bereiches zurueck
     * @return Breite in "Indizes"
     */
    public int getWidth() {
        return width;
    }

    /**
     * Setzt den Wert des Indexes, an dem das Layout innerhalb des editierbaren Bereiches von links aus beginnt
     * @return Ganzzahliger Wert
     */
    public void setStartX(int x){
        this.startX = x;
    }

    /**
     * Gibt den Wert des Indexes zurueck, an dem das Layout innerhalb des editierbaren Bereiches von links aus beginnt
     * @return Ganzzahliger Wert
     */
    public int getStartX(){
        return this.startX;
    }

    /**
     * Setzt den Wert des Indexes, an dem das Layout innerhalb des editierbaren Bereiches von oben aus beginnt
     * @return Ganzzahliger Wert
     */
    public void setStartY(int y){
        this.startY = y;
    }

    /**
     * Gibt den Wert des Indexes zurueck, an dem das Layout innerhalb des editierbaren Bereiches von links aus beginnt
     * @return Ganzzahliger Wert
     */
    public int getStartY(){
        return this.startY;
    }

    /**
     * Kann statisch den HashCode eines Room-Objektes berechnen. Somit kann ueberprueft werden, welchen Hashcode ein
     * erstelltes Room-Objekt mit diesen Attributen haben wuerde
     * @param layout Byte-Array mit dem Grundriss
     * @param placeableItemList ArrayList mit PlaceableItems, die in dem Raum platziert werden sollen
     * @return HashCode
     */
    public static int testHash(byte[][] layout, ArrayList<PlaceableItem> placeableItemList, ArrayList<LayoutItem> walls,
                               ArrayList<LayoutItem> windows, ArrayList<LayoutItem> doors){
        return Arrays.deepHashCode(layout) * placeableItemList.hashCode() * walls.hashCode() * windows.hashCode() * doors.hashCode();
    }

    @Override
    public int hashCode() {
        return testHash(layout, placeableItemList, walls, windows, doors);
    }


    // Ab hier wird zu RoomPreview durchgereicht

    /**
     * Name des Raums
     */
    public void setName(String name){
        roomPreview.setName(name);
    }

    /**
     * @return Name des Raums
     */
    public String getName(){
        return roomPreview.getName();
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
        roomPreview.setLevel(value);
    }

    /**
     * @return Thumbnail-Bild
     */
    public Image getThumbnail(){
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
