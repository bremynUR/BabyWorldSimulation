
public class Feedback {

    String recommendation;
    double weight;
    String source;
    Boolean recent;

    public Feedback(String recommendation, double weight, String source) {
        this.recommendation = recommendation;
        this.weight = weight;
        this.source = source;
        this.recent = true;
    }
    
    public Boolean isRecent() {
        return recent;
    }

    public void makeOld() {
        this.recent = false;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public double getWeight() {
        return weight;
    }

}
