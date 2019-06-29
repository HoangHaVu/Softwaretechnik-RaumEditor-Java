package roomieboomie.business.item.placable;

/**
 * Typ eines PlacableItems mit abrufbaren Eigenschaften wallItem, storagePlace, floorItem, storable, height,
 * shelterHeight, scorePoints, length und width
 */
public enum PlacableItemType {

    DINO(false, false, false, true, Height.SMALL, Height.FLAT, 10, 1, 1),
    UNICORN(false, false, false, true, Height.SMALL, Height.FLAT, 10, 1, 1),
    PLANT(false, false, false, true, Height.SMALL, Height.FLAT, 10, 1, 1),
    TEDDY(false, false, false, true, Height.SMALL, Height.FLAT, 10, 1, 1),
    CARPET(false, true, true, false, Height.FLAT, Height.FLAT, 50, 2, 3),
    TABLE(false, true, true, false, Height.MEDIUM, Height.MEDIUM, 50, 2, 3),
    TVTABLE(false, true, true, false, Height.SMALL, Height.FLAT, 50, 1, 3),
    STOOL(false, false, true, false, Height.MEDIUM, Height.FLAT, 20, 1, 1),
    COUCH(false, false, true, false, Height.MEDIUM, Height.FLAT, 100, 3, 2),
    BED(false, false, true, false, Height.MEDIUM, Height.FLAT, 100, 3, 2),
    LOFTBED(false, false, true, false, Height.HIGH, Height.MEDIUM, 150, 3, 2),
    CLOSET(false, false, true, false, Height.HIGH, Height.FLAT, 80, 1, 2),
    CORNERSOFA(false, false, true, false, Height.MEDIUM, Height.FLAT, 200, 2, 4),
    WALLCLOSET(true, true, false, false, Height.HIGH, Height.SMALL, 100, 1, 1),
    SHELF(true, true, false, false, Height.HIGH, Height.MEDIUM, 80, 1, 2),
    WALLCLOCK(true, false, false, false, Height.HIGH, Height.MEDIUM, 30, 1, 1),
    WALLPICTURE(true, false, false, false, Height.HIGH, Height.MEDIUM, 50, 1, 2);

    private String texture;
    private boolean wallItem;
    private boolean storagePlace;
    private boolean floorItem;
    private boolean storable;
    private Height height;
    private Height shelterHeight;
    private int scorePoints;
    private int length;
    private int width;

    /**
     * Privater Konstruktor
     * @param wallitem Ob Item an der Wand platziert werden muss
     * @param storagePlace Ob auf dem Item etwas abgestellt werden darf
     * @param floorItem Ob das Item auf Boden oder Teppich stehen muss
     * @param storable Ob man das Item auf einem anderen abstellen kann
     * @param height Hoehe
     * @param shelterHeight Unterstellhoehe
     * @param scorePoints Punkte, die das Item im Spiel bringt
     * @param length Laenge
     * @param width Breite
     */
    private PlacableItemType(boolean wallItem, boolean storagePlace, boolean floorItem, boolean storable,
                             Height height, Height shelterHeight, int scorePoints, int length, int width) {
        //this.texture = texture; TODO
        this.wallItem = wallItem;
        this.storagePlace = storagePlace;
        this.floorItem = floorItem;
        this.storable = storable;
        this.height = height;
        this.shelterHeight = shelterHeight;
        this.scorePoints = scorePoints;
        this.length = length;
        this.width = width;
    }

    /**
     * @return Textur des PlacableItemTypes
     */
    public String getTexture() {
        return texture;
    }

    /**
     * @return Ob Item an der Wand platziert werden muss
     */
    public boolean isWallitem() {
        return wallItem;
    }

    /**
     * @return Ob auf dem Item etwas abgestellt werden darf
     */
    public boolean isStoragePlace() {
        return storagePlace;
    }

    /**
     * @return Ob das Item auf Boden oder Teppich stehen muss
     */
    public boolean isFloorItem() {
        return floorItem;
    }

    /**
     * @return Ob man das Item auf einem anderen abstellen kann
     */
    public boolean isStorable() {
        return storable;
    }

    /**
     * @return Laenge des Items
     */
    public int getLength() {
        return length;
    }

    /**
     * @return Breite des Items
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return Hoehe des Items
     */
    public Height getHeight() {
        return height;
    }

    /**
     * @return Unterstellhoehe des Items
     */
    public Height getShelterHeight() {
        return shelterHeight;
    }

    /**
     * @return Punkte, die das Item im Spiel bringt
     */
    public int getScorePoints() {
        return scorePoints;
    }
}
