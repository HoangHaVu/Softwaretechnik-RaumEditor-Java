package roomieboomie.business.editor;

import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.layout.LayoutItem;
import roomieboomie.business.item.placable.Height;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.item.placable.PlacableItemType;
import roomieboomie.business.room.Room;
import roomieboomie.business.room.RoomPreview;
import roomieboomie.persistence.Config;
import roomieboomie.persistence.JsonHandler;

import java.util.ArrayList;
import java.util.List;

import static roomieboomie.business.item.placable.PlacableItemType.UNICORN;

public class PlaceableEditor {

    private Room room;
    private byte[][] flat;
    private byte[][] small;
    private byte[][] medium;
    private byte[][] high;
    private PlacableItem currentItem;

    public PlaceableEditor(Room room) {
        this.room = room;

        flat = new byte[Config.get().MAXHEIGHT()][Config.get().MAXWIDTH()];
        small = new byte[Config.get().MAXHEIGHT()][Config.get().MAXWIDTH()];
        medium = new byte[Config.get().MAXHEIGHT()][Config.get().MAXWIDTH()];
        high = new byte[Config.get().MAXHEIGHT()][Config.get().MAXWIDTH()];


        /*
        flat=new byte[5][5];
        small=new byte[5][5];
        medium= new byte[5][5];
        high=new byte [5][5];
        currentItem = new PlacableItem(UNICORN); */
    }

    public void selectPlaceableItem(PlacableItemType type) {
        currentItem = new PlacableItem(type);
    }

    public void placeCurrItem(int x, int y) {
        currentItem.setY(y);
        currentItem.setX(x);
        addItem(currentItem);
        currentItem = currentItem.clone();

    }


    public void addItem(PlacableItem currentItem) {
        room.addPlacableItem(currentItem);
        int endX, endY;
        int itemHeight = currentItem.getType().getHeight().getValue();
        int itemShelterHeight = currentItem.getType().getShelterHeight().getValue();

        endX = currentItem.getY()+currentItem.getLength();
        endY = currentItem.getX()+currentItem.getWidth();



        if (currentItem.getOrientation() == Orientation.TOP || currentItem.getOrientation() == Orientation.BOTTOM) {
            endX = currentItem.getX()+currentItem.getWidth();
            endY = currentItem.getY()+currentItem.getLength();
        }

        for (int i = currentItem.getX(); i <= endX; i++) {
            for (int j = currentItem.getY(); j <= endY; j++) {

                if (Height.FLAT.getValue() > itemShelterHeight && Height.FLAT.getValue() <= itemHeight || (Height.FLAT.getValue() == itemHeight && Height.FLAT.getValue() == itemShelterHeight)) {
                    flat[i][j] = (byte) room.getPlacableItemList().size();
                }
                if (Height.SMALL.getValue() > itemShelterHeight && Height.SMALL.getValue() <= itemHeight || (Height.SMALL.getValue() == itemHeight && Height.SMALL.getValue() == itemShelterHeight)) {
                    small[i][j] = (byte) room.getPlacableItemList().size();
                }
                if (Height.MEDIUM.getValue() > itemShelterHeight && Height.MEDIUM.getValue() <= itemHeight || (Height.MEDIUM.getValue() == itemHeight && Height.MEDIUM.getValue() == itemShelterHeight)) {
                    medium[i][j] = (byte) room.getPlacableItemList().size();
                }
                if (Height.HIGH.getValue() > itemShelterHeight && Height.HIGH.getValue() <= itemHeight || (Height.HIGH.getValue() == itemHeight && Height.HIGH.getValue() == itemShelterHeight)) {
                    high[i][j] = (byte) room.getPlacableItemList().size();
                }
                //small[i][j]=(byte)room.getItemList().size();


                // if (currentItem.getType().getShelterHeight() >Height.FLAT && currentItem.getType().getShelterHeight()==Height.FLAT)

                /*
                if(currentItem.getType().getShelterHeight()==Height.FLAT ){
                    small[i][j]=(byte)room.getItemList().size();
                    medium[i][j]=(byte)room.getItemList().size();
                    high[i][j]=(byte)room.getItemList().size();
                }else if(currentItem.getType().getShelterHeight()==Height.SMALL){
                    medium[i][j]=(byte)room.getItemList().size();
                    high[i][j]=(byte)room.getItemList().size();
                }else if(currentItem.getType().getShelterHeight()==Height.MEDIUM){
                    high[i][j]=(byte)room.getItemList().size();
                }*/

            }

        }



    }

    public void rotateItem() {
        currentItem.turnRight();
    }

    public void delete(int x,int y) {

        boolean del = false;
        byte itemNumber = 0;
        if (flat[x][y] == 0 && small[x][y] == 0 && medium[x][y] == 0 && high[x][y] == 0) {
            return;
        }

        if (high[x][y] != 0) {
            itemNumber = high[x][y];
        } else if (medium[x][y] != 0) {
            itemNumber = medium[x][y];
        } else if (small[x][y] != 0) {
            itemNumber = small[x][y];
        } else if (flat[x][y] != 0) {
            itemNumber = flat[x][y];
        }

        currentItem = room.getPlacableItemList().get(itemNumber - 1);

        int itemHeight = currentItem.getType().getHeight().getValue();
        int itemShelterHeight = currentItem.getType().getShelterHeight().getValue();

        int endX,endY;

        endX = currentItem.getY()+currentItem.getLength();
        endY = currentItem.getX()+currentItem.getWidth();

        if (currentItem.getOrientation() == Orientation.TOP || currentItem.getOrientation() == Orientation.BOTTOM) {
            endX = currentItem.getX()+currentItem.getWidth();
            endY = currentItem.getY()+currentItem.getLength();
        }

        for (int i = currentItem.getX(); i <= endX; i++) {
            for (int j = currentItem.getY(); j <= endY; j++) {

                if (Height.FLAT.getValue() > itemShelterHeight && Height.FLAT.getValue() <= itemHeight || (Height.FLAT.getValue() == itemHeight && Height.FLAT.getValue() == itemShelterHeight)) {
                    flat[i][j] = 0;
                }
                if (Height.SMALL.getValue() > itemShelterHeight && Height.SMALL.getValue() <= itemHeight || (Height.SMALL.getValue() == itemHeight && Height.SMALL.getValue() == itemShelterHeight)) {
                    small[i][j] = 0;
                }
                if (Height.MEDIUM.getValue() > itemShelterHeight && Height.MEDIUM.getValue() <= itemHeight || (Height.MEDIUM.getValue() == itemHeight && Height.MEDIUM.getValue() == itemShelterHeight)) {
                    medium[i][j] = 0;
                }
                if (Height.HIGH.getValue() > itemShelterHeight && Height.HIGH.getValue() <= itemHeight || (Height.HIGH.getValue() == itemHeight && Height.HIGH.getValue() == itemShelterHeight)) {
                    high[i][j] = 0;
                }
            }
        }

        for (int o = 0; o < room.getLayout().length; o++) {
            for (int p = 0; p < room.getLayout()[0].length; p++) {
                if (flat[o][p] > itemNumber) {
                    flat[o][p] -= 1;
                }
                if (small[o][p] > itemNumber) {
                    small[o][p] -= 1;
                }
                if (medium[o][p] > itemNumber) {
                    medium[o][p] -= 1;
                }
                if (high[o][p] > itemNumber) {
                    high[o][p] -= 1;
                }
            }
        }

        room.getPlacableItemList().remove(currentItem);
    }

    public void editItem(int x,int y) {
        List<PlacableItem> roomItemList = room.getPlacableItemList();
        byte itemNumber = 0;


        if (flat[x][y] == 0 && small[x][y] == 0 && medium[x][y] == 0 && high[x][y] == 0) {
            return;
        }
        if (high[x][y] != 0) {
            itemNumber = high[x][y];
        } else if (medium[x][y] != 0) {
            itemNumber = medium[x][y];
        } else if (small[x][y] != 0) {
            itemNumber = small[x][y];
        } else if (flat[x][y] != 0) {
            itemNumber = flat[x][y];
        }

        PlacableItem itemToEdit = room.getPlacableItemList().get(itemNumber-1);

        currentItem = room.getPlacableItemList().get(itemNumber - 1);

        //itemToEdit = roomItemList.get(index);
        delete(x,y);

        currentItem = itemToEdit;
    }

    public Room getRoom() {
        return room;
    }

    public PlacableItem getCurrentItem() {
        return currentItem;
    }
    public void printArray(){
        System.out.println("flat");
        for (int i =0;i<flat.length;i++){
            for(int j=0;j<flat[0].length;j++){
                System.out.print(small[i][j]);
            }
            System.out.println("");
        }

        System.out.println("\nsmall");

        for (int i =0;i<small.length;i++){
            for(int j=0;j<small[0].length;j++){
                System.out.print(small[i][j]);
            }
            System.out.println("");
        }

        System.out.println("\nmedium");

        for (int i =0;i<medium.length;i++){
            for(int j=0;j<medium[0].length;j++){
                System.out.print(small[i][j]);
            }
            System.out.println("");
        }

        System.out.println("\nhigh");

        for (int i =0;i<high.length;i++){
            for(int j=0;j<high[0].length;j++){
                System.out.print(small[i][j]);
            }
            System.out.println("");

        }
    }


}