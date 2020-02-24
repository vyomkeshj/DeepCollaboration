package edu.vsb.realCollaborationn.learning;

import javafx.geometry.Point3D;

import java.util.Random;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Utils {
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
        Random r = new Random();
        return (max-min)*r.nextDouble();
    }

    public static Point3D getRandomPointsBetweenTwoConcentricSpheres(double radiusInner, double radiusOuter) {

        double r = getRandomDoubleInRange(radiusInner, radiusOuter);
        double theta = (3.14/180) * getRandomDoubleInRange(0, 90);
        double alpha = (3.14/180) * getRandomDoubleInRange(0, 90);
        Point3D nextTarget = polarToCartesian(r, theta, alpha);
        System.out.println("NEXT TARGET________________________________________________________________________________________");
        System.out.println("__________________________________________"+nextTarget+"______________________________________________");
        System.out.println("________________________________________________________________________________________");
        System.out.println("________________________________________________________________________________________");

        return nextTarget;
    }

    public static Point3D polarToCartesian(double r, double theta, double alpha) {
       double x = r * sin(theta) * cos(alpha);
       double y = r * sin(theta) * sin(alpha);
       double z = r * cos(theta);
       return new Point3D(x, y, z);
    }
}
