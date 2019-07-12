package roomieboomie.business.item;

/**
 * Richtungswert eines RoomItems, für die Darstellung benoetigt.
 */
public enum Orientation {
    TOP, RIGHT, BOTTOM, LEFT;
    private static Orientation[] values = Orientation.values();

    /**
     * Gibt den naechsten Richtungswert zurueck. Wenn aktuell der letzte ausgewaehlt ist, kommt wieder der erste
     * @return Richtungswert, der nach dem aktuellen kommt
     */
    public Orientation getNext() {
        return values[(this.ordinal() + 1) % values.length];
    }

    /**
     * Gibt den vorherigen Richtungswert zurueck. Wenn aktuell der erste ausgewaehlt ist, kommt wieder der letzte
     * @return Richtungswert, der vor dem aktuellen kommt
     */
    public Orientation getPrev() {
        if (this.ordinal() == 0){
            return values[values.length - 1];
        } else{
            return values[this.ordinal() - 1];
        }
    }

    /**
     * Gibt zurueck, ob eine vertikale Ausrichtung vorliegt (TOP oder BOTTOM)
     * @return true, wenn Objekt horizontal ausgerichtet ist
     */
    public boolean isVertical(){
        return (this == TOP || this == BOTTOM);
    }

    /**
     * Gibt zurueck, ob eine horizontale Ausrichtung vorliegt (RIGHT oder LEFT)
     * @return true, wenn Objekt vertikal ausgerichtet ist
     */
    public boolean isHorizontal(){
        return (this == RIGHT || this == LEFT);
    }
}
