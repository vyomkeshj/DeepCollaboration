package edu.vsb.realCollaborationn.learning.model;

import javafx.geometry.Point3D;
import org.deeplearning4j.gym.StepReply;

public interface Action {
    double DISTANCE_THRESH = 0.10;
    int REWARD_SUCCESS = 200;

    StepReply<Observation> performAction();
    void setTarget(Point3D target);
    Integer getEncoding();
}
