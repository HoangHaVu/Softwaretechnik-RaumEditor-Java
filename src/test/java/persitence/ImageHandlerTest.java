package persitence;

import javafx.scene.image.Image;
import org.junit.Before;
import org.junit.Test;
import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.item.placable.PlacableItemType;

public class ImageHandlerTest {
    @Before
    public void init(){

    }

    @Test
    public void testPlacableImage(){
        boolean success = true;
        PlacableItem item = new PlacableItem(0,0, Orientation.TOP, PlacableItemType.DINO);
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
