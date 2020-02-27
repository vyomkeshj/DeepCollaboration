package edu.vsb.realCollaborationn.learning;

import javafx.geometry.Point3D;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Utils {
   public static boolean MADE_IT_TO_TARGET = true;
   public static Point3D CURRENT_TARGET;

    public static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static double getRandomDoubleInRange(double min, double max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        double random = ThreadLocalRandom.current().nextDouble(min, max);
        return random;
    }

    public static Point3D getRandomPointsBetweenTwoConcentricSpheres(double radiusInner, double radiusOuter) {
        if(MADE_IT_TO_TARGET) {
            double r = getRandomDoubleInRange(radiusInner, radiusOuter);
            System.out.println("point distance:"+r);
            double theta = (3.14 / 180) * getRandomDoubleInRange(30, 70);
            double alpha = (3.14 / 180) * getRandomDoubleInRange(30, 70);
            Point3D nextTarget = polarToCartesian(r, theta, alpha);
            System.out.println("NEXT TARGET________________________________________________________________________________________");
            System.out.println("__________________________________________" + nextTarget + "______________________________________________");
            System.out.println("________________________________________________________________________________________");
            System.out.println("________________________________________________________________________________________");
            CURRENT_TARGET = nextTarget;
            MADE_IT_TO_TARGET = false;
            return nextTarget;
        }
        return CURRENT_TARGET;
    }

    public static Point3D polarToCartesian(double r, double theta, double alpha) {
       double x = r * sin(theta) * cos(alpha);
       double y = r * sin(theta) * sin(alpha);
       double z = r * cos(theta);
       return new Point3D(x, y, z);
    }
}
