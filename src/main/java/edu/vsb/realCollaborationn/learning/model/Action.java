package edu.vsb.realCollaborationn.learning.model;

import org.deeplearning4j.gym.StepReply;

public interface Action {
    public static final double MAX_REWARD = 20;
    StepReply<Observation> performAction();
}
