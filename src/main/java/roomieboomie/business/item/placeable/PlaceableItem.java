package roomieboomie.business.item.placeable;

import javafx.scene.image.Image;
import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.RoomItem;
import roomieboomie.persistence.ImageHandler;

/**
 * Platzierbares Objekt fuer ein Spiel
 */
public class PlaceableItem extends RoomItem {
    private PlaceableItemType type;
    private PlaceableItem next;
    private byte[][] layout;

    /**
     * Neues PlaceableItem
     * @param x x-Koordinate
     * @param y y-Koordinate
     * @param orientation Richtung
     * @param type Typ des Items
     */
    public PlaceableItem(int x, int y, Orientation orientation, PlaceableItemType type) {
        super(x, y,type.getLength(),type.getWidth(), orientation);
        this.type = type;
        this.next = null;

        if (this.getOrientation().isVertical()){
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
     * Neues PlaceableItem ohne Informationen zu Platzierung
     * @param type Typ des Items
     */
    public PlaceableItem(PlaceableItemType type){
        super(type.getLength(),type.getWidth(),Orientation.TOP);
        this.type = type;
        this.next = null;
        if (this.getOrientation().isVertical()){
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

    public PlaceableItem getNext(){
        return this.next;
    }


    public PlaceableItem findItemByCoordinates(int x, int y){


        x = x - this.getX();
        y = y - this.getY();

        if (this.layout[y][x] == 0) return this;

        else{

            return next.findItemByCoordinates(x, y);
        }

    }

    public boolean hasNextOn(int x, int y){

        if (this.getOrientation().isVertical()){
            if (this.layout[y][x] != 0){
                return true;
            }
            return false;
        }else{
            if(this.layout[x][y]!=0){
                return true;
            }
            return false;
        }
    }

    public void placeItemOnThis(PlaceableItem item){
        removeItemFromThis();
        this.next = item;
        int startX = item.getX();
        int startY = item.getY();
        if (item.getOrientation().isHorizontal()){
            int temp = startX;
            startX = startY;
            startY = temp;
        }

        int endX = startX + item.getLength();
        int endY = startY + item.getWidth();
        byte placeNumber = 1;

        if (item.getOrientation().isHorizontal()){
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
    public PlaceableItemType getType() {
        return type;
    }

    /**
     * Gibt zurueck, ob dieses Item unter das Vergleichsobjekt passt
     * @param compareItem Item, unter das dieses gestellt werden soll
     * @return Ob die Unterstellhoehe hoch genug ist
     */
    public boolean fitsBeneath(PlaceableItem compareItem){
        return this.type.getHeight().fitsBeneath(compareItem.getType().getHeight());
    }

    @Override
    public int hashCode() {
        return type.toString().hashCode();
    }
    public void setNext(PlaceableItem item){
        this.next = item;
    }

    /**
     * Ruft ueber den ImageHandler die zum Typ und Orientation passende Datei ab
     * @return JavaFX-Image
     */
    public Image getTextureImage(){
        return ImageHandler.get().placeableItemImage(type + "_" + getOrientation());
    }

    public void setLayout(byte[][] layout){
        this.layout = layout;
    }

    /**
     * @return Kopie des Objekts mit X, Y, Orientation und Typ
     */
    public PlaceableItem clone(){

        PlaceableItem newItem = new PlaceableItem(this.getType());
        return  newItem;

    }
}
