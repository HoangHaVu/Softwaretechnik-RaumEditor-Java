package roomieboomie.business.item.placable;

import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.RoomItem;

/**
 * Platzierbares Pbjekt fuer ein Spiel
 */
public class PlacableItem extends RoomItem {
    private PlacableItemType type;
    private Height height;
    private Height shelterHeight;
    private int scorePoints;

    /**
     *
     * @param x x-Koordinate
     * @param y y-Koordinate
     * @param length Laenge
     * @param width Breite
     * @param orientation Richtung
     * @param type Typ des Items
     * @param height Hoehe
     * @param shelterHeight Unterstellhoehe
     * @param scorePoints Punkte, die das Item im Spiel bringt
     */
    public PlacableItem(int x, int y, int length, int width, Orientation orientation, PlacableItemType type, Height height, Height shelterHeight, int scorePoints) {
        super(x, y, length, width, orientation);
        this.type = type;
        this.height = height;
        this.shelterHeight = shelterHeight;
        this.scorePoints = scorePoints;
    }

    /**
     *
     * @return Typ des Items
     */
    public PlacableItemType getType() {
        return type;
    }

    /**
     *
     * @return Hoehe des Items
     */
    public Height getHeight() {
        return height;
    }

    /**
     *
     * @return Unterstellhoehe des Items
     */
    public Height getShelterHeight() {
        return shelterHeight;
    }

    /**
     *
     * @return Punkte, die das Item im Spiel bringt
     */
    public int getScorePoints() {
        return scorePoints;
    }

    /**
     * Gibt zurueck, ob dieses Item unter das Vergleichsobjekt passt
     * @param compareItem Item, unter das dieses gestellt werden soll
     * @return Ob die Unterstellhoehe hoch genug ist
     */
    public boolean fitsBeneath(PlacableItem compareItem){
        //TODO im Klassendiagramm
        return this.height.fitsBeneath(compareItem.shelterHeight);
    }
}
