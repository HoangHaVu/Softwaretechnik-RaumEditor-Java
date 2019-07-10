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
    public void placePlaceableItem(){
        //placeableItemEditor.addItem(teppich);
        placeableItemEditor.addItem(tisch);
        placeableItemEditor.addItem(dino);

        placeableItemEditor.editItem(3,3);
        assertTrue(true);

    }
    @Test
    /**
     * getestete Funktionen: platzieren von 2 Gegenstaeden die aufeinanderliegen
     * Beweis: im layout vom Tisch ist der Wert vom Dino drinne
     */
    public void placeItems(){
        placeableItemEditor.addItem(tisch);
        placeableItemEditor.addItem(dino);
        assertTrue(!placeableItemEditor.getPlacableItemList().isEmpty());
        assertTrue(placeableItemEditor.getPlacableItemList().get(0)!=null);//TODO Bedingung hinzufuegen (hasNext ungleich null)
        assertTrue(true);//TODO Bedingung hinzufuegen
    }


    @Test
    /**
     * getestete Funktionen: loeschen von Gegenstaenden
     * Beweis: der Tisch hat keine Werte in seinem layout --> Dino wurde geloescht
     */
    public void deletePlaceableItem(){
        placeableItemEditor.addItem(tisch);
        placeableItemEditor.addItem(dino);
        placeableItemEditor.delItem(3,4);
        assertTrue(true); //TODO Bedingung hinzufuegen (tisch hasNext null)
    }

    @Test
    /**
     * getestete Funktionen: editieren von Gegenstaenden
     * Beweis: der ausgewaehlte Gegenstand wird zunaechst geloescht und als aktuellen Gegenstand gesetzt
     */
    public void editPlaceableEditor(){
        placeableItemEditor.addItem(tisch);
        placeableItemEditor.addItem(dino);

        placeableItemEditor.editItem(3,3);
        //Liste sollte leer sein weil Dino auf dem Tisch liegt
        assertTrue(placeableItemEditor.getPlacableItemList().isEmpty());
        //aktueller Gegenstand sollte dann der Tisch sein
        assertTrue(placeableItemEditor.getCurItem().getType().equals(PlacableItemType.TABLE));
        //TODO Bedingung hinzufuegen (tisch hasNext isEmpty)
    }


}
