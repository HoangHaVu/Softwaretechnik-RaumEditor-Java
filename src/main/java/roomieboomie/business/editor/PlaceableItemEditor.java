package roomieboomie.business.editor;

import java.util.ArrayList;

import roomieboomie.business.exception.validationExceptions.ItemIsTooCloseToDoorException;
import roomieboomie.business.exception.validationExceptions.ObjectToHighInFrontOfWindowException;
import roomieboomie.business.exception.validationExceptions.PlaceItemIsNotInInteriorException;
import roomieboomie.business.item.placeable.PlaceableItem;
import roomieboomie.business.item.placeable.PlaceableItemType;
import roomieboomie.business.room.Room;
import roomieboomie.business.validation.Validator;
import roomieboomie.persistence.Config;

public class PlaceableItemEditor {

    private Room room;
    private byte [][] layout;
    private ArrayList<PlaceableItem> placeableItemList;
    private PlaceableItem currItem;
    private Validator validator;

    public PlaceableItemEditor(){
        currItem = new PlaceableItem(PlaceableItemType.TEDDY);
    }

    public void setValidator(Validator validator){
        this.validator = validator;
    }

    public void setRoom (Room room){
        this.room = room;
        placeableItemList = room.getPlaceableItemList();
        this.currItem = new PlaceableItem(PlaceableItemType.TEDDY);
        initializeLayout();
    }

    public void selectPlaceableItem(PlaceableItemType type) {
        this.currItem = new PlaceableItem(type);
    }

    public void setRoomPlaceableItemList(){
        this.room.setPlaceableItemList(this.placeableItemList);
    }


    public void setCurrItem(PlaceableItemType type){
        this.currItem = new PlaceableItem(type);
    }

    public void rotateCurItem(){

        this.currItem.turnRight();

    }

    public void editItem(int x, int y){

        if (this.layout[y][x] <= 0 ) return;

        PlaceableItem item = this.placeableItemList.get(this.layout[y][x] - 1);

        this.currItem = item.findItemByCoordinates(x, y);
        this.currItem = item;
        delItem(x, y);

    }

    public void delItem(int x, int y){


        if (this.layout[y][x] <= 0 ) return;

        int itemNumber = layout[y][x] - 1;
        PlaceableItem delItem = placeableItemList.get(itemNumber);
        PlaceableItem item = delItem;

        if (!delItem.hasNextOn(x - delItem.getX(), y - delItem.getY())){
            int startX = item.getX();
            int startY = item.getY();
            int endX = startX + item.getLength();
            int endY = startY + item.getWidth();

            if (item.getOrientation().isVertical()){
                endX = startX + item.getWidth();
                endY = startY + item.getLength();
            }
            delItem.removeItemFromThis();

            this.getPlaceableItemList().remove(delItem);
            
            for (int i = startY; i < endY; i++){
                for (int j = startX; j < endX; j++){
                    layout[i][j] = 0;
                }
            }

            for (int i = 0; i < this.layout.length; i++){
                for (int j = 0; j < this.layout[0].length; j++){
                    if (layout[i][j] > itemNumber){
                        layout[i][j] -= 1;
                    }
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
        this.currItem.setX(x);
        this.currItem.setY(y);
        if(validator.validatePlaceItemPlacement(currItem,this.layout,room.getPlaceableItemList())){
            addItem(this.currItem);
            this.currItem = currItem.clone();
            return;
        }


    }

    public void addItem(PlaceableItem item){
        int startX = item.getX();
        int startY= item.getY();
        /*
        if (item.getOrientation() == Orientation.TOP || item.getOrientation() == Orientation.BOTTOM){
            startX = item.getX();
            startY = item.getY();
        } else{
            startX = item.getY();
            startY = item.getX();
        }*/

        if (layout[startY][startX] > 0){
            PlaceableItem unterItem = placeableItemList.get(layout[startY][startX] - 1);
            item.setX(item.getX() - unterItem.getX());
            item.setY(item.getY() - unterItem.getY());
            unterItem.placeItemOnThis(item);
            return;
        }

        int endX = startX + item.getLength();
        int endY = startY + item.getWidth();
        byte placeNumber;

        if (item.getOrientation().isVertical()){
            endX = startX + item.getWidth();
            endY = startY + item.getLength();
        }

        placeableItemList.add(item);
        placeNumber = (byte) placeableItemList.size();

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
                if (gameLayout[i][j]<= Config.get().EDITORMAXWINDOWVALUE()){
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


    public ArrayList<PlaceableItem> getPlaceableItemList() {
        return this.placeableItemList;
    }  

    public byte[][] getLayout(){
        return this.layout;
    }

    public PlaceableItem getCurrentItem() {
        return currItem;
    }
}