import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Random;

public class BabyWorld {

    ArrayList<Baby> curBabbies;
    ArrayList<Location> curLocations;
    Scanner scanner = new Scanner(System.in);
    Random rand = new Random();
    NameGen gener = new NameGen();
    Integer interactions = 0;
    double accuracy = 0;
    ArrayList<Double> accuracyList = new ArrayList<>();
    Integer size = 0;

    public BabyWorld() {
        this.curBabbies = new ArrayList<>();
        this.curLocations = new ArrayList<>();
    }

    String askInitials () {
        Integer numLocations;
        Integer numBabies;
        Integer numTeachers;

        boundry();
        boundry();
        System.out.println("How many locations should the environment consist of?\n");
        
        String input = this.scanner.nextLine();

        try {
            numLocations = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            invalidEntry();
            System.out.println("Please try entering inital conditions again\n");
            askInitials();
            return "";
        }

        System.out.println("How many babies (learner agents) should be in the environemnt?\n");
        
        String input2 = this.scanner.nextLine();

        try {
            numBabies = Integer.parseInt(input2);
        } catch (NumberFormatException e) {
            invalidEntry();
            System.out.println("Please try entering inital conditions again\n");
            askInitials();
            return "";
        }

        boundry();

        System.out.println("How many teachers (all knowing agents) should be in the environemnt?\n");
        
        String input3 = this.scanner.nextLine();

        try {
            numTeachers = Integer.parseInt(input3);
        } catch (NumberFormatException e) {
            invalidEntry();
            System.out.println("Please try entering inital conditions again\n");
            askInitials();
            return "";
        }

        System.out.println("What length should the target words be?\n");
        
        String input4 = this.scanner.nextLine();

        try {
            size = Integer.parseInt(input4);
        } catch (NumberFormatException e) {
            invalidEntry();
            System.out.println("Please try entering inital conditions again\n");
            askInitials();
            return "";
        }

        boundry();
        
        initWorld(numLocations, numBabies, numTeachers);

        return "";
    }

    void initWorld(Integer numLocations, Integer numBabies, Integer numTeachers) {
        for (int i = 0; i < numLocations; i++) {
            String name = gener.generateName(size);
            Location loc = new Location(name, i);
            curLocations.add(loc);
        }

        for (int i = 0; i < numBabies; i++) {
            String name = String.valueOf(i);
            String recomendation;
            Baby baby = new Baby("baby " + name, this, false);
            for (Location loc : curLocations) {
                for (int in = 0; in < 2; in++) {
                    recomendation = gener.generateName(size);
                    baby.addFeedback(new Feedback(recomendation, 5, "initial"), loc);
                }
            }
            curBabbies.add(baby);
        }

        for (int i = 0; i < numTeachers; i++) {
        Baby teacher = new Baby("Teach!", this, true);
            for (Location loc : curLocations) {
                String recomendation;
                for (int in = 0; in < 2; in++) {
                    recomendation = loc.getObject();
                    teacher.addFeedback(new Feedback(recomendation, 2000, "initial"), loc);        //Teachers start with highly weighted recomendations
                }
            }
            curBabbies.add(teacher);
        } 

        String babyString;

        boundry();

        System.out.println("Initial world (babies will go to random locations before each interaction round):");

        boundry();

        for (Location loc : curLocations) {
            babyString = "Babies at " + loc.getObject() + ": ";
            Boolean first = true;
            for (Baby baby : curBabbies) {
                if (baby.getLocation() == loc) {
                    if (first) {
                        babyString = babyString + baby.getName();
                        first = false;
                    } else {
                        babyString = babyString + ", " + baby.getName();
                    }
                    loc.addBaby(baby);
                }
            }
            if (first) {
                babyString = babyString + "NONE";
            }

            System.out.println(babyString);
        }

        askQuestion();


    }

    private void interactBabies() {
        interactions++;
        for (Location loc : curLocations) {
            loc.getBabies().clear();
        }
        for (Baby baby : curBabbies) {
            HashMap<Location, ArrayList<Feedback>> feedbackMap = baby.getMap();
            HashMap<Location, ArrayList<Feedback>> newMap = new HashMap<>();
            for (HashMap.Entry<Location, ArrayList<Feedback>> entry : feedbackMap.entrySet()) {
                ArrayList<Feedback> feedbackList = new ArrayList<>();
                for (Feedback f : entry.getValue()) {
                    f.makeOld();
                    feedbackList.add(f);
                }
                newMap.put(entry.getKey(), feedbackList);
            }
            baby.setMap(newMap);
        }
        for (Baby baby : curBabbies) {
            baby.randomizeLocation();
        }
        for (Baby baby : curBabbies) {
            baby.generateGuess();
        }
        for (Baby baby : curBabbies) {
            baby.logWeights();
        }

        getAccuracy();
        accuracyList.add(accuracy);
    }

    private void askQuestion() {
        boundry();
        boundry();
        System.out.println("enter 1 to initiate 1 round of interaction between agents\n");
        System.out.println("enter 2 to initiate 10 rounds of interaction between agents\n");
        // System.out.println("enter 3 to see stats\n");
        System.out.println("enter 3 to remove all teachers\n");
        System.out.println("enter 4 to see accuracy over time\n");
        System.out.println("enter 5 to restart program");
        boundry();
        System.out.println();
        
        String input = this.scanner.nextLine();

        System.out.println();
        boundry();

        switch (input) {
            case "1":
            interactBabies();
            askQuestion();
            break;

            case "2":
            for (int i = 0; i < 10; i++) {
                interactBabies();
                boundry();
            }
            askQuestion();
            break;

            case "x":
            for (int i = 0; i < 100; i++) {
                interactBabies();
                boundry();
            }
            askQuestion();
            break;

            case "3":
            ArrayList<Baby> teacherlessBabies = new ArrayList<>();
            for (Baby b : curBabbies) {
                if (!b.getTeacher()) {
                    teacherlessBabies.add(b);
                }
            }
            curBabbies = teacherlessBabies;
            System.out.println("done, no more teachers!");
            askQuestion();
            break;

            // case "3":
            // getAccuracy();
            // System.out.println("Rounds of interactions thusfar: " + interactions + "\n");
            // System.out.println("Accuracy of agents: " + accuracy + "%\n");
            // askQuestion();
            // break;

            case "4":
            for (int i = 0; i < accuracyList.size(); i++) {
                System.out.println("round " + (i+1) + ": Accuracy of agents: " + accuracyList.get(i) + "%\n");
                //System.out.println(accuracyList.get(i) + " ");
            }
            askQuestion();
            break;

            case "5":
            System.out.println();
            System.out.println("START");
            BabyWorld world = new BabyWorld();
            world.askInitials();
            break;
                
            default:
                invalidEntry();
                askQuestion();
        }
    }

    private void boundry() {
        System.out.println("..............................");
    }

    private void getAccuracy() {
        accuracy = 0;
        for (Baby b : curBabbies) {
            if (b.getCurGuess().equals(b.getLocation().getObject())) {
                accuracy++;
            }
        }
        accuracy = accuracy/curBabbies.size() * 100;
    }

    public ArrayList<Location> getCurLocations() {
        return curLocations;
    }

    void invalidEntry() {
        System.out.println("Invalid entry\n");
    }

}
