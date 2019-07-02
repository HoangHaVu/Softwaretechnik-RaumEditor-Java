package roomieboomie.business.validation;

import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.layout.LayoutItem;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.room.Room;


public class Validator {

    private byte[][] layout;
    private int highestX, highestY, smallestX, smallestY; 

    public Validator(byte[][] layout){
        this.layout = layout;
    }


    /**
     * 
     * Hinweis: überschreibt Felder im aktuellen Layout, 
     * und sollte deshalb, erst nach vorherigem erfolgreichen durchlaufen, eine richtige Referenz auf das Layout bekomen.
     * 
     * Validiert einzelne Felder im Layout. Solange Felder Valide sind, werden diese auf 0 gesetzt. 
     * 
     * @param x X-Koordinate der Tür
     * @param y Y-Koordinate der Tür
     * 
     * 
     */

    private boolean validateLayoutField(byte[][] findInsideRoomLayout, int x, int y){

        if ( x < 0 || y < 0 || x >= findInsideRoomLayout[0].length || y >= findInsideRoomLayout.length){
            return false;
        }

        if (findInsideRoomLayout[y][x] != -1){
            if (x > highestX) highestX = x;
            if (x < smallestX) smallestX = x;
            if (y > highestY) highestY = y;
            if (y < smallestY) smallestY = y;

            return true;
        }
        
        findInsideRoomLayout[y][x] = 0;
        

        if (
            validateLayoutField(findInsideRoomLayout, x - 1, y) &&
            validateLayoutField(findInsideRoomLayout, x + 1, y) &&
            validateLayoutField(findInsideRoomLayout, x, y - 1) &&
            validateLayoutField(findInsideRoomLayout, x, y + 1)
        ){  
            return true;
        }
        
        return false;

    }

    /**
     * Validiert kompletten Raum. Hierbei wird überprüft ob es nur ein Raum mit Tür ist. Sowie alle Wände geschlossen sind, ansonsten return false
     * Ist Raum valide wird noch die Raumhöhe sowie Breite bestimmt, in der RoomPreview eingetragen und true returned.
     * @param room
     * @return
     */

    public boolean validateRoom(Room room){
        int startX = room.getDoors().get(0).getX();
        int startY = room.getDoors().get(0).getY();
        boolean field1, field2;
        
        if(room.getDoors().get(0).getOrientation() == Orientation.TOP || room.getDoors().get(0).getOrientation() == Orientation.BOTTOM){
            
            field1 = validateLayoutField(room.getLayout(), startX - 1, startY);
            field2 = validateLayoutField(room.getLayout(), startX + 1, startY);
            byte[][] finalLayout = room.getLayout();

            highestX = 0;
            smallestX = Integer.MAX_VALUE;
            highestY = 0;
            smallestY = Integer.MAX_VALUE;

            if (field1 && !field2) {
                validateLayoutField(finalLayout, startX - 1, startY);
                room.setLayout(finalLayout);
            }
            else if( !field1 && field2){
                validateLayoutField(finalLayout, startX + 1, startY);
                room.setLayout(finalLayout);
            } else return false;
            
            

        } else{

            field1 = validateLayoutField(room.getLayout(), startX, startY - 1);
            field2 = validateLayoutField(room.getLayout(), startX, startY + 1);
            byte[][] finalLayout = room.getLayout();

            highestX = 0;
            smallestX = Integer.MAX_VALUE;
            highestY = 0;
            smallestY = Integer.MAX_VALUE;

            if (field1 && !field2){
                validateLayoutField(finalLayout, startX, startY - 1);
                room.setLayout(finalLayout);
            }
            
            else if(!field1 && field2){
                validateLayoutField(finalLayout, startX, startY + 1);
                room.setLayout(finalLayout);
            } else return false;
            
        }
        
        room.getRoomPreview().setStartX(this.smallestX);
        room.getRoomPreview().setStartY(this.smallestY);
        room.getRoomPreview().setHeight(this.highestY - this.smallestY + 1);
        room.getRoomPreview().setWidth(this.highestX - this.smallestX + 1); 
        return true;
    }

    public boolean validatePlacement (LayoutItem item){
        return true;
    }

    public boolean validatePlacement(PlacableItem item){
        return true;
    }

    public void setLayout (byte[][]layout){
        this.layout = layout;
    }

    public byte[][] getLayout(){
        return this.layout;
    }

}
