package roomieboomie.business.editor;
import roomieboomie.business.highscore.HighscoreList;
import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.layout.LayoutItem;
import roomieboomie.business.item.layout.LayoutItemType;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.item.placable.PlacableItemType;
import roomieboomie.business.room.Room;
import roomieboomie.business.room.RoomPreview;
import roomieboomie.business.validation.Validator;
import roomieboomie.persistence.Config;
import roomieboomie.persistence.ImageHandler;
import roomieboomie.persistence.JsonHandler;
import roomieboomie.persistence.JsonWritingException;

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
    private PlacableItem actPlaceableItem;
    private byte [][] previewLayout;
    private final int MAXITEMLENGTH = Config.get().MAXITEMLENGTH();

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
        jsonHandler = new JsonHandler();
        RoomPreview roomPreview = new RoomPreview(name, level, jsonHandler);

        this.layoutItemList = layoutItems;
        this.placableItemList = placableItems;

        this.room = new Room(Config.get().MAXHEIGHT(),Config.get().MAXWIDTH(), roomPreview);
        this.validator = new Validator(room.getLayout());
        this.room.setLevel(level);
        this.previewLayout = new byte[MAXITEMLENGTH][MAXITEMLENGTH];
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
        updatePreviewLayout();
        return true;
    }

    /**
     * Aktualisiert Layout für die Itemvorschau wird vielleicht nicht mehr benötigt
     */
    private void updatePreviewLayout(){

        new Thread() {
            int startX, endX, startY, endY;
            byte itemNumber = -1;
            public void run(){
                for (int i = 0; i < previewLayout.length; i++){
                    for(int j = 0; j < previewLayout[0].length; j++){
                        previewLayout[i][j] = -1;
                    }
                }
                if (actLayoutItem == null){
                    return;
                }

                if (actLayoutItem.getOrientation() == Orientation.BOTTOM || actLayoutItem.getOrientation() == Orientation.TOP){


                    startY = previewLayout[0].length / 2 -  actLayoutItem.getWidth() / 2;
                    endY = startY + actLayoutItem.getWidth();
                    startX = previewLayout.length / 2 - actLayoutItem.getLength() / 2;
                    endX = startX + actLayoutItem.getLength();
                } else{

                    startY = previewLayout.length / 2 - actLayoutItem.getLength() / 2;
                    endY = startY + actLayoutItem.getLength();
                    startX = previewLayout[0].length / 2 -  actLayoutItem.getWidth() / 2;
                    endX = startX + actLayoutItem.getWidth();
                }

                if (actLayoutItem.getType() == LayoutItemType.WALL) itemNumber = 1;
                else if (actLayoutItem.getType() == LayoutItemType.DOOR) itemNumber = -2;
                else if (actLayoutItem.getType() == LayoutItemType.WINDOW) itemNumber = -3;

                for (int i = startY; i < endY; i++) {
                    for (int j = startX; j < endX; j++) {
                        if (i >= 0 && j >= 0 && i < previewLayout[0].length && j < previewLayout.length)
                            previewLayout[j][i] = itemNumber;
                    }
                }
            }

        }.start();
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

        updatePreviewLayout();
    }


    /**
     * dreht aktuell ausgewähltes LayoutItem um 90 Grad
     */
    public void rotateItem(){

        actLayoutItem.turnRight();
        updatePreviewLayout();
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

        /////////////////////////////////TODO loeschen, ist nur Test zum Speichern
        /*ImageHandler.drawThumbnail(room);
        room.addPlacableItem(new PlacableItem(PlacableItemType.BED));
        try {
            jsonHandler.saveRoom(room);
        } catch (JsonWritingException e) {
            e.printStackTrace();
        }*/

        /////////////////////////////////////
    }

    /**
     * ruft addItem Methode des Raumes auf
     * @param item
     */
    public void addItem(LayoutItem item){
        room.addItem(item);
    }

    public void addPlaceableItem(PlacableItem item){
        room.addPlacableItem(item);
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
        updatePreviewLayout();
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

    /**
     * liefert Kopie des aktuellen PreviewLayouts
     */
    public byte[][] getPreviewLayout(){
        return previewLayout.clone();
    }

    public LayoutItem getActLayoutItem(){
        return actLayoutItem;
    }

    public ArrayList<PlacableItem> getPlacableItemList() {
        return placableItemList;
    }

    public void selectnewPlaceableItem(PlacableItemType type){

        if (type == PlacableItemType.TABLE){
            actPlaceableItem = new PlacableItem(PlacableItemType.TABLE);

        }

        updatePreviewPlaceableItems();
    }
    private void updatePreviewPlaceableItems(){

        new Thread() {
            int startX, endX, startY, endY;
            byte itemNumber = -1;
            public void run(){
                for (int i = 0; i < previewLayout.length; i++){
                    for(int j = 0; j < previewLayout[0].length; j++){
                        previewLayout[i][j] = -1;
                    }
                }
                if (actPlaceableItem == null){
                    return;
                }

                if (actPlaceableItem.getOrientation() == Orientation.BOTTOM || actPlaceableItem.getOrientation() == Orientation.TOP){


                    startY = previewLayout[0].length / 2 -  actPlaceableItem.getWidth() / 2;
                    endY = startY + actPlaceableItem.getWidth();
                    startX = previewLayout.length / 2 - actPlaceableItem.getLength() / 2;
                    endX = startX + actLayoutItem.getLength();
                } else{

                    startY = previewLayout.length / 2 - actPlaceableItem.getLength() / 2;
                    endY = startY + actPlaceableItem.getLength();
                    startX = previewLayout[0].length / 2 -  actPlaceableItem.getWidth() / 2;
                    endX = startX + actPlaceableItem.getWidth();
                }

                if (actPlaceableItem.getType() == PlacableItemType.TABLE) itemNumber = 1;


                for (int i = startY; i < endY; i++) {
                    for (int j = startX; j < endX; j++) {
                        if (i >= 0 && j >= 0 && i < previewLayout[0].length && j < previewLayout.length)
                            previewLayout[j][i] = itemNumber;
                    }
                }
            }

        }.start();
    }

}