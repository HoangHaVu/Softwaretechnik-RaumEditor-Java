import org.junit.Before;
import org.junit.Test;
import roomieboomie.business.RoomieBoomieManager;
import roomieboomie.business.editor.PlaceableItemEditor;
import roomieboomie.business.editor.RoomEditor;
import roomieboomie.business.item.Orientation;
import roomieboomie.business.item.layout.LayoutItemType;
import roomieboomie.business.item.placeable.PlaceableItem;
import roomieboomie.business.item.placeable.PlaceableItemType;
import roomieboomie.business.room.Room;
import roomieboomie.persistence.JsonHandler;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlaceableItemEditorTest {
    PlaceableItem table;
    byte[][] layout;
    PlaceableItemEditor placeableItemEditor;
    PlaceableItem dino;
    PlaceableItem tisch;
    PlaceableItem teppich;
    PlaceableItem tisch2;
    @Before
    public void init(){
        byte[][] lay=new byte[10][10];
        Room r= new Room(10,10,null);
        r.setLayout(lay);
        //r.addPlaceableItem(new PlaceableItem(PlaceableItemType.CARPET));
        placeableItemEditor = new PlaceableItemEditor();

        teppich=new PlaceableItem(3,3, Orientation.TOP, PlaceableItemType.CARPET);
        tisch=new PlaceableItem(3,3, Orientation.TOP, PlaceableItemType.TABLE);
        tisch2=new PlaceableItem(3,3, Orientation.RIGHT, PlaceableItemType.TABLE);
        dino=new PlaceableItem(3,4, Orientation.TOP, PlaceableItemType.DINO);
    }



    @Test

    public void placePlaceableItem(){
        tisch=new PlaceableItem(1,1, Orientation.TOP, PlaceableItemType.TABLE);
       // tisch2=new PlaceableItem(5,5, Orientation.RIGHT,PlaceableItemType.TABLE);
        PlaceableItem bild= new PlaceableItem(1,1 ,Orientation.TOP, PlaceableItemType.BED);
       // PlaceableItem bild2= new PlaceableItem(5,5 ,Orientation.RIGHT,PlaceableItemType.WALLPICTURE);

        placeableItemEditor.addItem(tisch);
        placeableItemEditor.addItem(bild);
       // placeableItemEditor.editItem(2,2);
        //placeableItemEditor.rotateCurItem();
        //placeableItemEditor.placeCurrItem(1,1);

        /*placeableItemEditor.setRoomPlaceableItemList();
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
        assertTrue(!placeableItemEditor.getPlaceableItemList().isEmpty());
        assertTrue(placeableItemEditor.getPlaceableItemList().get(0)!=null);//TODO Bedingung hinzufuegen (hasNext ungleich null)
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
        assertTrue(placeableItemEditor.getPlaceableItemList().isEmpty());
        //aktueller Gegenstand sollte dann der Tisch sein
        try{
            assertTrue(placeableItemEditor.getCurrentItem().getType().equals(PlaceableItemType.TABLE));
        } catch (Exception e){
            e.printStackTrace();
        }
        
        //TODO Bedingung hinzufuegen (tisch hasNext isEmpty)
    }

   @Test
    public void saveRoom(){
       ArrayList<PlaceableItem> items=new ArrayList<PlaceableItem>();
       items.add(new PlaceableItem(PlaceableItemType.TABLE));

       //RoomEditor roomEditor= new RoomEditor("level",true,items, new JsonHandler());
       //TODO neuer konstruktor
       RoomEditor roomEditor = new RoomEditor(new JsonHandler(), new RoomieBoomieManager());

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
