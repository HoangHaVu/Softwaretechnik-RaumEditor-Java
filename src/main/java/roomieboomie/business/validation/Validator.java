package roomieboomie.business.validation;

import roomieboomie.business.exception.validationExceptions.*;
import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.layout.LayoutItem;
import roomieboomie.business.item.layout.LayoutItemType;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.item.placable.PlacableItemType;
import roomieboomie.business.room.Room;
import roomieboomie.persistence.Config;

import java.util.ArrayList;


public class Validator {

    private byte[][] layout;
    private int highestX, highestY, smallestX, smallestY;

    public Validator() {
    }

    /**
     * Hinweis: überschreibt Felder im aktuellen Layout,
     * und sollte deshalb, erst nach vorherigem erfolgreichen durchlaufen, eine richtige Referenz auf das Layout bekomen.
     *
     * Validiert einzelne Felder im Layout. Solange Felder Valide sind, werden diese auf 0 gesetzt.
     *
     * @param x X-Koordinate der Tür
     * @param y Y-Koordinate der Tür
     */
    private boolean validateLayoutField(byte[][] findInsideRoomLayout, int x, int y) {

        if (x < 0 || y < 0 || x >= findInsideRoomLayout[0].length || y >= findInsideRoomLayout.length) {
            return false;
        }

        if (findInsideRoomLayout[y][x] != -1) {
            if (x > highestX) highestX = x;
            if (x < smallestX) smallestX = x;
            if (y > highestY) highestY = y;
            if (y < smallestY) smallestY = y;

            return true;
        }

        findInsideRoomLayout[y][x] = 0;

        if (validateLayoutField(findInsideRoomLayout, x - 1, y) &&
                validateLayoutField(findInsideRoomLayout, x + 1, y) &&
                validateLayoutField(findInsideRoomLayout, x, y - 1) &&
                validateLayoutField(findInsideRoomLayout, x, y + 1)) {
            return true;
        }

        return false;
    }

     /**
     * Validiert einen Room. Dabei wird ueberprueft, ob der Raum eine Tuer und mindestens zwei Fenster hat und
     * ob alle Waende geschlossen sind. Ist der Room valide, werden height und length im Room eingetragen.
     *
     * @param room Raum, der validiert werden soll
     * @return true, wenn der Raum valide ist
     */
    public boolean validateRoom(Room room) throws MissingDoorException, MissingWindowException, getIntoRoomException {
        if (room.getDoors().isEmpty()){ // Raum ist invalide, wenn er keine Tueren oder Fenster hat
            throw new MissingDoorException();
        }
        if( room.getWindows().isEmpty()){
            throw new MissingWindowException();
        }

        int startX = room.getDoors().get(0).getX();
        int startY = room.getDoors().get(0).getY();
        boolean field1, field2;

        if (room.getDoors().get(0).getOrientation() == Orientation.TOP || room.getDoors().get(0).getOrientation() == Orientation.BOTTOM) {

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
            } else if (!field1 && field2) {
                validateLayoutField(finalLayout, startX + 1, startY);
                room.setLayout(finalLayout);
            } else throw new getIntoRoomException();

        } else {
            field1 = validateLayoutField(room.getLayout(), startX, startY - 1);
            field2 = validateLayoutField(room.getLayout(), startX, startY + 1);
            byte[][] finalLayout = room.getLayout();

            highestX = 0;
            smallestX = Integer.MAX_VALUE;
            highestY = 0;
            smallestY = Integer.MAX_VALUE;

            if (field1 && !field2) {
                validateLayoutField(finalLayout, startX, startY - 1);
                room.setLayout(finalLayout);
            } else if (!field1 && field2) {
                validateLayoutField(finalLayout, startX, startY + 1);
                room.setLayout(finalLayout);
            } else throw new getIntoRoomException();
        }

        room.setStartX(this.smallestX);
        room.setStartY(this.smallestY);
        room.setHeight(this.highestY - this.smallestY + 1);
        room.setWidth(this.highestX - this.smallestX + 1);
        return true;
    }

    /**
     * Diese Methode ueberprueft ob das LayoutItem regelgerecht platziert wurde
     * @param item - LayoutItem das gesetzt wird
     * @param layout - 2D Layout
     * @return
     * @throws LayoutItemMissplaceException - Exception wird geworfen wenn das LayoutItem am Rand platziert wurde
     * @throws WallMissplaceException  - Exception wird geworfen wenn die Wand falsch platziert wird  ->
     * @throws DoorMissplaceException - Exception wird geworfen wenn die Tür falsch platziert wird -> muss in eine Wand platziert werden
     * @throws WindowMissplaceException - Exception wird geworfen wenn das Fenster falsch platziert wurde -> muss in eine Wand platziert werden
     */
    public boolean validateLayoutPlacement(LayoutItem item,byte[][]layout) throws LayoutItemMissplaceException, WallMissplaceException, DoorMissplaceException, WindowMissplaceException {
        int endY = item.getY() + item.getWidth();
        int endX = item.getX() + item.getLength();

        if (item.getOrientation() == Orientation.BOTTOM || item.getOrientation() == Orientation.TOP){
            endY = item.getY() + item.getLength();
            endX = item.getX() + item.getWidth();
        }
        if (item.getX()!=layout.length&&item.getX()!=0&&item.getY()!=layout[0].length&&item.getY()!=0){
            if (item.getType().equals(LayoutItemType.WALL)){
                for(int x=item.getX();x<endX;x++){
                    for(int y=item.getY();y<endY;y++) {
                        if(layout[y][x]!= Config.get().LAYOUTEXTERIORVALUE()){
                            throw new WallMissplaceException();
                        }
                    }
                }
                return true;
            }
            if(item.getType().equals(LayoutItemType.DOOR)||item.getType().equals(LayoutItemType.WINDOW)){
                for(int x=item.getX();x<endX;x++){
                    for(int y=item.getY();y<endY;y++) {
                        if(layout[y][x]< Config.get().EDITORMINWALLVALUE()){
                            if(item.getType().equals(LayoutItemType.DOOR)){
                                throw new DoorMissplaceException();
                            }else{
                                throw new WindowMissplaceException();
                            }
                        }
                    }
                }
                return true;
            }
        }

        throw new LayoutItemMissplaceException("LayoutItem darf nicht an die Wand platziert werden");

    }

    /**
     * Diese Methode ueberprueft ob das platzierte Objekt regelgerecht platziert wurde
     * @param item
     * @param layout
     * @param placableItems
     * @return
     * @throws PlaceItemIsNotInInteriorException
     * @throws ObjectToHighInFrontOfWindowException
     * @throws ItemIsTooCloseToDoorException
     */
    public boolean validatePlaceItemPlacement(PlacableItem item, byte[][]layout, ArrayList<PlacableItem>placableItems) throws PlaceItemIsNotInInteriorException,ObjectToHighInFrontOfWindowException,ItemIsTooCloseToDoorException {
        if(!checkLayoutInteractions(item,layout)){
            int endY = item.getY() + item.getWidth();
            int endX = item.getX() + item.getLength();

            if (item.getOrientation() == Orientation.BOTTOM || item.getOrientation() == Orientation.TOP){
                endY = item.getY() + item.getLength();
                endX = item.getX() + item.getWidth();
            }
            for(int x=item.getX();x<endX;x++){
                for(int y=item.getY();y<endY;y++) {
                    if(placableItems.size()>0){
                        if(item.getType().equals(PlacableItemType.CARPET)&&layout[y][x]-1>=0&&placableItems.get(layout[y][x]-1).getType().equals(PlacableItemType.CARPET)){
                            System.out.println("man kann keine Teppiche aufeinander legen");
                            return false;
                        }
                        if(item.getType().isStorable()&&placableItems.get(layout[y][x]-1).getType().isStoragePlace()==false){
                            System.out.println("deko wurde nicht auf Ablage drauf getan");
                            return false;
                        }
                        if(layout[y][x]>Config.get().LAYOUTINTERIORVALUE()&&item.getType().isStorable()==false){
                            System.out.println("Objekt kann nicht platziert werden da der Platz für dieses Objekt schon gesetzt ist ->"+placableItems.get(layout[y][x]-1).getType().getName());
                            return false;
                        }
                    }
                }
            }

        }
        return true;
    }

    /**
     * Diese Methode ueberprueft ob das gesetzte Item zu nah an einer Tür ist oder nicht im inneren des Raumes liegt
     * @param item - das PlaceableItem das gesetzt werden soll
     * @param layout - das 2D Layout
     * @return
     * @throws ItemIsTooCloseToDoorException - Exception wird geworfen wenn Item zu nah an der Tür platziert wurde -> verletzt Regel im Pflichtenheft
     * @throws PlaceItemIsNotInInteriorException - Exception wird geworfen wenn das Item nicht im inneren des Raumes platziert werden
     * @throws ObjectToHighInFrontOfWindowException - Exception wird geworfen wenn das Objekt zu hoch ist um es vor dem Fenster zu platzieren
     */
    public boolean checkLayoutInteractions(PlacableItem item,byte [][]layout) throws ItemIsTooCloseToDoorException,ObjectToHighInFrontOfWindowException,PlaceItemIsNotInInteriorException{
        int endY = item.getY() + item.getWidth();
        int endX = item.getX() + item.getLength();

        if (item.getOrientation() == Orientation.BOTTOM || item.getOrientation() == Orientation.TOP){
            endY = item.getY() + item.getLength();
            endX = item.getX() + item.getWidth();
        }

        for(int x=item.getX()-1;x<(endX+1);x++){
            for(int y=item.getY()-1;y<(endY+1);y++) {
                if(layout[y][x]==Config.get().GAMEWINDOWVALUE()&&item.getType().getHeight().getValue()>2){
                    throw new ObjectToHighInFrontOfWindowException();
                }

                if(layout[y][x]== Config.get().EDITORDOORVALUE()){
                    throw new ItemIsTooCloseToDoorException();
                }
                if(x>=item.getX()&&x<(item.getX()+item.getLength())&& y>=item.getY()&&y<(item.getWidth()+item.getY())&&layout[y][x]< Config.get().LAYOUTINTERIORVALUE()){
                    throw new PlaceItemIsNotInInteriorException();
                }


            }
        }
        return false;
    }

    public void setLayout(byte[][] layout) {
        this.layout = layout;
    }

    public byte[][] getLayout() {
        return this.layout;
    }

}
