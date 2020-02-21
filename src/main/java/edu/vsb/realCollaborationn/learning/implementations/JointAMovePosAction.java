package edu.vsb.realCollaborationn.learning.implementations;

import edu.vsb.realCollaborationn.learning.model.Action;
import edu.vsb.realCollaborationn.visualization.robot.UR3Model;

public class JointAMovePosAction implements Action {

    UR3Model currentModel;
    public JointAMovePosAction(UR3Model robot) {
        this.currentModel = robot;
    }

    @Override
    public void performAction() {
        currentModel.incrementA();
    }
}
