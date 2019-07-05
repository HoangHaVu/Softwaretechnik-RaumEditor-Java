package roomieboomie.business.item.placable;

import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.RoomItem;

/**
 * Platzierbares Objekt fuer ein Spiel
 */
public class PlacableItem extends RoomItem {
    private PlacableItemType type;

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
    }

    /**
     * Neues PlacableItem ohne Informationen zu Platzierung
     * @param type Typ des Items
     */
    public PlacableItem(PlacableItemType type){
        super(-1,-1,Orientation.TOP);
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
