package roomieboomie.business.editor;

import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.placable.Height;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.item.placable.PlacableItemType;
import roomieboomie.business.room.Room;

public class PlaceableEditor {
    Room room;
    byte[][] flat;
    byte[][] small;
    byte[][] medium;
    byte[][] high;
    PlacableItem currentItem;


    public PlaceableEditor(PlacableItem item){
       currentItem= item;
       /*
       flat[][]=new byte[][];
        small=new byte[][];
        medium= new byte[][];
        high=new byte[][];
*/

    }

    public void selectPlaceableItem(PlacableItemType type){
        currentItem=new PlacableItem(type);
    }

    public void placeActItem(PlacableItem item,int x,int y){
        item.setY(y);
        item.setX(x);
        addItem(item);
    }

    public void addItem(PlacableItem currentItem){
        room.addPlacableItem(currentItem);

        for(int o =0;o<small.length;o++){
            for (int p =0;p<small[0].length;p++){
                System.out.print(small[o][p]+" ");
            }
            System.out.println("");

        }

        int endX, endY;

        endX = currentItem.getWidth();
        endY = currentItem.getLength();

        if (currentItem.getOrientation() == Orientation.TOP ||currentItem.getOrientation() == Orientation.BOTTOM ){
            endX = currentItem.getLength();
            endY = currentItem.getWidth();
        }

        for(int i=currentItem.getX();i<=endX;i++){
            for(int j=currentItem.getY();j<=endY;j++){


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
        System.out.println("");
        for(int q =0;q<small.length;q++){
            for (int w =0;w<small[0].length;w++){
                System.out.print(small[q][w]+" ");
            }
            System.out.println("");

        }

    rotate();

    }

    public void rotate(){


    }
}
