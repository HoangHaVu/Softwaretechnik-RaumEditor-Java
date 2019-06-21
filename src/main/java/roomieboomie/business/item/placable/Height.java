package roomieboomie.business.item.placable;

/**
 * Hoehe eines PlacalbeItems
 */
public enum Height {
    FLAT(0), SMALL(1), MEDIUM(2), HIGH(3);

    private int value;

    private Height (int value){
        this.value = value;
    }

    /**
     * Gibt an, ob die Hoehe eines PlacableItems unter eine andere Hoehe passt
     * @param compareHeight Hohe des Objekts, unter das das Item gestellt werden soll
     * @return ob das Item unter das andere passt
     */
    public boolean fitsBeneath(Height compareHeight){
        //TODO in UML aendern
        if (this.value < compareHeight.value){
            return true;
        }
        return false;
    }
}
