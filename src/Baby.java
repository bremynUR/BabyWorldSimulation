import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Baby {
    
    Random rand = new Random();
    Location location;
    HashMap<Location, ArrayList<Feedback>> feedbackMap;
    String name;
    BabyWorld babyWorld;
    Boolean teacher;
    String curGuess;

    public Baby(String name, BabyWorld world, boolean teacher) {
        feedbackMap = new HashMap<>();
        this.name = name;
        Integer randInt = rand.nextInt(world.getCurLocations().size());
        location = world.getCurLocations().get(randInt);
        this.teacher = teacher;
        this.babyWorld = world;
        location.addBaby(this);
    }
    
    public String getCurGuess() {
        return curGuess;
    }

    public HashMap<Location, ArrayList<Feedback>> getMap() {
        return feedbackMap;
    }

    public void setMap(HashMap<Location, ArrayList<Feedback>> newMap) {
        this.feedbackMap = newMap;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public Boolean getTeacher() {
        return teacher;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void addFeedback(Feedback feedback, Location loc) {
        if (feedbackMap.isEmpty() || !feedbackMap.containsKey(loc)) {
            ArrayList<Feedback> feedbackList = new ArrayList<Feedback>();
            feedbackList.add(feedback);
            feedbackMap.put(loc, feedbackList);
        } else {
            ArrayList<Feedback> feedbackList;
            feedbackList = feedbackMap.get(loc);
            feedbackList.add(feedback);
            feedbackMap.put(loc, feedbackList);
        }
    }
    
    public void removeFeedback(Feedback feedback) {
        if (!feedbackMap.isEmpty() && feedbackMap.containsKey(this.location)) {
            ArrayList<Feedback> feedbackList;
            feedbackList = feedbackMap.get(this.location);
            if (feedbackList.contains(feedback)) {
                feedbackList.remove(feedback);
            }
        }
    }

    public ArrayList<Feedback> getFeedback(Location location) {
        if (feedbackMap.isEmpty() || !feedbackMap.containsKey(this.location)) {
            return null;
        } else {
            ArrayList<Feedback> feedbackList;
            feedbackList = feedbackMap.get(this.location);
            if (feedbackList.isEmpty()) {
                return null;
            } else {
                return feedbackList;
            }
        }
    }

    public void generateGuess() {
        ArrayList<Feedback> curFeedback = feedbackMap.get(location);
        HashMap<String, ArrayList<Letter>> letterMatrix = generateLetterMatrix(curFeedback);

        //for (HashMap.Entry<Letter, ArrayList<Letter>> entry : letterMatrix.entrySet()) {
        //    for (int i = 0; i < entry.getValue().size(); i++) {
        //        System.out.println(entry.getKey().getSymbol() + " / " + entry.getValue().get(i).getSymbol() 
        //        + " / " + entry.getValue().get(i).getWeight());
        //    }
        //}
        //System.out.println("DONE!");

        Letter curLetter = new Letter("/", 0);
        String guess = "";
        Integer i = 0;

        while (!(curLetter.getSymbol() == ".")) {
            guess = guess + curLetter.getSymbol();
            curLetter = generateNextLetter(curLetter, letterMatrix);
            i++;
        }

        guess = guess.substring(1, guess.length());
        this.curGuess = guess;

        if (teacher) {
            this.curGuess = location.getObject();
        }

        System.out.println(name + " at " + this.location.getObject() + " says " + this.curGuess);
        
    }

    Letter generateNextLetter(Letter curLetter, HashMap<String, ArrayList<Letter>> letterMatrix) {
        
        Letter nextLetter = null;
        ArrayList<Letter> possibleSeconds = new ArrayList<>();
        for (HashMap.Entry<String, ArrayList<Letter>> entry : letterMatrix.entrySet()) {
            if (entry.getKey().equals(curLetter.getSymbol())) {
                possibleSeconds = entry.getValue();
                break;
            }
        }

        HashMap<Letter, Double> LettersWithThresholds = new HashMap<>();
        double threshold = 0;

        for (int i = 0; i < possibleSeconds.size(); i++) {
            Letter curChar = possibleSeconds.get(i);
            threshold = threshold + possibleSeconds.get(i).getWeight();
            LettersWithThresholds.put(curChar, threshold);
        }

        double fraction = rand.nextDouble();

        double chance = threshold * fraction;

        for (HashMap.Entry<Letter, Double> entry : LettersWithThresholds.entrySet()) {
            if (entry.getValue() >= chance) {
                nextLetter = entry.getKey();
                break;
            }
        }

        return nextLetter;
    }

    HashMap<String, ArrayList<Letter>> generateLetterMatrix(ArrayList<Feedback> feedbackList) {
        HashMap<String, ArrayList<Letter>> letterMatrix = new HashMap<>();

        for (Feedback feedback : feedbackList) {
            ArrayList<Letter> feedbackLetters = new ArrayList<>();
            feedbackLetters.add(new Letter("/", 0));
            for (int i = 0; i < feedback.getRecomendation().length(); i++) {
                String symbol = feedback.getRecomendation().substring(i, i+1);
                Letter letter = new Letter(symbol, feedback.getWeight());
                feedbackLetters.add(letter);
            }
            feedbackLetters.add(new Letter(".", feedback.getWeight()));

            for (int i = 0; i < feedbackLetters.size() - 1; i++) {

                String letter = feedbackLetters.get(i).getSymbol();
                Letter nextLetter = feedbackLetters.get(i + 1);
                ArrayList<Letter> secondLetterList = new ArrayList<>();
                String keyToStoreAt = letter;

                for (HashMap.Entry<String, ArrayList<Letter>> entry : letterMatrix.entrySet()) {

                    if (entry.getKey() == letter) {
                        secondLetterList = entry.getValue();
                        keyToStoreAt = entry.getKey();
                        break;
                    }
                }
                
                secondLetterList.add(nextLetter);
                letterMatrix.put(keyToStoreAt, secondLetterList);
                
            }
        }

        return letterMatrix;
            
    }

    public void logWeights() {
        if (teacher) {
            //do nothing, you know you are right
        } else {
            Feedback feedback;
            for (Baby baby : location.getBabies()) {
                if (!this.equals(baby)) {
                    double weightAve = 0;
                    Integer recentFeedback = 0;
                    for (Feedback f : baby.getFeedback(location)) {
                        if (!f.isRecent()) {
                            weightAve += f.getWeight();
                        } else {
                            recentFeedback++;
                        }
                    }
                    Integer n = baby.getFeedback(location).size() - recentFeedback;
                    weightAve = weightAve/n;    // weight = average weight of feedback from giver
                    feedback = new Feedback(baby.getCurGuess(), weightAve, baby.getName());
                    addFeedback(feedback, location);
                }
            }
        }
    }

    public Location randomizeLocation() {
        Integer randInt = rand.nextInt(babyWorld.getCurLocations().size());
        location = babyWorld.getCurLocations().get(randInt);
        location.addBaby(this);
        return location;
    }
}
