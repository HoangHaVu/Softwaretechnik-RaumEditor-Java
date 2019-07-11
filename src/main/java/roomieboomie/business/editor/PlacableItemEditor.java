package roomieboomie.business.editor;

import java.util.ArrayList;

import roomieboomie.business.exception.validationExceptions.ItemIsTooCloseToDoorException;
import roomieboomie.business.exception.validationExceptions.ObjectToHighInFrontOfWindowException;
import roomieboomie.business.exception.validationExceptions.PlaceItemIsNotInInteriorException;
import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.item.placable.PlacableItemType;
import roomieboomie.business.room.Room;
import roomieboomie.business.validation.Validator;
import roomieboomie.persistence.Config;

public class PlacableItemEditor {

    private Room room;
    private byte [][] layout;
    private ArrayList<PlacableItem> placableItemList;
    private PlacableItem curItem;
    private Validator validator;

    public PlacableItemEditor(){
        curItem = new PlacableItem(PlacableItemType.TEDDY);
    }

    public void setValidator(Validator validator){
        this.validator = validator;
    }

    public void setRoom (Room room){
        this.room = room;
        placableItemList = room.getPlacableItemList();
        this.curItem = new PlacableItem(PlacableItemType.TEDDY);
        initializeLayout();
    }

    public void selectPlaceableItem(PlacableItemType type) {
        this.curItem = new PlacableItem(type);
    }

    public void saveRoom (){

        this.room.setPlacableItemList(this.placableItemList);
    }


    public void setCurItem(PlacableItemType type){
        this.curItem = new PlacableItem(type);
    }

    public void rotateCurItem(){

        this.curItem.turnRight();

    }

    public void editItem(int x, int y){

        if (this.layout[y][x] <= 0 ) return;

        PlacableItem item = this.placableItemList.get(this.layout[y][x] - 1);

        this.curItem = item.findItemByCoordinates(x, y);
        this.curItem = item;
        delItem(x, y);

    }

    public void delItem(int x, int y){


        if (this.layout[y][x] <= 0 ) return;


        PlacableItem delItem = placableItemList.get(layout[y][x] - 1);
        PlacableItem item = delItem;

        if (!delItem.hasNextOn(x - delItem.getX(), y - delItem.getY())){
            int startX = item.getX();
            int startY = item.getY();
            int endX = startX + item.getLength();
            int endY = startY + item.getWidth();

            if (item.getOrientation() == Orientation.TOP || item.getOrientation() == Orientation.BOTTOM){
                endX = startX + item.getWidth();
                endY = startY + item.getLength();
            }

            room.getPlacableItemList().remove(delItem);
            
            for (int i = startY; i < endY; i++){
                for (int j = startX; j < endX; j++){
                    layout[i][j] = 0;
                }
            }

            return;

        }


        while (delItem.getNext() != null && delItem.hasNextOn(x - delItem.getX(),y - delItem.getY())){
            item = delItem;
            delItem = delItem.getNext();
        }

        item.removeItemFromThis();
    }

    public void placeCurrItem(int x, int y) throws PlaceItemIsNotInInteriorException, ObjectToHighInFrontOfWindowException, ItemIsTooCloseToDoorException {
        this.curItem.setX(x);
        this.curItem.setY(y);

        if(validator.validatePlaceItemPlacement(curItem,this.layout,room.getPlacableItemList())){
            addItem(this.curItem);
            this.curItem = curItem.clone();
            return;
        }

    }

    public void addItem(PlacableItem item){
        int startX;
        int startY;
        if (item.getOrientation() == Orientation.TOP || item.getOrientation() == Orientation.BOTTOM){
            startX = item.getX();
            startY = item.getY();
        } else{
            startX = item.getY();
            startY = item.getX();
        }


        if (layout[startX][startY] > 0){
            PlacableItem unterItem = placableItemList.get(layout[startX][startY] - 1);
            item.setX(item.getX() - unterItem.getX());
            item.setY(item.getY() - unterItem.getY());
            unterItem.placeItemOnThis(item);
            return;
        }

        
        int endX = startX + item.getLength();
        int endY = startY + item.getWidth();
        byte placeNumber;

        if (item.getOrientation() == Orientation.TOP || item.getOrientation() == Orientation.BOTTOM){
            endX = startX + item.getWidth();
            endY = startY + item.getLength();
        }

        placableItemList.add(item);
        placeNumber = (byte) placableItemList.size();

        for (int y = startY; y < endY; y++){
            for (int x = startX; x < endX; x++){
                layout[y][x] = placeNumber;
            }
        }

    }


    private void initializeLayout(){

        byte [][] gameLayout = room.getLayout();

        for (int i = 0; i < gameLayout.length; i++){
            for (int j = 0; j < gameLayout[0].length; j++){
                if (gameLayout[i][j] < Config.get().EDITORMAXWINDOWVALUE()){
                    gameLayout[i][j] = Config.get().GAMEWINDOWVALUE();
                }
            }
        }
        for (int i = 0; i < gameLayout.length; i++){
            for (int j = 0; j < gameLayout[0].length; j++){
                if (gameLayout[i][j] >= Config.get().EDITORMINWALLVALUE()){
                    gameLayout[i][j] = Config.get().GAMEWALLVALUE();
                }
            }
        }

        this.layout = gameLayout;

    }


    public ArrayList<PlacableItem> getPlacableItemList() {
        return placableItemList;
    }

    public PlacableItem getCurrentItem() {
        return curItem;
    }
}