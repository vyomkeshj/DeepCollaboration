package edu.vsb.realCollaborationn.learning.model;

import edu.vsb.realCollaborationn.learning.methods.definitions.RobotAngleUpdate;
import javafx.geometry.Point3D;
import org.deeplearning4j.gym.StepReply;

public interface ContinuousAction {
    double DISTANCE_THRESH = 0.10;
    int REWARD_SUCCESS = 200;

    StepReply<Observation> performAction(RobotAngleUpdate angleUpdate);
    void setTarget(Point3D target);
    Integer getEncoding();

}
