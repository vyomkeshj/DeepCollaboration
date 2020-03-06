package edu.vsb.realCollaborationn.learning;

import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
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

    public static Point3D getTargetOnConstrainedRobot() {
        if(MADE_IT_TO_TARGET) {
            UR3Model robotModel = new UR3Model();
            //set the random angles to the joints that are unconstrained
            robotModel.rotateAtJoint(2, getRandomDoubleInRange(0, 360));
            robotModel.rotateAtJoint(4, getRandomDoubleInRange(0, 180));
            robotModel.rotateAtJoint(6, getRandomDoubleInRange(-160, 160));
            robotModel.rotateAtJoint(8, getRandomDoubleInRange(-160, 160));
            robotModel.rotateAtJoint(10, getRandomDoubleInRange(-160, 160));


            CURRENT_TARGET = robotModel.getTCPcoords();
            MADE_IT_TO_TARGET=false;
        }
        System.out.println("NEW TARGET:"+CURRENT_TARGET);
        //return new Point3D(0.159588, 0.516524, -0.396608);
        return CURRENT_TARGET;
    }

    public static Point3D polarToCartesian(double r, double theta, double alpha) {
       double x = r * sin(theta) * cos(alpha);
       double y = r * sin(theta) * sin(alpha);
       double z = r * cos(theta);
       return new Point3D(x, y, z);
    }
}
