package roomieboomie.business.item.placeable;

/**
 * Typ eines PlaceableItems mit abrufbaren Eigenschaften wallItem, storagePlace, floorItem, storable, height,
 * shelterHeight, scorePoints, length und width
 */
public enum PlaceableItemType {

    DINO(false, false, true, Height.SMALL, Height.FLAT, 30, 1, 1, "Dino"),
    UNICORN(false, false, true, Height.SMALL, Height.FLAT, 30, 1, 1, "Einhorn"),
    PLANT(false, false, true, Height.SMALL, Height.FLAT, 30, 1, 1, "Pflanze"),
    TEDDY(false, false, true, Height.SMALL, Height.FLAT, 30, 1, 1, "Teddy"),
    CARPET(true, true, false, Height.FLAT, Height.FLAT, 80, 2, 3, "Teppich"),
    TABLE(true, true, false, Height.MEDIUM, Height.MEDIUM, 110, 2, 3, "Tisch"),
    CHAIR(false, true, false, Height.MEDIUM, Height.FLAT, 60, 1, 1, "Stuhl"),
    TVTABLE(true, true, false, Height.SMALL, Height.FLAT, 70, 1, 4, "Fernsehtisch"),
    STOOL(false, true, false, Height.MEDIUM, Height.FLAT, 60, 1, 1, "Hocker"),
    COUCH(false, true, false, Height.MEDIUM, Height.FLAT, 110, 2, 4, "Sofa"),
    BED(false, true, false, Height.MEDIUM, Height.FLAT, 110, 3, 2, "Bett"),
    LOFTBED(false, true, false, Height.HIGH, Height.MEDIUM, 150, 3, 2, "Hochbett"),
    CLOSET(false, true, false, Height.HIGH, Height.FLAT, 80, 1, 2, "Schrank");

    private boolean storagePlace;
    private boolean floorItem;
    private boolean storable;
    private Height height;
    private Height shelterHeight;
    private int scorePoints;
    private int length;
    private int width;
    private String name;

    /**
     * Privater Konstruktor
     *
     * @param wallItem      Ob Item an der Wand platziert werden muss
     * @param storagePlace  Ob auf dem Item etwas abgestellt werden darf
     * @param floorItem     Ob das Item auf Boden oder Teppich stehen muss
     * @param storable      Ob man das Item auf einem anderen abstellen kann
     * @param height        Hoehe
     * @param shelterHeight Unterstellhoehe
     * @param scorePoints   Punkte, die das Item im Spiel bringt
     * @param length        Laenge
     * @param width         Breite
     * @param name          Name fuer GUI-Darstellung
     */
    PlaceableItemType(boolean storagePlace, boolean floorItem, boolean storable,
                      Height height, Height shelterHeight, int scorePoints, int length, int width, String name) {
        this.storagePlace = storagePlace;
        this.floorItem = floorItem;
        this.storable = storable;
        this.height = height;
        this.shelterHeight = shelterHeight;
        this.scorePoints = scorePoints;
        this.length = length;
        this.width = width;
        this.name = name;
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

    /**
     * Gibt den Namen des Types so zurueck, wie er in die GUI dargestellt werden soll, zB "Einhorn" statt "UNICORON"
     * @return Name als String
     */
    public String getName() {
        return name;
    }
}
