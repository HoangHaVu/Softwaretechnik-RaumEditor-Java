package roomieboomie.business.item.placable;

/**
 * Typ eines PlacableItems mit abrufbaren Eigenschaften wallItem, storagePlace, floorItem, storable
 */
public enum PlacableItemType {

    DINO(false, false, false, true),
    UNICORN(false, false, false, true),
    PLANT(false, false, false, true),
    TEDDY(false, false, false, true),
    CARPET(false, true, true, false),
    TABLE(false, true, true, false),
    TVTABLE(false, true, true, false),
    STOOL(false, false, true, false),
    COUCH(false, false, true, false),
    BED(false, false, true, false),
    LOFTBED(false, false, true, false),
    CLOSET(false, false, true, false),
    CORNERSOFA(false, false, true, false),
    WALLCLOSET(true, true, false, false),
    SHELF(true, true, false, false),
    WALLCLOCK(true, false, false, false),
    WALLPICTURE(true, false, false, false);

    private String texture;
    private boolean wallitem;
    private boolean storagePlace;
    private boolean floorItem;
    private boolean storable;

    /**
     * Privater Konstruktor
     * @param wallitem Ob Item an der Wand platziert werden muss
     * @param storagePlace Ob auf dem Item etwas abgestellt werden darf
     * @param floorItem Ob das Item auf Boden oder Teppich stehen muss
     * @param storable Ob man das Item auf einem anderen abstellen kann
     */
    private PlacableItemType(boolean wallitem, boolean storagePlace, boolean floorItem, boolean storable) {
        //this.texture = texture; TODO
        this.wallitem = wallitem;
        this.storagePlace = storagePlace;
        this.floorItem = floorItem;
        this.storable = storable;
    }

    /**
     *
     * @return Textur des PlacableItemTypes
     */
    public String getTexture() {
        return texture;
    }

    /**
     *
     * @return Ob Item an der Wand platziert werden muss
     */
    public boolean isWallitem() {
        return wallitem;
    }

    /**
     *
     * @return Ob auf dem Item etwas abgestellt werden darf
     */
    public boolean isStoragePlace() {
        return storagePlace;
    }

    /**
     *
     * @return Ob das Item auf Boden oder Teppich stehen muss
     */
    public boolean isFloorItem() {
        return floorItem;
    }

    /**
     *
     * @return Ob man das Item auf einem anderen abstellen kann
     */
    public boolean isStorable() {
        return storable;
    }
}
