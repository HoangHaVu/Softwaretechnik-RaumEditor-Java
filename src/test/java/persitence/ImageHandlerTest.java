package persitence;

import javafx.scene.image.Image;
import org.junit.Before;
import org.junit.Test;
import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.placeable.PlaceableItem;
import roomieboomie.business.item.placeable.PlaceableItemType;

public class ImageHandlerTest {
    @Before
    public void init(){

    }

    @Test
    public void testPlaceableImage(){
        boolean success = true;
        PlaceableItem item = new PlaceableItem(0,0, Orientation.TOP, PlaceableItemType.DINO);
        try{
            Image img = item.getTextureImage();
        }catch (IllegalArgumentException e){
            success = false;
        }catch(RuntimeException ex){
            // das ist okay. Bedeutet, Datei wurde gefunden, kann nur nicht angezeigt werden, a hier kein JFX
        }

        assert success;
    }
}
