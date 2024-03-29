package edu.vsb.realCollaborationn.learning;

import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import javafx.geometry.Point3D;

import static edu.vsb.realCollaborationn.learning.Utils.getRandomDoubleInRange;

public class PointProvider {
    private boolean madeItToTarget = true;
    public static Point3D targetPoint;

    public Point3D renewPointTarget() {
        if(madeItToTarget) {
            UR3Model robotModel = new UR3Model();
            //set the random angles to the joints that are unconstrained
            robotModel.rotateAtJoint(2, getRandomDoubleInRange(0, 360));
            robotModel.rotateAtJoint(4, getRandomDoubleInRange(0, 90));
            robotModel.rotateAtJoint(6, getRandomDoubleInRange(0, 120));
            robotModel.rotateAtJoint(8, getRandomDoubleInRange(0, 120));

            targetPoint = robotModel.getTCPcoords();
            madeItToTarget=false;
            System.out.println("NEW_TARGET:"+ targetPoint);
        }
        return targetPoint;
    }

    public boolean hasMadeItToTarget() {
        return madeItToTarget;
    }

    public void setMadeItToTarget(boolean madeItToTarget) {
        this.madeItToTarget = madeItToTarget;
    }

    public static Point3D getTargetPoint() {
        return targetPoint;
    }

}
