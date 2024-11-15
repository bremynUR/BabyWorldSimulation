import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class NameGen {

    Random rand = new Random();

    String generateName(Integer size) {
        String name = "";
        Boolean vowel = true;
        for (int i = 0; i < size; i++) {
            Integer chance = rand.nextInt(101);
            if(vowel) {
                if (chance > 20) {
                    vowel = false;
                    if (chance > 75) {
                        if (chance > 98) {
                            name = name + generateRareConsonant();
                        } else {
                            name = name + generateUncommonConsonant();
                        }
                    } else {
                        name = name + generateCommonConsonant();
                    }
                } else {
                    if (chance > 3) {
                        name = name + generateCommonVowel();
                    } else {
                        name = name + generateUncommonVowel();
                    }
                }

            } else {
                if (chance > 30) {
                    vowel = true;
                    if (chance > 90) {
                        name = name + generateUncommonVowel();
                    } else {
                        name = name + generateCommonVowel();
                    }
                } else {
                    if (chance > 12) {
                        name = name + generateCommonConsonant();
                    } else {
                        if (chance > 1) {
                            name = name + generateUncommonConsonant();
                        } else {
                            name = name + generateRareConsonant();
                        }
                    }
                }
            }
        }
        return name;
    }

    String generateCommonVowel() {
        int randInt = rand.nextInt(4);
        ArrayList<String> letters = new ArrayList<>();
        Collections.addAll(letters, "a", "e", "i", "o");
        String vowel = intToLetter(letters, randInt);
        return vowel;
    }
    
    String generateUncommonVowel() {
        int randInt = rand.nextInt(2);
        ArrayList<String> letters = new ArrayList<>();
        Collections.addAll(letters, "u", "y");
        String vowel = intToLetter(letters, randInt);
        return vowel;
    }

    String generateCommonConsonant() {
        int randInt = rand.nextInt(7);
        ArrayList<String> letters = new ArrayList<>();
        Collections.addAll(letters, "t", "n", "s", "h", "r", "d", "l");
        String cons = intToLetter(letters, randInt);
        return cons;
    }

    String generateUncommonConsonant() {
        int randInt = rand.nextInt(7);
        ArrayList<String> letters = new ArrayList<>();
        Collections.addAll(letters, "c", "w", "m", "f", "g", "p", "b");
        String cons = intToLetter(letters, randInt);
        return cons;
    }

    String generateRareConsonant() {
        int randInt = rand.nextInt(6);
        ArrayList<String> letters = new ArrayList<>();
        Collections.addAll(letters, "v", "k", "x", "j", "q", "z");
        String cons = intToLetter(letters, randInt);
        return cons;
    }

    String intToLetter(ArrayList<String> letters, Integer num) {
        String letter = "";
        for(int i = 0; i <= num; i++) {
            letter = letters.get(i);
        }
        return letter;
    }
}
