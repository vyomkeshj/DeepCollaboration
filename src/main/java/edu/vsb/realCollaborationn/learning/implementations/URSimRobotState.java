package edu.vsb.realCollaborationn.learning.implementations;

import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import javafx.geometry.Point3D;
import org.deeplearning4j.rl4j.space.Encodable;

public class URSimRobotState implements Encodable {

    UR3Model robotModel;
    Point3D targetPosition = new Point3D(0.1,.2,.3);

    public URSimRobotState(UR3Model robotModel) {
        this.robotModel = robotModel;
    }

    float getReward() {
        return 0.1f;
    }

    @Override
    public double[] toArray() {
        return new double[] {getReward(), targetPosition.getX()};
    }
}
