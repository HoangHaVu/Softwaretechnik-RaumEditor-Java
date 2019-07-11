package roomieboomie.business.item.layout;

import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.RoomItem;

/**
 * Grundrissobjekt zur Verwendung im Editor und im Spiel
 */
public class LayoutItem extends RoomItem {
    private LayoutItemType type;

    /**
     * Initialisiert ein LayoutItem, welches schon einmal existierte.
     * @param type Typ des Objekts
     */
    public LayoutItem (LayoutItemType type, int x, int y, int length, int width, Orientation orientation) {
        super(x, y, length, width, orientation);
        this.type = type;
    }

    /**
     * initialisiert ein komplett neues LayoutItem
     * @param type
     * @param length
     * @param width
     * @param orientation
     */
    public LayoutItem(LayoutItemType type, int length, int width, Orientation orientation){
        super(length, width, orientation);
        this.type = type;
    }

    /**
     * gibt Kopie von sich selbst zurück
     */
    public LayoutItem clone(){
        return new LayoutItem(this.type, this.getLength(), this.getWidth(), this.getOrientation());
    }

    /**
     * @return Typ des Objekts
     */
    public LayoutItemType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return type.toString().hashCode();
    }
}
