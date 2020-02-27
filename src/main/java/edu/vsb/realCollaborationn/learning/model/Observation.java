package edu.vsb.realCollaborationn.learning.model;

import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import javafx.geometry.Point3D;
import org.deeplearning4j.rl4j.space.Encodable;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class Observation implements Encodable {
    UR3Model robotModel;
    double jointAngleA = 0.00;
    double jointAngleB = 0.00;
    double jointAngleC = 0.00;
    double jointAngleD = 0.00;
    double jointAngleE = 0.00;
    int tcpFactor = 100;
    private static double previousReward = 0;
    Point3D currentTCPCoords = new Point3D(0,0,0);
    Point3D targetTCPCoords = new Point3D(0,0,0);

    public Observation(UR3Model robotModel) {
        this.robotModel = robotModel;
        currentTCPCoords = robotModel.getTCPcoords();
    }

    public Observation(UR3Model robotModel, Point3D targetTCPCoords) {
        this.robotModel = robotModel;
        currentTCPCoords = robotModel.getTCPcoords();
        this.targetTCPCoords = targetTCPCoords;
    }

    public double getJointAngleA() {
        return robotModel.getA();
    }

    public double getJointAngleB() {
        return robotModel.getB();
    }

    public double getJointAngleC() {
        return jointAngleC;
    }

    public double getJointAngleD() {
        return jointAngleD;
    }

    public double getJointAngleE() {
        return jointAngleE;
    }

    public Point3D getCurrentTCPCoords() {
        return robotModel.getTCPcoords();
    }

    public void setCurrentTCPCoords(Point3D currentTCPCoords) {
        this.currentTCPCoords = currentTCPCoords;
    }

    public INDArray getData() {
        double[] stateArray = {jointAngleA, jointAngleB,jointAngleC, jointAngleD, jointAngleE, currentTCPCoords.getX() ,currentTCPCoords.getY(), currentTCPCoords.getZ()};
        return Nd4j.create(stateArray);
    }

    public double getDistanceFromTarget(Point3D target) {
        return currentTCPCoords.distance(target);
    }

    public double getReward(Point3D target) {

        if(currentTCPCoords.getY()<=0) {
            return -2;
        }
        double stepReward = 1.000d/(1.000d+currentTCPCoords.distance(target));

        if(previousReward==0) {
            previousReward = stepReward;
            return stepReward;
        }
        double lastReward = previousReward;
        previousReward = stepReward;

        stepReward = stepReward - lastReward;
        stepReward = stepReward*currentTCPCoords.distance(target);
        return stepReward;
    }

    @Override
    public double[] toArray() {
        return new double[] {Math.sin(jointAngleA), Math.sin(jointAngleB),Math.sin(jointAngleC), Math.sin(jointAngleD),
                Math.sin(jointAngleE), currentTCPCoords.getX(), currentTCPCoords.getY(), currentTCPCoords.getZ(),
                targetTCPCoords.getX(), targetTCPCoords.getY(), targetTCPCoords.getZ(), currentTCPCoords.distance(targetTCPCoords)};

    }
}
