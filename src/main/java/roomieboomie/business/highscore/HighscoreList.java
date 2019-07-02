package roomieboomie.business.highscore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Beinhaltet und verwaltet eine Liste von {@link HighscoreRecord}s fuer einen Raum.
 */
public class HighscoreList implements Iterable<HighscoreRecord> {

    @Override
    public Iterator<HighscoreRecord> iterator() {
        return highscoreList.listIterator();
    }

    private class SortByPointsComparator implements Comparator<HighscoreRecord>{
        @Override
        public int compare(HighscoreRecord record1, HighscoreRecord record2) {
            return record1.compareTo(record2);
        }
    }

    private ArrayList<HighscoreRecord> highscoreList = new ArrayList<>();
    private SortByPointsComparator comparator = new SortByPointsComparator();

    /**
     * Fuegt einen {@link HighscoreRecord} zur Liste hinzu und sortiert diese.
     * Gibt die Platzierung des Scores zurueck.
     * @param record HighscoreRecord, der hinzugefuegt werden soll
     * @return Platzierung mit 1 beginnend
     */
    public int addRecord(HighscoreRecord record){
        highscoreList.add(record);
        highscoreList.sort(comparator);
        return highscoreList.indexOf(record) + 1;
    }

    /**
     * @return Sortierte Liste mit allen {@link HighscoreRecord}s
     */
    public ArrayList<HighscoreRecord> getList(){
        return highscoreList;
    }

    public int getHighestScore() {
        if(!highscoreList.isEmpty()){
            return highscoreList.get(0).getPoints();
        }
        return 0;
    }

    //TEST
    @Override
    public String toString() {
        String s = "";
        for (HighscoreRecord r : highscoreList){
            s += r.toString() + "\n";
        }
        return s;
    }

    @Override
    public int hashCode() {
        return highscoreList.hashCode();
    }
}
