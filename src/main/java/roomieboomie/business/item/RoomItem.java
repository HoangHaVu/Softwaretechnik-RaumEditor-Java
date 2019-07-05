package roomieboomie.business.item;

import javafx.scene.image.Image;

/**
 * Oberklasse von PlacalbeItem und LayoutItem. Beinhaltet Positioniertungs-,
 * Richtungs- und Laengenwerte
 */
public class RoomItem {
    private int x;
    private int y;
    private Orientation orientation;
    private int length;
    private int width;
    private Image image;

    /**
     * Neuer RoomItem
     * @param x x-Koordinate
     * @param y y-Koordinate
     * @param length Länge
     * @param width Breite
     * @param orientation Richtung
     */
    public RoomItem(int x, int y, int length, int width, Orientation orientation) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.length = length;
        this.width = width;
    }

    public RoomItem(int length, int width, Orientation orientation){
        this.length = length;
        this.width = width;
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
     * @return Höhe des Items
     */
    public int getLength() {
        return length;
    }

    public void setLength(int length){
        if (length > 0)
        this.length = length;
    }

    /**
     *
     * @param height Höhe des Items
     */
    public void setHeight(int height) {
        this.length = height;
    }

    /**
     *
     * @return Breite des Items
     */
    public int getWidth() {
        return width;
    }

    /**
     *
     * @param width Breite des Items
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     *
     * @return Richtungswert des Items
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Richtungswert des Items
     * @param orientation
     */
    public void setOrientation(Orientation orientation){
        this.orientation = orientation;
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

    public Image getImage() {
        return image;
    }
    public void setImage(Image image){this.image=image;}
}
