package edu.vsb.realCollaborationn.learning.model;

import javafx.geometry.Point3D;
import org.deeplearning4j.gym.StepReply;

public interface Action {
    StepReply<Observation> performAction();
}
