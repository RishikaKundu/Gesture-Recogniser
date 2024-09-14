package comp128.gestureRecognizer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;

import edu.macalester.graphics.Point;

/**
 * Recognizer to recognize 2D gestures. Uses the $1 gesture recognition algorithm.
 */
public class Recognizer {

    ArrayList<Template> templates;
    Template template;
    double size;

    /**
     * Constructs a recognizer object
     */
    public Recognizer(){
        templates= new ArrayList<>();
        template= new Template();
        size= 250;

    }

    /**
     * Create a template to use for matching
     * @param name of the template
     * @param points in the template gesture's path
     */
    public void addTemplate(String name, Deque<Point> points){
        Template template1= new Template();
        points= resample(points, 64);
        points= rotateBy(points, -indicativeAngle(points));
        points= scaleTo(points, size);
        points= translateTo(points, new Point(0, 0));
        template1.setPoints(points);
        template1.setName(name);
        templates.add(template1);
    
            
    }

    /**
     * Uses a golden section search to calculate rotation that minimizes the distance between the gesture and the template points.
     * @param points
     * @param templatePoints
     * @return best distance
     */
    private double distanceAtBestAngle(Deque<Point> points, Deque<Point> templatePoints){
        double thetaA = -Math.toRadians(45);
        double thetaB = Math.toRadians(45);
        final double deltaTheta = Math.toRadians(2);
        double phi = 0.5*(-1.0 + Math.sqrt(5.0));// golden ratio
        double x1 = phi*thetaA + (1-phi)*thetaB;
        double f1 = distanceAtAngle(points, templatePoints, x1);
        double x2 = (1 - phi)*thetaA + phi*thetaB;
        double f2 = distanceAtAngle(points, templatePoints, x2);
        while(Math.abs(thetaB-thetaA) > deltaTheta){
            if (f1 < f2){
                thetaB = x2;
                x2 = x1;
                f2 = f1;
                x1 = phi*thetaA + (1-phi)*thetaB;
                f1 = distanceAtAngle(points, templatePoints, x1);
            }
            else{
                thetaA = x1;
                x1 = x2;
                f1 = f2;
                x2 = (1-phi)*thetaA + phi*thetaB;
                f2 = distanceAtAngle(points, templatePoints, x2);
            }
        }
        return Math.min(f1, f2);
    }

    /**
     * Rotates the points and returns the pathDistance
     * @param points
     * @param templatePoints
     * @param theta
     * @return
     */

    private double distanceAtAngle(Deque<Point> points, Deque<Point> templatePoints, double theta){
        Deque<Point> rotatedPoints = null;
        rotatedPoints = rotateBy(points, theta);
        return pathDistance(rotatedPoints, templatePoints);
    }

    /**
     * Calculates the total path length of the original path by adding up the distances between successive points
     * @param points
     */

    public double pathLength(Deque<Point> points){
        Iterator<Point> itr= points.iterator();
        Point tempPoint= itr.next();
        double path=0;
        Point currentPoint;
        while (itr.hasNext()){
            currentPoint= itr.next();
            path += tempPoint.distance(currentPoint);
            tempPoint= currentPoint;
        }
        return path;

    }

    /**
     * Resamples the total path length into n points.
     * @param points
     * @param n
     */

    public Deque<Point> resample(Deque<Point> points, int n){
        Deque<Point> resampledPoints= new ArrayDeque<>();
        double resampleInterval= pathLength(points)/(n-1);
        resampledPoints.add(points.peekFirst());
        Iterator<Point> itr= points.iterator();
        Point prevPoint= itr.next();
        Point currentPoint, resampledPoint;
        double accumDist=0;
        currentPoint= itr.next();
        while (itr.hasNext()){
            double segmentDist = prevPoint.distance(currentPoint);
            if (accumDist+segmentDist >= resampleInterval){
                resampledPoint= Point.interpolate(prevPoint, currentPoint, (resampleInterval - accumDist)/ segmentDist);
                resampledPoints.add(resampledPoint);
                prevPoint= resampledPoint;
                accumDist= 0;
            } else{
                accumDist += segmentDist;
                prevPoint= currentPoint;
                currentPoint= itr.next();
            }
        }
        if (!(itr.hasNext()) && resampledPoints.size()== n-1){
                resampledPoints.add(points.peekLast());
        }
        return resampledPoints;
    }

    /**
     * Returns the path distance between points in two deques. 
     */

    public double pathDistance(Deque<Point> a, Deque<Point> b){
        Iterator<Point> itra= a.iterator();
        Iterator<Point> itrb= b.iterator();
        double dist=0;
        while (itra.hasNext()){
            dist += itra.next().distance(itrb.next());

        }
        double meanDist= dist/a.size();
        return meanDist;
    }

    /**
     * Calculates the indicitive angle. This is the angle that the gesture makes with the x axis.
     */

    public double indicativeAngle(Deque<Point> points){
        Point centroid= calculateCentroid(points);
        Point difference= centroid.subtract(points.peekFirst());
        double indicitiveAngle= difference.angle();
        return indicitiveAngle;
    }

    /**
     * Rotates each point in the gesture around the centroid by the negative indicative angle.
     * @param points
     * @param angle
     */

    public Deque<Point> rotateBy(Deque<Point> points, double angle){
        Deque<Point> rotatedPoints= new ArrayDeque<>();
        Point centroid= calculateCentroid(points);
        for (Point point: points){
            rotatedPoints.add(point.rotate(angle, centroid));
        }
        return rotatedPoints;
    }

    /**
     * Calculates the centroid of the guesture.
     */

    public Point calculateCentroid(Deque<Point> points){
        double avgX=0, avgY=0;
        for (Point point: points){
            avgX+= point.getX();
            avgY+= point.getY();
        }
        double centroidX= avgX/points.size();
        double centroidY= avgY/points.size();

        return new Point(centroidX, centroidY);

    }

    /**
     * Scales  each point in the gesture to the size.
     * @param points
     * @param scale
     */

    public Deque<Point> scaleTo(Deque<Point> points, double scale){
        Deque<Point> scaledPoints= new ArrayDeque<>();
        Point initialPoint= new Point(points.peek().getX(), points.peek().getY());
        double maxX= initialPoint.getX();
        double minX= initialPoint.getX();
        double maxY= initialPoint.getY();
        double minY= initialPoint.getY();
        for (Point point: points){
            maxX = Math.max(maxX, point.getX());
            minX= Math.min(minX, point.getX());
            maxY= Math.max(maxY, point.getY());
            minY= Math.min(minY, point.getY());
        }
        double height= maxY-minY;
        double width= maxX-minX;

        for (Point point: points){
            scaledPoints.add(point.scale(scale/width, scale/height));
        }
        return scaledPoints;


    }

    /**
     * Translates all points in the gesture to the input point.
     * @param points
     * @param inputPoint
     */

    public Deque<Point> translateTo(Deque<Point> points, Point inputPoint){
        Deque<Point> translatedPoints= new ArrayDeque<>();
        Point centroid= calculateCentroid(points);
        for (Point point: points){
            translatedPoints.add(point.add(inputPoint).subtract(centroid));
        }
        return translatedPoints;
    }

    /**
     * Checks which template is the most accurate to the drawn gesture. 
     * Calculates the score of the template.
     * Resturns a result object.
     * @param points
     */

    public Result recognize(Deque<Point> points){

        points= resample(points, 64);
        points= rotateBy(points, -indicativeAngle(points));
        points= scaleTo(points, size);
        points= translateTo(points, new Point(0, 0));
        double minDistance= Double.MAX_VALUE;
        Template minTemplate= new Template();

        for (Template template: templates){
            double distance= distanceAtBestAngle(points, template.getPoints());
            if (distance < minDistance){
                minDistance= distance;
                minTemplate= template;
                System.out.println("MinDist:");
                System.out.println(minDistance);
            }

        }
        double score= 1 - (minDistance/(Math.sqrt(0.5) * size));
        return new Result(minTemplate, score);

    }

    /**
     * Checks whether the template object is empty. 
     */

    public boolean isEmpty(){
        if (templates.isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }
}