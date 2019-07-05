package roomieboomie.business.editor;

import roomieboomie.business.highscore.HighscoreList;
import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.layout.LayoutItem;
import roomieboomie.business.item.layout.LayoutItemType;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.room.Room;
import roomieboomie.business.room.RoomPreview;
import roomieboomie.business.validation.Validator;
import roomieboomie.persistence.JsonHandler;

import java.util.ArrayList;
import java.util.List;


/**
 * Editor zum erstellen / bearbeiten eines Raumes.
 */
   
public class RoomEditor {

    private ArrayList<LayoutItem> layoutItemList;
    private ArrayList<PlacableItem> placableItemList;
    private Validator validator;
    private Room room;
    private JsonHandler jsonHandler;
    private LayoutItem actLayoutItem;
    public final int MAXITEMLENGTH = 19;
    


    /**
     * Erstellt und initialisiert RoomEditor zum editieren eines bereits vorhandenen Raumes.
     * @param room
     * @param layoutItems
     * @param placableItems
     */
    public RoomEditor(Room room, ArrayList <LayoutItem> layoutItems, ArrayList<PlacableItem> placableItems){

        byte[][] tempLayout = room.getLayout();
        byte [][]unvalidatedLayout = new byte[tempLayout.length][tempLayout[0].length];

        for (int i = 0; i < tempLayout.length; i++){
            for(int j = 0; j < tempLayout[0].length; j++){
                if (tempLayout[i][j] == 0) unvalidatedLayout[i][j] = -1;
                else unvalidatedLayout[i][j] = tempLayout[i][j];
            }
        }
        
        room.setLayout(unvalidatedLayout);
        this.room = room;
        this.layoutItemList = layoutItems;
        this.placableItemList = placableItems;
        jsonHandler = new JsonHandler();
        room.setLayout(unvalidatedLayout);
        this.validator = new Validator(room.getLayout());
        selectnewItem(LayoutItemType.WALL);
    }

    /**
     * Erstellt komplett neuen Raum
     * 
     * 
     * @param name des neuen Raumes     
     * @param level boolean ob der Raum im Level Modus spielbar sein soll
     * @param layoutItems
     * @param placableItems
     */

    public RoomEditor(String name, boolean level, ArrayList <LayoutItem> layoutItems, ArrayList<PlacableItem> placableItems){

        RoomPreview roomPreview = new RoomPreview(name, level);
        roomPreview.setJsonHandler(jsonHandler);
        

        this.layoutItemList = layoutItems;
        this.placableItemList = placableItems;
        jsonHandler = new JsonHandler();
        //this.room = new Room(Integer.valueOf(jsonHandler.getConfigAttribute("maxHeight")), Integer.valueOf(jsonHandler.getConfigAttribute("maxWidth")));
        this.room = new Room(60, 60);
        this.room.setRoomPreview(roomPreview);
        this.validator = new Validator(room.getLayout());
        this.room.setLevel(level);
        selectnewItem(LayoutItemType.WALL);

    }
    /**
     * Ändert Breite des aktuellen Items. Wert muss zwischen 0 und 1 liegen
     * @param value
     */
    public boolean changeLength(float value){

        if (actLayoutItem.getType() == LayoutItemType.DOOR) return false;
        if (actLayoutItem.getLength() + 1 - value < 1 && value - actLayoutItem.getLength() < 1) return false;
        actLayoutItem.setHeight((int) ((MAXITEMLENGTH - 1) * value) + 1);
        return true;
    }


    /**
     * Erstellt LayoutItem über mitgegebenen Typ
     *
     * @param type bestimmt ob item vom typ Wand, Fenster oder Tür ist
     * 
     */
    
    public void selectnewItem(LayoutItemType type){

        if (type == LayoutItemType.WALL){
            actLayoutItem = new LayoutItem(type, 10, 1, Orientation.TOP);
        } else if (type == LayoutItemType.WINDOW){
            actLayoutItem = new LayoutItem(type, 4, 1, Orientation.RIGHT);
        } else if (type == LayoutItemType.DOOR){
            actLayoutItem = new LayoutItem(type, 2, 1, Orientation.RIGHT);
        }

    }


    /**
     * dreht aktuell ausgewähltes LayoutItem um 90 Grad
     */

    public void rotateItem(){

        actLayoutItem.turnRight();
    }


    /**
     * platziert aktuell ausgewähltes LayoutItem an übergebenen Koordinaten
     * @param x Koordinate
     * @param y Koordinate
     */

    public void placeActItem(int x, int y){
        actLayoutItem.setX(x);
        actLayoutItem.setY(y);

        if (!validator.validatePlacement(actLayoutItem)){
            return;
        }

        addItem(actLayoutItem);
        actLayoutItem = actLayoutItem.clone();
    }

    

    /**
     * speichert Raum über JSonHandler falls dieser erfolgreich validiert wurde
     */

    public void saveRoom(){


        if (!validator.validateRoom(this.room)) return;
        
        
        room.getRoomPreview().setHighscoreList(new HighscoreList());
        room.getRoomPreview().setJsonHandler(this.jsonHandler);
        try{
            jsonHandler.saveRoom(this.room);
        } catch(Exception e){
            
        }        

    }


    /**
     * ruft addItem Methode des Raumes auf
     * @param item
     */
    
    public void addItem(LayoutItem item){
        room.addItem(item);
    }
    
    /**
     * Löscht item aus Layout und benutzt es als aktuelles Item
     * @param layoutNumber
     * @return
     */
    
    public void editItem(byte layoutNumber){
        List<LayoutItem> roomItemList = null;
        byte index = 0;
        LayoutItem itemToEdit = null;

        if (layoutNumber == -2){
            roomItemList = room.getDoors();
            index = (byte) ((-layoutNumber) - 2);
        }
            
        else if (layoutNumber > 0){
            roomItemList = room.getWalls();
            index = (byte) (layoutNumber - 1);
        }
            
        else if (layoutNumber < -2){
            roomItemList = room.getWindows();
            index = (byte) ((-layoutNumber) - 3);
        } else{
            return;
        }
        
        itemToEdit = roomItemList.get(index);
        deleteItem(layoutNumber);

        actLayoutItem = itemToEdit;
    }  

    /**
     * Löscht Item über mitgegebene Nummer aus dem Layout
     * 
     * 
     * @param layoutNumber
     */

    public void deleteItem(byte layoutNumber){
        room.deleteItem(layoutNumber);
    }
       
    public Room getRoom (){
        return this.room;
    }

    public LayoutItem getActLayoutItem(){
        return actLayoutItem;
    }
}
