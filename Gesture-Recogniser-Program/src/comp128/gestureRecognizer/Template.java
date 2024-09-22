package comp128.gestureRecognizer;

import java.util.ArrayDeque;
import java.util.Deque;

import edu.macalester.graphics.Point;

/**
 * This class stores the properties of each tempelate. 
 */
public class Template {
    Deque<Point> newPoints;
    String name;

    /**
     * Constructs a tempelate object
     */

    public Template(){
        newPoints= new ArrayDeque<>();
        
    }

    /**
     * Sets the points of the template to the deque.
     * @param points
     */

    public void setPoints(Deque<Point> points){
        newPoints= points;
    }

    public Deque<Point> getPoints(){
        return newPoints;

    }

    public void setName(String name){
        this.name= name;
    }

    public String getName(){
        return name;

    }



    
}
