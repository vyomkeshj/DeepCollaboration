package edu.vsb.realCollaborationn.learning.implementations;

import edu.vsb.realCollaborationn.learning.model.Action;
import edu.vsb.realCollaborationn.visualization.robot.UR3Model;

public class JointBMoveNegAction implements Action {
    UR3Model currentModel;

    public JointBMoveNegAction(UR3Model currentModel) {
        this.currentModel = currentModel;
    }

    @Override
    public void performAction() {

    }
}
