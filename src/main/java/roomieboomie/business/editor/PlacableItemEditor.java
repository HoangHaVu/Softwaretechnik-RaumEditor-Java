package roomieboomie.business.editor;

import java.util.ArrayList;

import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.room.Room;
import roomieboomie.persistence.Config;

public class PlacableItemEditor {

    private Room room;
    private byte [][] layout;
    private ArrayList<PlacableItem> placableItemList;

    public PlacableItemEditor(Room room){
        this.room = room;
        placableItemList = room.getPlacableItemList();
        initializeLayout();
    }

    public void delItem(int x, int y){


        if (this.layout[y][x] <= 0 ) return;


        PlacableItem delItem = placableItemList.get(layout[y][x] - 1);
        PlacableItem item = delItem;

        if (!delItem.hasNextOn(x, y)){
            int startX = item.getX();
            int startY = item.getY();
            int endX = startX + item.getLength();
            int endY = startY + item.getWidth();

            if (item.getOrientation() == Orientation.TOP || item.getOrientation() == Orientation.BOTTOM){
                endX = startX + item.getWidth();
                endY = startY + item.getLength();
            }

            placableItemList.remove(delItem);
            
            for (int i = startY; y < endY; y++){
                for (int j = startX; x < endX; x++){
                    layout[i][j] = 0;
                }
            }

            return;

        }


        while (delItem.getNext() != null && delItem.hasNextOn(x,y)){
            item = delItem;
            delItem = delItem.getNext();
        }

        item.removeItemFromThis();
    }

    public void addItem(PlacableItem item){

        int startX = item.getX();
        int startY = item.getY();
        
        if (layout[startX][startY] != 0){
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









}