package edu.vsb.realCollaborationn.learning.model;

import edu.vsb.realCollaborationn.learning.actions.*;
import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import javafx.geometry.Point3D;
import org.deeplearning4j.gym.StepReply;

import java.util.ArrayList;
import java.util.List;

import static edu.vsb.realCollaborationn.learning.Utils.getRandomNumberInRange;

public class ActionSpace implements org.deeplearning4j.rl4j.space.ActionSpace<Action> {
    List<Action> actionsList = new ArrayList<>();
    Point3D targetPoint = new Point3D(0, 0, 0);
    NoOpAction noOpAction;
    public ActionSpace(UR3Model robot) {
        JointAMoveNegAction jointAMoveNegAction = new JointAMoveNegAction(robot, targetPoint);
        JointBMoveNegAction jointBMoveNegAction = new JointBMoveNegAction(robot, targetPoint);
        JointAMovePosAction jointAMovePosAction = new JointAMovePosAction(robot, targetPoint);
        JointBMovePosAction jointBMovePosAction = new JointBMovePosAction(robot, targetPoint);
        noOpAction = new NoOpAction(robot, targetPoint);

        actionsList.add(jointAMoveNegAction);
        actionsList.add(jointAMovePosAction);
        actionsList.add(jointBMoveNegAction);
        actionsList.add(jointBMovePosAction);
        actionsList.add(noOpAction);
    }

    public void setTargetPoint(Point3D targetPoint) {
        this.targetPoint = targetPoint;
    }

    public StepReply<Observation> executeActionAt(Integer actionIndex) {
        return actionsList.get(actionIndex).performAction();
    }

    @Override
    public Action randomAction() {
        return actionsList.get(getRandomNumberInRange(0,4));
    }

    @Override
    public Object encode(Action action) {
        return null;
    }

    @Override
    public int getSize() {
        return 5;
    }

    @Override
    public Action noOp() {
        return noOpAction;
    }
}
