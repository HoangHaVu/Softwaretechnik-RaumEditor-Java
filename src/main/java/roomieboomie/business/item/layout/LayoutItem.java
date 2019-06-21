package roomieboomie.business.item.layout;

/**
 * Grundrissobjekt zur Verwendung im Editor und im Spiel
 */
public class LayoutItem {
    private LayoutItemType type;

    /**
     *
     * @param type Typ des Objekts
     */
    public LayoutItem(LayoutItemType type) {
        this.type = type;
    }

    /**
     *
     * @return Typ des Objekts
     */
    public LayoutItemType getType() {
        return type;
    }
}
