package roomieboomie.business.validation;

import roomieboomie.business.exception.editorExceptions.*;
import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.layout.LayoutItem;
import roomieboomie.business.item.layout.LayoutItemType;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.room.Room;
import roomieboomie.persistence.Config;


public class Validator {

    private byte[][] layout;
    private int highestX, highestY, smallestX, smallestY;

    public Validator() {
    }

    /**
     * Hinweis: überschreibt Felder im aktuellen Layout,
     * und sollte deshalb, erst nach vorherigem erfolgreichen durchlaufen, eine richtige Referenz auf das Layout bekomen.
     * <p>
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
     * Validiert kompletten Raum. Hierbei wird überprüft ob es nur ein Raum mit Tür ist. Sowie alle Wände geschlossen sind, ansonsten return false
     * Ist Raum valide wird noch die Raumhöhe sowie Breite bestimmt, in der RoomPreview eingetragen und true returned.
     *
     * @param room
     * @return
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

    public boolean validateLayoutPlacement(LayoutItem item,byte[][]layout) throws LayoutItemMissplaceException, WallMissplaceException, DoorMissplaceException, WindowMissplaceException {

        if (item.getX()!=layout.length||item.getX()!=0&&item.getY()!=layout[0].length||item.getY()!=0){
            if (item.getType().equals(LayoutItemType.WALL)){
                for(int x=item.getX();x<(item.getLength()+item.getX());x++){
                    for(int y=item.getY();y<(item.getWidth()+item.getY());y++) {
                        if(layout[x][y]!= Config.get().LAYOUTEXTERIORVALUE()){
                            throw new WallMissplaceException();
                        }
                    }
                }
                return true;
            }
            if(item.getType().equals(LayoutItemType.DOOR)||item.getType().equals(LayoutItemType.WINDOW)){
                for(int x=item.getX();x<(item.getLength()+item.getX());x++){
                    for(int y=item.getY();(y<item.getWidth()+item.getY());y++) {
                        if(layout[x][y]< Config.get().EDITORMINWALLVALUE()){
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

        throw new LayoutItemMissplaceException();

    }

    public boolean validatePlacement(PlacableItem item) {


        return true;
    }

    public void setLayout(byte[][] layout) {
        this.layout = layout;
    }

    public byte[][] getLayout() {
        return this.layout;
    }

}
