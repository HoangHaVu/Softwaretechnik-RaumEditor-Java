package roomieboomie.business.highscore;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Beinhaltet und verwaltet eine Liste von {@link HighscoreRecord}s fuer einen Raum.
 */
public class HighscoreList {

    private class SortByPointsComparator implements Comparator<HighscoreRecord>{
        @Override
        public int compare(HighscoreRecord record1, HighscoreRecord record2) {
            return record1.compareTo(record2);
        }
    }

    private ArrayList<HighscoreRecord> highscoreList = new ArrayList<>();
    private SortByPointsComparator comparator = new SortByPointsComparator();

    /** TODO so richtig?
     * Gibt zurueck, auf welcher Platzierung man sich mit dem angegebenen Score befindet
     * @param score Punktzahl, die gesucht werden soll
     * @return Platzierung mit 1 beginnend
     */
    public int getPlacement(int score){
        for (int i = 0; i < highscoreList.size(); i++){
            if(highscoreList.get(i).getPoints() == score){
                return i + 1;
            }
        }
        return -1;
    }

    /**
     * Fuegt einen {@link HighscoreRecord} zur Liste hinzu und sortiert diese
     * @param record HighscoreRecord, der hinzugefuegt werden soll
     */
    public void addRecord(HighscoreRecord record){
        highscoreList.add(record);
        highscoreList.sort(comparator);
    }

    /**
     * @return Sortierte Liste mit allen {@link HighscoreRecord}s
     */
    public ArrayList<HighscoreRecord> getHighscoreList(){
        return highscoreList;
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
}
