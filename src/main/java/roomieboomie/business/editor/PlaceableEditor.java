package roomieboomie.business.editor;

import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.placable.Height;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.item.placable.PlacableItemType;
import roomieboomie.business.room.Room;
import roomieboomie.persistence.Config;

public class PlaceableEditor {
    Room room;
    byte[][] flat;
    byte[][] small;
    byte[][] medium;
    byte[][] high;
    PlacableItem currentItem;


    public PlaceableEditor(){

       flat=new byte[Config.get().MAXHEIGHT()][Config.get().MAXITEMLENGTH()];
        small=new byte[Config.get().MAXHEIGHT()][Config.get().MAXITEMLENGTH()];
        medium= new byte[Config.get().MAXHEIGHT()][Config.get().MAXITEMLENGTH()];
        high=new byte[Config.get().MAXHEIGHT()][Config.get().MAXITEMLENGTH()];


    }

    public void selectPlaceableItem(PlacableItemType type){
        currentItem=new PlacableItem(type);
    }

    public void placeActItem(int x,int y){

        currentItem.setY(y);
        currentItem.setX(x);
        addItem(currentItem);
    }

    public void addItem(PlacableItem currentItem){
        //room.addPlacableItem(currentItem);
        /*
        for(int o =0;o<small.length;o++){
            for (int p =0;p<small[0].length;p++){
                System.out.print(small[o][p]+" ");
            }
            System.out.println("");

        }
        */
        int endX, endY;

        endX = currentItem.getWidth();
        endY = currentItem.getLength();

        if (currentItem.getOrientation() == Orientation.TOP ||currentItem.getOrientation() == Orientation.BOTTOM ){
            endX = currentItem.getLength();
            endY = currentItem.getWidth();
        }

        for(int i=currentItem.getX();i<=endX;i++){
            for(int j=currentItem.getY();j<=endY;j++){
                small[i][j]=(byte)room.getItemList().size();


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


    }

    public void rotate(){


    }
}
