package roomieboomie.business.item.placable;

import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.RoomItem;

/**
 * Platzierbares Objekt fuer ein Spiel
 */
public class PlacableItem extends RoomItem {
    private PlacableItemType type;
    private PlacableItem next;
    private byte[][] layout;

    /**
     * Neues PlacableItem
     * @param x x-Koordinate
     * @param y y-Koordinate
     * @param orientation Richtung
     * @param type Typ des Items
     */
    public PlacableItem(int x, int y, Orientation orientation, PlacableItemType type) {
        super(x, y,type.getLength(),type.getWidth(), orientation);
        this.type = type;
        this.next = null;
        this.layout = new byte[this.getLength()][this.getWidth()];
        for (int i = 0; i < this.getLength(); i++){
            for (int j = 0; j < this.getWidth(); j++){
                this.layout[i][j] = 0;
            }
        }
    }


    public PlacableItem getNext(){
        return this.next;
    }


    public boolean hasNextOn(int x, int y){
        
        if (this.layout[y][x] != 0){
            return true;
        }

        return false;
    }


    public void placeItemOnThis(PlacableItem item){
        this.next = item;

        int startX = item.getX();
        int startY = item.getY();

        if (this.layout[startX][startY] != 0){
            item.setX(item.getX() - this.getX());
            item.setY(item.getY() - this.getY());
            this.next.placeItemOnThis(item);
            return;
        }

        int endX = startX + item.getLength();
        int endY = startY + item.getWidth();
        byte placeNumber = 1;

        if (item.getOrientation() == Orientation.TOP || item.getOrientation() == Orientation.BOTTOM){
            endX = startX + item.getWidth();
            endY = startY + item.getLength();
        }

        this.next = item;

        for (int y = startY; y < endY; y++){
            for (int x = startX; x < endX; x++){
                layout[y][x] = placeNumber;
            }
        }
        
    }

    public void removeItemFromThis(){
        this.next = null;
        for (int i = 0; i < this.layout.length; i++){
            for (int j = 0; j < this.layout[0].length; j++){
                this.layout[i][j] = 0;
            }
        }
    }


    /**
     * Neues PlacableItem ohne Informationen zu Platzierung
     * @param type Typ des Items
     */
    public PlacableItem(PlacableItemType type){
        super(type.getLength(),type.getWidth(),Orientation.TOP);
        this.type = type;
    }

    /**
     * @return Typ des Items
     */
    public PlacableItemType getType() {
        return type;
    }

    /**
     * Gibt zurueck, ob dieses Item unter das Vergleichsobjekt passt
     * @param compareItem Item, unter das dieses gestellt werden soll
     * @return Ob die Unterstellhoehe hoch genug ist
     */
    public boolean fitsBeneath(PlacableItem compareItem){
        return this.type.getHeight().fitsBeneath(compareItem.getType().getHeight());
    }

    @Override
    public int hashCode() {
        return type.toString().hashCode();
    }

    public PlacableItem clone(){ return new PlacableItem(this.getX(),this.getY(),this.getOrientation(),this.getType());}
}
