package edu.vsb.realCollaborationn.learning.implementations;

import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import javafx.geometry.Point3D;

public class URSimRobotState {

    UR3Model robotModel;
    Point3D targetPosition = new Point3D(0.1,.2,.3);

    public URSimRobotState(UR3Model robotModel) {
        this.robotModel = robotModel;
    }

    float getReward() {
        return (float) ((float) 1.0/(1.0-targetPosition.distance(robotModel.getTCPcoords())));
    }
}
