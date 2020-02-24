package edu.vsb.realCollaborationn.learning.model;

import org.deeplearning4j.gym.StepReply;

public interface Action {
    public static final float MAX_REWARD = 5;
    StepReply<Observation> performAction();
}
