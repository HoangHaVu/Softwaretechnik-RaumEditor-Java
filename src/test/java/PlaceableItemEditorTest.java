import org.junit.Before;
import org.junit.Test;
import roomieboomie.business.editor.PlacableItemEditor;
import roomieboomie.business.editor.RoomEditor;
import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.layout.LayoutItemType;
import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.item.placable.PlacableItemType;
import roomieboomie.business.room.Room;
import roomieboomie.persistence.JsonHandler;
import roomieboomie.persistence.exception.JsonWritingException;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlaceableItemEditorTest {
    PlacableItem table;
    byte[][] layout;
    PlacableItemEditor placeableItemEditor;
    PlacableItem dino;
    PlacableItem tisch;
    PlacableItem teppich;
    PlacableItem tisch2;
    @Before
    public void init(){
        byte[][] lay=new byte[10][10];
        Room r= new Room(10,10,null);
        r.setLayout(lay);
        //r.addPlacableItem(new PlacableItem(PlacableItemType.CARPET));
        placeableItemEditor = new PlacableItemEditor(new JsonHandler());

        teppich=new PlacableItem(3,3, Orientation.TOP,PlacableItemType.CARPET);
        tisch=new PlacableItem(3,3, Orientation.TOP,PlacableItemType.TABLE);
        tisch2=new PlacableItem(3,3, Orientation.RIGHT,PlacableItemType.TABLE);
        dino=new PlacableItem(3,4, Orientation.TOP,PlacableItemType.DINO);
    }



    @Test

    public void placePlaceableItem(){
        tisch=new PlacableItem(1,1, Orientation.TOP,PlacableItemType.TABLE);
       // tisch2=new PlacableItem(5,5, Orientation.RIGHT,PlacableItemType.TABLE);
        PlacableItem bild= new PlacableItem(1,1 ,Orientation.TOP,PlacableItemType.BED);
       // PlacableItem bild2= new PlacableItem(5,5 ,Orientation.RIGHT,PlacableItemType.WALLPICTURE);

        placeableItemEditor.addItem(tisch);
        placeableItemEditor.addItem(bild);
       // placeableItemEditor.editItem(2,2);
        //placeableItemEditor.rotateCurItem();
        //placeableItemEditor.placeCurrItem(1,1);

        /*placeableItemEditor.setRoomPlacableItemList();
        try {
            placeableItemEditor.saveRoom();
        } catch (JsonWritingException e) {
            e.printStackTrace();
        }*/
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
        try{
            assertTrue(placeableItemEditor.getCurrentItem().getType().equals(PlacableItemType.TABLE));
        } catch (Exception e){
            e.printStackTrace();
        }
        
        //TODO Bedingung hinzufuegen (tisch hasNext isEmpty)
    }

   @Test
    public void saveRoom(){
       ArrayList<PlacableItem> items=new ArrayList<PlacableItem>();
       items.add(new PlacableItem(PlacableItemType.TABLE));

       RoomEditor roomEditor= new RoomEditor("level",true,items, new JsonHandler());
        
       try{
        roomEditor.selectnewItem(LayoutItemType.WALL);
        roomEditor.placeCurrItem(0,0);
        roomEditor.selectnewItem(LayoutItemType.WALL);
        roomEditor.rotateItem();
        roomEditor.placeCurrItem(0,0);
        roomEditor.selectnewItem(LayoutItemType.WALL);
        roomEditor.placeCurrItem(9,0);
        roomEditor.selectnewItem(LayoutItemType.WALL);
        roomEditor.rotateItem();
        roomEditor.placeCurrItem(0,9);
 
        roomEditor.selectnewItem(LayoutItemType.DOOR);
        roomEditor.placeCurrItem(0,0);
 
        roomEditor.selectnewItem(LayoutItemType.WINDOW);
        roomEditor.placeCurrItem(0,9);
       } catch (Exception e){
           e.printStackTrace();
       }
      

       roomEditor.getPlaceableEditor().setRoom(roomEditor.getRoom());


       assertTrue(true);

    }



}
