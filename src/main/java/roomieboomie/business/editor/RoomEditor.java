package roomieboomie.business.editor;

import roomieboomie.business.exception.validationExceptions.*;
import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.layout.LayoutItem;
import roomieboomie.business.item.layout.LayoutItemType;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.item.placable.PlacableItemType;
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

    private ArrayList<PlacableItem> placableItemList;
    private Validator validator;
    private Room room;
    byte[][] placableLayout;
    private JsonHandler jsonHandler;
    private LayoutItem currLayoutItem;
    private PlacableItem currPlaceableItem;
    private byte[][] previewLayout;
    public final int MAXITEMLENGTH = Config.get().MAXITEMLENGTH();
    private PlaceableEditor placeableEditor;

    byte layoutDoor = Config.get().EDITORDOORVALUE();
    byte layoutInterior = Config.get().LAYOUTINTERIORVALUE();
    byte layoutExterior = Config.get().LAYOUTEXTERIORVALUE();
    byte maxWindow = Config.get().EDITORMAXWINDOWVALUE();
    byte minWall = Config.get().EDITORMINWALLVALUE();

    /**
     * Erstellt und initialisiert RoomEditor zum Editieren eines bereits vorhandenen Raumes.
     */
    public RoomEditor() {
        this.placableItemList = new ArrayList<PlacableItem>();
        jsonHandler = new JsonHandler();
        this.validator = new Validator();
        selectnewItem(LayoutItemType.WALL);
        selectnewPlaceableItem(PlacableItemType.TABLE);
        initDefaultPlaceableItem();
    }

    /**
     * Erstellt komplett neuen Raum
     *
     * @param name          Name des neuen Raumes
     * @param level         true, wenn der Raum im Level-Modus spielbar ist; false, wenn im Kreativ-Modus
     * @param placableItems
     */
    public RoomEditor(String name, boolean level, ArrayList<PlacableItem> placableItems) {
        jsonHandler = new JsonHandler();
        RoomPreview roomPreview = new RoomPreview(name, level, jsonHandler);

        this.placableItemList = placableItems;

        this.room = new Room(Config.get().MAXHEIGHT(), Config.get().MAXWIDTH(), roomPreview);
        this.validator = new Validator();
        this.room.setLevel(level);
        selectnewItem(LayoutItemType.WALL);

        this.placeableEditor = new PlaceableEditor(this.room);
    }

    public void loadNewRoom(String name, boolean level) {
        RoomPreview newPreview = new RoomPreview(name, level, jsonHandler);
        this.room = new Room(Config.get().MAXHEIGHT(), Config.get().MAXWIDTH(), newPreview);
        this.placeableEditor = new PlaceableEditor(room);
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
            room.setPlacableItemList(new ArrayList<PlacableItem>());
        }
        if(room.getPlacableItemList().size()!=0){
            this.placableItemList = room.getPlacableItemList();
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
        }

    }

    /**
     * Validiert den aktuellen Room ueber den Validator. Dabei werden height und width des Rooms gesetzt.
     * @return true, wenn der Raum erfolgreich validiert wurde
     */
    public boolean validateRoom() throws MissingWindowException, MissingDoorException, getIntoRoomException {
        return validator.validateRoom(this.room);
    }

    /**
     * Speichert den aktuellen Raum ueber den JsonHandler
     * @throws JsonWritingException Wenn der Room im JsonHandler nicht geschrieben werden kann
     */
    public void saveRoom() throws JsonWritingException {
        jsonHandler.saveRoom(this.room);
    }

    /**
     * ruft .addItem()-Methode des Raumes auf
     * @param item
     */
    public void addItem(LayoutItem item) {
        room.addItem(item);
    }

    public void addPlaceableItem(PlacableItem item) {
        room.addPlacableItem(item);
    }

    /**
     * Löscht item aus Layout und benutzt es als aktuelles Item
     *
     * @param layoutNumber
     * @return
     */
    public void editItem(byte layoutNumber) {
        List<LayoutItem> roomItemList = null;
        byte index = 0;
        LayoutItem itemToEdit = null;

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
     *
     * @param layoutNumber
     */
    public void deleteItem(byte layoutNumber) {
        room.deleteItem(layoutNumber);
    }

    public Room getRoom() {
        return this.room;
    }

    public byte[][] getPreviewLayout() {
        return previewLayout;
    }

    public LayoutItem getCurrLayoutItem() {
        return currLayoutItem;
    }

    public PlacableItem getCurrPlaceableItem() {
        return currPlaceableItem;
    }

    public ArrayList<PlacableItem> getPlacableItemList() {
        return placableItemList;
    }

    public void initDefaultPlaceableItem() {
        for (PlacableItemType i : PlacableItemType.values()) {
            this.placableItemList.add(new PlacableItem(i));
        }
    }

    public void selectnewPlaceableItem(PlacableItemType type) {
        if (type == PlacableItemType.TABLE) {
            currPlaceableItem = new PlacableItem(PlacableItemType.TABLE);
        }
        updatePreviewPlaceableItems();
    }

    private void updatePreviewPlaceableItems() {
        /*
        new Thread() {
            int startX, endX, startY, endY;
            byte itemNumber = -1;

            public void run() {
                for (int i = 0; i < previewLayout.length; i++) { //FIXME NullPointer
                    for (int j = 0; j < previewLayout[0].length; j++) {
                        previewLayout[i][j] = -1;
                    }
                }

                if (currPlaceableItem == null) {
                    return;
                }

                if (currPlaceableItem.getOrientation() == Orientation.BOTTOM || currPlaceableItem.getOrientation() == Orientation.TOP) {
                    startY = previewLayout[0].length / 2 - currPlaceableItem.getWidth() / 2;
                    endY = startY + currPlaceableItem.getWidth();
                    startX = previewLayout.length / 2 - currPlaceableItem.getLength() / 2;
                    endX = startX + currLayoutItem.getLength();
                } else {
                    startY = previewLayout.length / 2 - currPlaceableItem.getLength() / 2;
                    endY = startY + currPlaceableItem.getLength();
                    startX = previewLayout[0].length / 2 - currPlaceableItem.getWidth() / 2;
                    endX = startX + currPlaceableItem.getWidth();
                }

                if (currPlaceableItem.getType() == PlacableItemType.TABLE) itemNumber = 1;

                for (int i = startY; i < endY; i++) {
                    for (int j = startX; j < endX; j++) {
                        if (i >= 0 && j >= 0 && i < previewLayout[0].length && j < previewLayout.length)
                            previewLayout[j][i] = itemNumber;
                    }
                }
            }

        }.start();*/
    } 

    public PlaceableEditor getPlaceableEditor() {
        return placeableEditor;
    }

    public void rotatePlaceableItem() {
        placeableEditor.rotateItem();
    }

    public void selectPlaceableItem(PlacableItemType type) {
        placeableEditor.selectPlaceableItem(type);
        currPlaceableItem = placeableEditor.getCurrentItem();
    }

    public void placePlaceableItem(int x, int y) {
        placeableEditor.placeCurrItem(x, y);
    }

}