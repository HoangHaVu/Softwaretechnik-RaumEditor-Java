package roomieboomie.business.user;

/**
 * Anwender, der das Programm benutzt. Kann mit oder ohne Angabe des erreichten Levels erstellt werden.
 */
public class User {
    private String name;
    private int reachedLevel;

    /**
     * Neuer User inklusive Angabe des Levels, das fuer ihn freigeschaltet ist.
     * @param name Eindeutiger Name des Users
     * @param reachedLevel Index des hoechsten Levels, das fuer den User freigeschaltet ist
     */
    public User(String name, int reachedLevel) {
        this.name = name;
        this.reachedLevel = reachedLevel;
    }

    /**
     * Neuer User, der noch nie gespielt hat und bei Level 1 beginnt.
     * @param name
     */
    public User(String name){
        this.name = name;
        this.reachedLevel = 1;
    }

    /**
     * @return Name des Users
     */
    public String getName(){
        return name;
    }

    /**
     * @return Hoechstes erreichtes Level des Users
     */
    public int getReachedLevel() {
        return reachedLevel;
    }

    /**
     * Schaltet fuer den User das naechste Level frei.
     * @return Index des Levels, das nun freigeschaltet wurde
     */
    public int levelUp(){
        return ++ reachedLevel;
    }

    @Override
    public int hashCode() {
        return testHash(name, reachedLevel);
    }

    /**
     * Kann statisch den HashCode eines User-Objektes berechnen. Somit kann ueberprueft werden, welchen Hashcode ein
     * erstelltes User-Objekt mit diesen Attributen haben wuerde
     * @param name Name des Users
     * @param reachedLevel Hoechstes erreichtes Level des Users
     * @return HashCode
     */
    public static int testHash(String name, int reachedLevel){
        return name.hashCode() * Integer.hashCode(reachedLevel);
    }
}
