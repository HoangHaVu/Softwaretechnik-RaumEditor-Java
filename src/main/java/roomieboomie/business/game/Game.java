package roomieboomie.business.game;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import roomieboomie.business.item.placeable.PlaceableItem;
import roomieboomie.business.item.placeable.PlaceableItemType;
import roomieboomie.business.room.Room;
import roomieboomie.persistence.Config;

/**
 * Prototyp fuer Game
 */
public class Game {

    private Room room;
    byte[][] gameLayout;
    private ArrayList<PlaceableItem> placedItems;
    private final int SECONDSPERITEM = Config.get().SECONDSPERITEM() * 100;
    private int count;
    private SimpleIntegerProperty countProperty = new SimpleIntegerProperty();
    private Timer timer;
    private TimerTask task;
    private SimpleBooleanProperty timerRunning = new SimpleBooleanProperty(true);
    private int numberOfPlacedItems;
    private int numberOfItems;
    private int numberofFailures;

    public Game(Room room){
        this.room = room;
        testSetRoom();

        numberOfItems = room.getPlaceableItemList().size();
        this.gameLayout = room.getLayout();
        countProperty.set(SECONDSPERITEM);

        initTimer();

        PlaceableItem item1 = popItem();
        PlaceableItem item2 = popItem();
        PlaceableItem item3 = popItem();
        PlaceableItem item4 = popItem();
    }

    /**
     * Test
     */
    private void testSetRoom(){
        ArrayList<PlaceableItem> placeableItemList =  new ArrayList<>();
        placeableItemList.add(new PlaceableItem(PlaceableItemType.BED));
        placeableItemList.add(new PlaceableItem(PlaceableItemType.COUCH));
        placeableItemList.add(new PlaceableItem(PlaceableItemType.DINO));
        placeableItemList.add(new PlaceableItem(PlaceableItemType.TEDDY));

        room.setPlaceableItemList(placeableItemList);
    }

    /**
     * Initialisiert den Timer neu mit Sekundenangabe aus der Konfig
     */
    private void initTimer() {
        count = SECONDSPERITEM;
        this.timer = new Timer();
        this.task = new TimerTask() {
            @Override
            public void run() {
                if ( count > 0){
                    count --;
                    countProperty.set(count);
                } else {
                    timerRunning.set(false);
                    timer.cancel();
                    timer.purge();
                }
            }
        };
    }

    /**
     * Startet den TimerTask
     */
    public void startTimer(){
        timer.schedule(task, 0, 10);
    }

    /**
     * Stoppt den TimerTask, initialisiert ihn neu und startet ihn
     */
    public void resetTime(){
        timer.cancel();
        timer.purge();
        initTimer();
        startTimer();
    }

    /**
     * Property, das angibt, ob der Timer noch laeuft
     * @return SimpleBooleanProperty, das true beinhaltet, solange der Timer laeuft
     */
    public SimpleBooleanProperty getTimerRunning(){
        return timerRunning;
    }

    /**
     * Property, das angibt, auf welchem Sekundenstand der Timer ist
     * @return Sekudnenstand in Zentisekunden (hundertstsel)
     */
    public SimpleIntegerProperty getCountProperty(){
        return countProperty;
    }

    //TODO fehlversuche
    //TODO 3er queue

    /**
     * Holt eine Kopie des ersten PlaceableItem aus dem Room und loescht es aus dem Array
     * @return PlaceableItem, das als naechstes im Spiel platzier werden muss
     */
    private PlaceableItem popItem(){
        PlaceableItem returnItem = room.getPlaceableItemList().get(0).clone();
        room.getPlaceableItemList().remove(0);
        //TODO evl Vorschaubilder aktualisieren
        return returnItem;
    }

    /**
     * Prototyp, der spaeter Item hinzufuegen soll und ueberprueft, ob das Spiel gewonnen oder verloren ist
     * @return true, wenn das Spiel gewonnen wurde
     */
    public boolean addItem(){
        //if(addItem)
            numberOfItems++;
        //else
            numberofFailures++;
        if (numberOfPlacedItems == numberOfItems){
            return true;
        }
        return false;
    }
}