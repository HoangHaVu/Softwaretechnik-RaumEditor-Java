package roomieboomie.business.item;

/**
 * Oberklasse von PlacalbeItem und LayoutItem. Beinhaltet Positioniertungs-, Richtungs- und Laengenwerte
 */
public class RoomItem {
    private int x;
    private int y;
    private Orientation orientation;

    /**
     * Neuer RoomItem
     * @param x x-Koordinate
     * @param y y-Koordinate
     * @param orientation Richtung
     */
    public RoomItem(int x, int y, Orientation orientation) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
    }

    /**
     *
     * @return x-Koordinate
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @param x x-Koordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     *
     * @return y-Koordinate
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @param y y-Koordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     *
     * @return Richtungswert des Items
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Dreht das Item um 90 Grad nach rechts
     */
    public void turnRight() {
        this.orientation = this.orientation.getNext();
    }

    /**
     * Dreht das Item um 90 Grad nach links
     */
    public void turnLeft() {
        this.orientation = this.orientation.getPrev();
    }

    /**
     *
     * @param input
     * @return
     */
    public String svgToPath(String input){
        return null; //TODO
    }
}
