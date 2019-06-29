package roomieboomie.business.highscore;

import roomieboomie.business.user.User;

/**
 * Eintrag in einer HighscoreList mit User, Zeit und erreichten Punkten
 */
public class HighscoreRecord implements Comparable{
    private int time;
    private int points;
    private User user;

    /**
     * Erstellt neuen HighscoreRecord
     * @param time Zeit, die benoetigt wurde
     * @param points Erreichte Punkte
     * @param user User, der gespielt hat
     */
    public HighscoreRecord(int time, int points, User user) {
        this.time = time;
        this.points = points;
        this.user = user;
    }

    /**
     * @return Benoetigte Zeit
     */
    public int getTime() {
        return time;
    }

    /**
     * @return Erreichte Punkte
     */
    public int getPoints() {
        return points;
    }

    /**
     * @return User, der gespielt hat
     */
    public User getUser() {
        return user;
    }

    /**
     * Vergleicht zwei {@link HighscoreRecord}s miteinander anhand ihrer Punktzahl.
     * Ist die Punktzahl gleich,
     * @param o HighscoreRecord, der verglichen werden soll
     * @return
     */
    @Override
    public int compareTo(Object o) {
        HighscoreRecord compareRecord = (HighscoreRecord) o;
        if (this.points == compareRecord.getPoints()){
            return Integer.compare(this.time, compareRecord.getTime());
        } else{
            return Integer.compare(compareRecord.getPoints(), this.points);
        }
    }

    //TEST
    @Override
    public String toString(){
        return "User: " + user.getName() + ", Punkte: " + points + ", Zeit: " + time;
    }

    @Override
    public int hashCode() {
        return time * points * user.getName().hashCode();
    }
}
