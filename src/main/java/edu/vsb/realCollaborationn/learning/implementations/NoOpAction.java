package edu.vsb.realCollaborationn.learning.implementations;

import edu.vsb.realCollaborationn.learning.model.Action;

public class NoOpAction implements Action {
    @Override
    public void performAction() {
        System.out.println("No operation in this step");
    }
}
