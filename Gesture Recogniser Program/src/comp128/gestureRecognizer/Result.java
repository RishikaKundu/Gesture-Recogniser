package comp128.gestureRecognizer;

/**
 * This class takes in a template object and a score value. It stores the final score and template. 
 */
public class Result {

    Template template;
    double score;
    /**
     * Constructs a result object
     */

    public Result( Template template, double score){
        this.template= template;
        this.score= score;

    }

    public Template getTemplate(){
        return template;
    }

    public double getScore(){
        return score;
    }

    public void setTemplate(Template template){
        this.template= template;
    }

    public void setScore(Double score){
        this.score= score;
    }

    





    
}
