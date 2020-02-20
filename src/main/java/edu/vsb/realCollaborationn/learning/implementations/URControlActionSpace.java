package edu.vsb.realCollaborationn.learning.implementations;

import edu.vsb.realCollaborationn.learning.model.Action;
import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import org.deeplearning4j.rl4j.space.ActionSpace;

import java.util.ArrayList;
import java.util.List;

public class URControlActionSpace<A extends Action> implements ActionSpace<A> {
    private List<Action> actionsList = new ArrayList<Action>();

    public URControlActionSpace(UR3Model robotModel) {
        actionsList.add(new JointAMoveNegAction(robotModel));
        actionsList.add(new JointBMoveNegAction(robotModel));
        actionsList.add(new JointAMovePosAction(robotModel));
        actionsList.add(new JointBMovePosAction(robotModel));
    }

    @Override
    public A randomAction() {
        return (A) actionsList.get(1);
    }

    @Override
    public Object encode(A a) {
        a.performAction();
        return null;
    }

    @Override
    public int getSize() {
        return actionsList.size();
    }

    @Override
    public A noOp() {
        return (A) new NoOpAction();
    }
}
