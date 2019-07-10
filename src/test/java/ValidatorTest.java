import org.junit.Before;
import org.junit.Test;
import roomieboomie.business.editor.PlaceableEditor;
import roomieboomie.business.editor.RoomEditor;
import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.layout.LayoutItem;
import roomieboomie.business.item.layout.LayoutItemType;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.item.placable.PlacableItemType;
import roomieboomie.business.room.Room;
import roomieboomie.business.validation.Validator;
import roomieboomie.persistence.Config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidatorTest {
    LayoutItem wall;
    LayoutItem window;
    LayoutItem door;
    Validator validator;
    byte[][] layout;

    @Before
    public void init(){
        Room r= new Room(60,60,null);
        validator = new Validator();
        layout= r.getLayout();
        wall=new LayoutItem(LayoutItemType.WALL,1,1,10,1, Orientation.TOP);
        window=new LayoutItem(LayoutItemType.WINDOW,2,2,5,1, Orientation.TOP);
        door=new LayoutItem(LayoutItemType.DOOR,3,3,2,1, Orientation.TOP);
        for (int i=0;i<10;i++){
            for(int j =0;j<10;j++){
                layout[i][j]= Config.get().LAYOUTEXTERIORVALUE();
            }
        }


    }
    @Test
    public void validate(){

        assertTrue(validator.validateLayoutPlacement(wall,layout));
        assertFalse(validator.validateLayoutPlacement(window,layout));
        assertFalse(validator.validateLayoutPlacement(door,layout));

    }
}
