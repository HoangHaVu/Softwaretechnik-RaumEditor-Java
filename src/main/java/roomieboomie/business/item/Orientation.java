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
}
