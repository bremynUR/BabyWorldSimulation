
public class Feedback {

    String recomendation;
    double weight;
    String source;
    Boolean recent;

    public Feedback(String recomendation, double weight, String source) {
        this.recomendation = recomendation;
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

    public String getRecomendation() {
        return recomendation;
    }

    public void setRecomendation(String recomendation) {
        this.recomendation = recomendation;
    }
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }

}
