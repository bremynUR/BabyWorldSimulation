import java.util.ArrayList;

public class Location {

    String object;
    Integer ID;
    ArrayList<Baby> babies;

    public Location(String object, Integer iD) {
        this.object = object;
        ID = iD;
        babies = new ArrayList<>();
    }

    public String getObject() {
        return object;
    }

    public ArrayList<Baby> getBabies() {
        return babies;
    }

    public void addBaby(Baby baby) {
        babies.add(baby);
    }

}
