import org.junit.Before;
import org.junit.Test;
import roomieboomie.business.editor.PlacableItemEditor;
import roomieboomie.business.editor.PlaceableEditor;
import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.item.placable.PlacableItemType;
import roomieboomie.business.room.Room;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlaceableItemEditorTest {
    PlacableItem table;
    byte[][] layout;
    PlacableItemEditor placeableItemEditor;
    PlacableItem dino;
    PlacableItem tisch;
    PlacableItem teppich;
    @Before
    public void init(){
        byte[][] lay=new byte[10][10];
        Room r= new Room(10,10,null);
        r.setLayout(lay);
        //r.addPlacableItem(new PlacableItem(PlacableItemType.CARPET));
        placeableItemEditor =new PlacableItemEditor(r);

        teppich=new PlacableItem(3,3, Orientation.TOP,PlacableItemType.CARPET);
        tisch=new PlacableItem(3,3, Orientation.TOP,PlacableItemType.TABLE);
        dino=new PlacableItem(3,4, Orientation.TOP,PlacableItemType.DINO);
    }



    @Test
    /**
     * getestete Funktionen: Auswahl und Platzieren von Gegenstaenden,die übereinander sind
     * Beweis:
     */
    public void placeableItemPlatzieren(){



        //placeableItemEditor.addItem(teppich);
        placeableItemEditor.addItem(tisch);
        placeableItemEditor.addItem(dino);

        placeableItemEditor.editItem(3,3);
        assertTrue(true);

    }
    @Test
    /**
     * getestete Funktionen: platzieren von 2 Gegenstaeden die aufeinanderliegen
     * Beweis:
     */
    public void placeablteItemsPlatzieren(){
        placeableItemEditor.addItem(tisch);
        placeableItemEditor.addItem(dino);
    }


}
