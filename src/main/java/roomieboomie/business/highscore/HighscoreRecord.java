package roomieboomie.business.highscore;

/**
 * Eintrag in einer HighscoreList mit User, Zeit und erreichten Punkten
 */
public class HighscoreRecord implements Comparable{
    private int time;
    private int points;
    private String username;

    /**
     * Erstellt neuen HighscoreRecord mit Time
     * @param time Zeit, die benoetigt wurde
     * @param points Erreichte Punkte
     * @param username Name des Users, der gespielt hat
     */
    public HighscoreRecord(int time, int points, String username) {
        this.time = time;
        this.points = points;
        this.username = username;
    }

    /**
     * Erstellt neuen HighscoreRecord ohne Time
     * @param points Erreichte Punkte
     * @param username Name des Users, der gespielt hat
     */
    public HighscoreRecord(int points, String username) {
        this.points = points;
        this.username = username;
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
    public String getUsername() {
        return username;
    }

    /**
     * Vergleicht zwei {@link HighscoreRecord}s miteinander anhand ihrer Punktzahl.
     * Ist die Punktzahl gleich,
     * @param o HighscoreRecord, der verglichen werden soll
     * @return Vergleichs-int
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
        return "User: " + username + ", Punkte: " + points + ", Zeit: " + time;
    }

    @Override
    public int hashCode() {
        return time * points * username.hashCode();
    }
}
