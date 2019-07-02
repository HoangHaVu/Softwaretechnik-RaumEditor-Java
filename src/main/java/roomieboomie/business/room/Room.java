package roomieboomie.business.room;

import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.layout.LayoutItem;
import roomieboomie.business.item.layout.LayoutItemType;
import roomieboomie.business.item.placable.PlacableItem;

import java.util.ArrayList;
import java.util.Arrays;
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
    

    /**
     * Erstellt einen neuen Room aus einer RoomPreview heraus. Diese berechnet layout und wird fuer
     * das Verwalten weiterer Attribute mitgegeben.
     * @param roomPreview roomPreview des Rooms
     * @param layout 2D-Array des Grundrisses
     */
    public Room(RoomPreview roomPreview) {
        
        Room initRoom = roomPreview.getFullRoom();
        this.roomPreview = roomPreview;
        this.itemList = initRoom.getItemList();
        this.walls = initRoom.getWalls();
        this.windows = initRoom.getWindows();
        this.doors = initRoom.getDoors();
        this.layout = initRoom.getLayout();
        
    }
    /**
     * 
     * Erstellt neuen Raum aus übergebenen Gesamtlänge / Breite (nicht die größe des Raumes, sondern die, des gesamten editierbaren Bereiches)
     * 
     */

    public Room(int totalHeight, int totalWidth){
        this.roomPreview = null;
        this.layout = new byte[totalHeight][totalWidth];
        walls = new ArrayList<LayoutItem>();
        doors = new ArrayList<LayoutItem>();
        windows = new ArrayList<LayoutItem>();
        for (int i = 0; i < totalHeight; i++){
            for (int j = 0; j < totalWidth; j++){
                this.layout[i][j] = -1;
            }
        }
    }

    /**
     * 
     * löscht Item anhand der Nummer welche im layout an gewünschter Stelle zu finden ist
     * @param layoutNumber Itemnummer
     */

    public void deleteItem(byte layoutNumber){
    
        if (layoutNumber == -1 || layoutNumber == 0) return;

        LayoutItem item = null;
        byte replaceNumber;
        

        if (layoutNumber == -2){
            item = this.doors.get(0);
        } else if (layoutNumber < -2){
            item = this.windows.get( -layoutNumber - 3 );
        } else if (layoutNumber > 0){
            item = this.walls.get(layoutNumber - 1);
        }


        if (item.getType() == LayoutItemType.DOOR || item.getType() == LayoutItemType.WINDOW){

            if (item.getOrientation() == Orientation.BOTTOM || item.getOrientation() == Orientation.TOP){
                replaceNumber = layout [ item.getY() - 1 ] [ item.getX()];
            } else{
                replaceNumber = layout [ item.getY()] [ item.getX() -1];
            }
        } else {
            replaceNumber = -1;
        }

        int y = item.getY();
        int x = item.getX();
        int endY = y + item.getWidth();
        int endX = x + item.getLength();
        if (item.getOrientation() == Orientation.BOTTOM || item.getOrientation() == Orientation.TOP){
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
     * fügt dem Layout ein neues Item hinzu. Jedes Objekt hat eine eigene Nummer. Jede Wand hat eine Nummer > 0,
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


        if (item.getOrientation() == Orientation.BOTTOM || item.getOrientation() == Orientation.TOP){
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
     * Setter für die RoomPreview 
     */
    public void setRoomPreview(RoomPreview roomPreview){
        this.roomPreview = roomPreview;
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
        byte[][] array = new byte[layout.length][];
        for(int i = 0; i < layout.length; i++){
            array[i] = Arrays.copyOf(layout[i], layout[i].length);
        }
        return array;
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

    public RoomPreview getRoomPreview(){
        return this.roomPreview;
    }

  

    
    
}
