package roomieboomie.business.editor;

import roomieboomie.business.RoomieBoomieManager;
import roomieboomie.business.exception.validationExceptions.*;
import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.layout.LayoutItem;
import roomieboomie.business.item.layout.LayoutItemType;
import roomieboomie.business.item.placeable.PlaceableItem;
import roomieboomie.business.item.placeable.PlaceableItemType;
import roomieboomie.business.room.Room;
import roomieboomie.business.room.RoomPreview;
import roomieboomie.business.validation.Validator;
import roomieboomie.persistence.Config;
import roomieboomie.persistence.JsonHandler;
import roomieboomie.persistence.exception.JsonLoadingException;
import roomieboomie.persistence.exception.JsonValidatingException;
import roomieboomie.persistence.exception.JsonWritingException;

import java.util.ArrayList;
import java.util.List;

/**
 * Editor zum Erstellen/ Bearbeiten eines Raumes.
 */
public class RoomEditor {

    private RoomieBoomieManager roomieBoomieManager;
    private ArrayList<PlaceableItem> placeableItemList;
    private Validator validator;
    private Room room;
    private JsonHandler jsonHandler;
    private LayoutItem currLayoutItem;
    public final int MAXITEMLENGTH = Config.get().MAXITEMLENGTH();

    private PlaceableItemEditor placeableItemEditor;

    byte layoutDoor = Config.get().EDITORDOORVALUE();
    byte layoutInterior = Config.get().LAYOUTINTERIORVALUE();
    byte layoutExterior = Config.get().LAYOUTEXTERIORVALUE();
    byte maxWindow = Config.get().EDITORMAXWINDOWVALUE();
    byte minWall = Config.get().EDITORMINWALLVALUE();

    /**
     * Erstellt und initialisiert RoomEditor
     */
    public RoomEditor(JsonHandler jsonHandler, RoomieBoomieManager roomieBoomieManager) {
        this.placeableItemList = new ArrayList<>();
        this.jsonHandler = jsonHandler;
        this.validator = new Validator();
        selectnewItem(LayoutItemType.WALL);
        initDefaultPlaceableItem();
        this.placeableItemEditor = new PlaceableItemEditor();
        this.roomieBoomieManager = roomieBoomieManager;
    }

    public void loadNewRoom(String name, boolean level) {
        RoomPreview newPreview = new RoomPreview(name, level, jsonHandler);
        this.room = new Room(Config.get().MAXHEIGHT(), Config.get().MAXWIDTH(), newPreview);

    }

    public void loadRoom(RoomPreview roomPreview, boolean editLayout) throws JsonValidatingException, JsonLoadingException {
        this.room = roomPreview.getFullRoom();
        if (editLayout) {
            byte[][] tempLayout = room.getLayout();
            byte[][] unvalidatedLayout = new byte[tempLayout.length][tempLayout[0].length];

            for (int i = 0; i < tempLayout.length; i++) {
                for (int j = 0; j < tempLayout[0].length; j++) {
                    if (tempLayout[i][j] == 0) unvalidatedLayout[i][j] = -1;
                    else unvalidatedLayout[i][j] = tempLayout[i][j];
                }
            }

            room.setLayout(unvalidatedLayout);
            room.setPlaceableItemList(new ArrayList<PlaceableItem>());
        }
        if(room.getPlaceableItemList().size()!=0){
            this.placeableItemList = room.getPlaceableItemList();
        }

    }

    /**
     * Ändert Breite des aktuellen Items. Wert muss zwischen 0 und 1 liegen
     * @param value
     */
    public boolean changeLength(float value) {

        if (currLayoutItem.getType() == LayoutItemType.DOOR) return false;
        if (currLayoutItem.getLength() + 1 - value < 1 && value - currLayoutItem.getLength() < 1) return false;
        currLayoutItem.setHeight((int) ((MAXITEMLENGTH - 1) * value) + 1);
        return true;
    }

    /**
     * Erstellt LayoutItem über mitgegebenen Typ
     * @param type bestimmt ob item vom typ Wand, Fenster oder Tür ist
     */
    public void selectnewItem(LayoutItemType type) {
        if (type == LayoutItemType.WALL) {
            currLayoutItem = new LayoutItem(type, 10, 1, Orientation.TOP); //TODO CONFIG
        } else if (type == LayoutItemType.WINDOW) {
            currLayoutItem = new LayoutItem(type, 4, 1, Orientation.RIGHT); //TODO CONFIG
        } else if (type == LayoutItemType.DOOR) {
            currLayoutItem = new LayoutItem(type, 2, 1, Orientation.RIGHT); //TODO CONFIG
        }
    }

    /**
     * dreht aktuell ausgewähltes LayoutItem um 90 Grad nach rechts
     */
    public void rotateItem() {
        currLayoutItem.turnRight();
    }

    /**
     * platziert aktuell ausgewähltes LayoutItem an übergebenen Koordinaten
     * @param x Koordinate
     * @param y Koordinate
     */
    public void placeCurrItem(int x, int y) throws WindowMissplaceException, LayoutItemMissplaceException, DoorMissplaceException, WallMissplaceException {
        currLayoutItem.setX(x);
        currLayoutItem.setY(y);
        if(validator.validateLayoutPlacement(currLayoutItem,getRoom().getLayout())){
            addItem(currLayoutItem);
            currLayoutItem = currLayoutItem.clone();
            return;
        }
    }

    /**
     * Validiert den aktuellen Room ueber den Validator. Dabei werden height und width des Rooms gesetzt.
     * @return true, wenn der Raum erfolgreich validiert wurde
     */
    public boolean validateRoom() throws MissingDoorException, MissingWindowException, getIntoRoomException {
        boolean sucess = validator.validateRoom(this.room);
        placeableItemEditor.setRoom(this.room);
        placeableItemEditor.setValidator(validator);

        return sucess;
    }

    /**
     * Speichert den aktuellen Raum ueber den JsonHandler
     * @throws JsonWritingException Wenn der Room im JsonHandler nicht geschrieben werden kann
     */
    public void saveRoom() throws JsonWritingException {
        jsonHandler.saveRoom(this.room);
        roomieBoomieManager.updateRoomMaps();
    }

    /**
     * ruft .addItem()-Methode des Raumes auf
     * @param item
     */
    public void addItem(LayoutItem item) {
        room.addItem(item);
    }

    /**
     * Löscht item aus Layout und benutzt es als aktuelles Item
     *
     * @param layoutNumber
     * @return
     */
    public void editItem(byte layoutNumber) {
        List<LayoutItem> roomItemList;
        byte index;
        LayoutItem itemToEdit;

        if (layoutNumber == layoutDoor) {
            roomItemList = room.getDoors();
            index = (byte) ((-layoutNumber) - 2);
        } else if (layoutNumber >= minWall) {
            roomItemList = room.getWalls();
            index = (byte) (layoutNumber - 1);
        } else if (layoutNumber <= maxWindow) {
            roomItemList = room.getWindows();
            index = (byte) ((-layoutNumber) - 3);
        } else {
            return;
        }

        itemToEdit = roomItemList.get(index);
        deleteItem(layoutNumber);

        currLayoutItem = itemToEdit;
    }

    /**
     * Löscht Item über mitgegebene Nummer aus dem Layout
     * @param layoutNumber
     */
    public void deleteItem(byte layoutNumber) {
        room.deleteItem(layoutNumber);
    }

    public Room getRoom() {
        return this.room;
    }

    public LayoutItem getCurrLayoutItem() {
        return currLayoutItem;
    }

    public ArrayList<PlaceableItem> getPlaceableItemList() {
        return placeableItemList;
    }

    public void initDefaultPlaceableItem() {
        for (PlaceableItemType i : PlaceableItemType.values()) {
            this.placeableItemList.add(new PlaceableItem(i));
        }
    }

    public PlaceableItemEditor getPlaceableEditor() {
        return placeableItemEditor;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

}