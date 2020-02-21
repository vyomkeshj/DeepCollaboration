package edu.vsb.realCollaborationn.learning.implementations;

import com.sun.webkit.network.Util;
import edu.vsb.realCollaborationn.learning.Utils;
import edu.vsb.realCollaborationn.learning.model.Action;
import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import org.deeplearning4j.rl4j.space.ActionSpace;
import org.deeplearning4j.rl4j.space.DiscreteSpace;

import java.util.ArrayList;
import java.util.List;

public class URControlActionSpace<A extends Action> extends DiscreteSpace {
    private List<Action> actionsList = new ArrayList<Action>();

    public URControlActionSpace(UR3Model robotModel) {
        super(4);
        actionsList.add(new JointAMoveNegAction(robotModel));
        actionsList.add(new JointBMoveNegAction(robotModel));
        actionsList.add(new JointAMovePosAction(robotModel));
        actionsList.add(new JointBMovePosAction(robotModel));

    }

    @Override
    public Object encode(Integer a) {
        actionsList.get(a).performAction();
        return null;
    }

    @Override
    public int getSize() {
        return actionsList.size();
    }

    @Override
    public Integer noOp() {
        return -1;
    }
}
