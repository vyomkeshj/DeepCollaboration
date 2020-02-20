package edu.vsb.realCollaborationn.learning.implementations;

import edu.vsb.realCollaborationn.learning.model.Action;
import edu.vsb.realCollaborationn.visualization.robot.UR3Model;

public class JointBMovePosAction implements Action {
    UR3Model currentModel;

    public JointBMovePosAction(UR3Model currentModel) {
        this.currentModel = currentModel;
    }

    @Override
    public void performAction() {
    }

}
