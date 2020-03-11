package edu.vsb.realCollaborationn.learning.model;

import javafx.geometry.Point3D;
import org.deeplearning4j.gym.StepReply;

public interface Action {
    double MAX_REWARD = 0.02;

    StepReply<Observation> performAction();
    void setTarget(Point3D target);
    Integer getEncoding();
}
