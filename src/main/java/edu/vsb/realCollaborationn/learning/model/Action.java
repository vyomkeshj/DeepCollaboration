package edu.vsb.realCollaborationn.learning.model;

import javafx.geometry.Point3D;
import org.deeplearning4j.gym.StepReply;

public interface Action {
    static int DONE_COUNTER = 0;
    double DISTANCE_THRESH = 0.05;
    int REWARD_SUCCESS = 20;

    StepReply<Observation> performAction();
    void setTarget(Point3D target);
    Integer getEncoding();
}
