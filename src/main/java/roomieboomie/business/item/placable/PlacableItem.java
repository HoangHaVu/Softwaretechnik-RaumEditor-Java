package roomieboomie.business.item.placable;

import javafx.scene.image.Image;
import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.RoomItem;
import roomieboomie.persistence.ImageHandler;

/**
 * Platzierbares Objekt fuer ein Spiel
 */
public class PlacableItem extends RoomItem {
    private PlacableItemType type;
    private PlacableItem next;
    private byte[][] layout;

    /**
     * Neues PlacableItem
     * @param x x-Koordinate
     * @param y y-Koordinate
     * @param orientation Richtung
     * @param type Typ des Items
     */
    public PlacableItem(int x, int y, Orientation orientation, PlacableItemType type) {
        super(x, y,type.getLength(),type.getWidth(), orientation);
        this.type = type;
        this.next = null;

        if (this.getOrientation() == Orientation.TOP || this.getOrientation() == Orientation.BOTTOM){
            this.layout = new byte[this.getLength()][this.getWidth()];
            for (int i = 0; i < this.getLength(); i++){
                for (int j = 0; j < this.getWidth(); j++){
                    this.layout[i][j] = 0;
                }
            }
        } else{
            this.layout = new byte[this.getWidth()][this.getLength()];
            for (int i = 0; i < this.getWidth(); i++){
                for (int j = 0; j < this.getLength(); j++){
                    this.layout[i][j] = 0;
                }
            }
        }
    }

    /**
     * Neues PlacableItem ohne Informationen zu Platzierung
     * @param type Typ des Items
     */
    public PlacableItem(PlacableItemType type){
        super(type.getLength(),type.getWidth(),Orientation.TOP);
        this.type = type;
        this.next = null;
        if (this.getOrientation() == Orientation.TOP || this.getOrientation() == Orientation.BOTTOM){
            this.layout = new byte[this.getLength()][this.getWidth()];
            for (int i = 0; i < this.getLength(); i++){
                for (int j = 0; j < this.getWidth(); j++){
                    this.layout[i][j] = 0;
                }
            }
        } else{
            this.layout = new byte[this.getWidth()][this.getLength()];
            for (int i = 0; i < this.getWidth(); i++){
                for (int j = 0; j < this.getLength(); j++){
                    this.layout[i][j] = 0;
                }
            }
        }

    }

    public boolean hasNext(){
        
        if (this.next == null) return false;

        return true;
    }

    public PlacableItem getNext(){
        return this.next;
    }


    public PlacableItem findItemByCoordinates(int x, int y){


        x = x - this.getX();
        y = y - this.getY();

        if (this.layout[y][x] == 0) return this;

        else{

            return next.findItemByCoordinates(x, y);
        }

    }

    public boolean hasNextOn(int x, int y){
        if (this.layout[y][x] != 0){
            return true;
        }
        return false;
    }

    public void placeItemOnThis(PlacableItem item){
        removeItemFromThis();
        this.next = item;
        int startX = item.getX();
        int startY = item.getY();
        if (item.getOrientation() == Orientation.LEFT|| item.getOrientation() == Orientation.RIGHT){
            int temp = startX;
            startX = startY;
            startY = temp;
        }



        if (this.layout[startY][startX] != 0){
            item.setX(item.getX() - this.getX());
            item.setY(item.getY() - this.getY());
            this.next.placeItemOnThis(item);
            return;
        }

        int endX = startX + item.getLength();
        int endY = startY + item.getWidth();
        byte placeNumber = 1;

        if (item.getOrientation() == Orientation.LEFT || item.getOrientation() == Orientation.RIGHT){
            endX = startX + item.getWidth();
            endY = startY + item.getLength();
        }

        this.next = item;

        for (int y = startY; y < endY; y++){
            for (int x = startX; x < endX; x++){
                layout[y][x] = placeNumber;
            }
        }
        
    }

    public void removeItemFromThis(){
        this.next = null;
        for (int i = 0; i < this.layout.length; i++){
            for (int j = 0; j < this.layout[0].length; j++){
                this.layout[i][j] = 0;
            }
        }
    }



    /**
     * @return Typ des Items
     */
    public PlacableItemType getType() {
        return type;
    }

    /**
     * Gibt zurueck, ob dieses Item unter das Vergleichsobjekt passt
     * @param compareItem Item, unter das dieses gestellt werden soll
     * @return Ob die Unterstellhoehe hoch genug ist
     */
    public boolean fitsBeneath(PlacableItem compareItem){
        return this.type.getHeight().fitsBeneath(compareItem.getType().getHeight());
    }

    @Override
    public int hashCode() {
        return type.toString().hashCode();
    }
    public void setNext(PlacableItem item){
        this.next = item;
    }

    /**
     * Ruft ueber den ImageHandler die zum Typ und Orientation passende Datei ab
     * @return JavaFX-Image
     */
    public Image getTextureImage(){
        return ImageHandler.get().placableItemImage(type + "_" + getOrientation());
    }

    public void setLayout(byte[][] layout){
        this.layout = layout;
    }

    /**
     * @return Kopie des Objekts mit X, Y, Orientation und Typ
     */
    public PlacableItem clone(){

        PlacableItem newItem = new PlacableItem(this.getType());
        return  newItem;

    }
}
