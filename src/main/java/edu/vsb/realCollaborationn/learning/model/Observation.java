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

    int tcpFactor = 100;

    Point3D currentTCPCoords = new Point3D(0,0,0);

    public Observation(UR3Model robotModel) {
        this.robotModel = robotModel;
    }

    public Observation(double jointAngleA, double jointAngleB, Point3D currentTCPCoords) {
        this.jointAngleA = jointAngleA;
        this.jointAngleB = jointAngleB;
        this.currentTCPCoords = currentTCPCoords;
    }

    public double getJointAngleA() {
        return robotModel.getA();
    }

    public double getJointAngleB() {
        return robotModel.getB();
    }

    public Point3D getCurrentTCPCoords() {
        return robotModel.getTCPcoords();
    }

    public void setCurrentTCPCoords(Point3D currentTCPCoords) {
        this.currentTCPCoords = currentTCPCoords;
    }

    public INDArray getData() {
        int[] stateArray = {(int) jointAngleA, (int) jointAngleB, (int) currentTCPCoords.getX()*tcpFactor, (int) currentTCPCoords.getY()*tcpFactor, (int) currentTCPCoords.getZ()*tcpFactor};
        return Nd4j.create(stateArray);
    }

    public double getReward(Point3D target) {
        return currentTCPCoords.distance(target);
    }

    @Override
    public double[] toArray() {
        return new double[] {jointAngleA, jointAngleB, currentTCPCoords.getX(), currentTCPCoords.getY(), currentTCPCoords.getZ()};
    }
}
