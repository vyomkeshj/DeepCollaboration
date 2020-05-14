package edu.vsb.realCollaborationn.learning.model;

import edu.vsb.realCollaborationn.learning.PointProvider;
import edu.vsb.realCollaborationn.learning.actions.*;
import edu.vsb.realCollaborationn.learning.actions.continuous.ContinuousAction;
import edu.vsb.realCollaborationn.learning.actions.pairwise.DecrementADecrementB;
import edu.vsb.realCollaborationn.learning.actions.pairwise.DecrementBIncrementA;
import edu.vsb.realCollaborationn.learning.actions.pairwise.IncrementAIncrementB;
import edu.vsb.realCollaborationn.learning.actions.pairwise.IncrementBDecrementA;
import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import javafx.geometry.Point3D;
import org.deeplearning4j.gym.StepReply;

import java.util.ArrayList;
import java.util.List;

import static edu.vsb.realCollaborationn.learning.Utils.getRandomNumberInRange;

public class ActionSpace implements org.deeplearning4j.rl4j.space.ActionSpace<Action> {
    List<Action> actionsList = new ArrayList<>();
    NoOpAction noOpAction;
    JointAMoveNegAction jointAMoveNegAction;
    JointBMoveNegAction jointBMoveNegAction;
    JointCMoveNegAction jointCMoveNegAction;
    JointDMoveNegAction jointDMoveNegAction;
    JointEMoveNegAction jointEMoveNegAction;


    JointAMovePosAction jointAMovePosAction;
    JointBMovePosAction jointBMovePosAction;
    JointCMovePosAction jointCMovePosAction;
    JointDMovePosAction jointDMovePosAction;
    JointEMovePosAction jointEMovePosAction;

    IncrementAIncrementB incrementAIncrementB;
    IncrementBDecrementA incrementBDecrementA;
    DecrementADecrementB decrementADecrementB;
    DecrementBIncrementA decrementBIncrementA;

    ContinuousAction continuousAction;

    public ActionSpace(UR3Model robot, PointProvider provider) {

         continuousAction = new ContinuousAction(robot, provider);
         jointAMoveNegAction = new JointAMoveNegAction(robot, provider);
         jointBMoveNegAction = new JointBMoveNegAction(robot, provider);
/*
         jointCMoveNegAction = new JointCMoveNegAction(robot, provider);
         jointDMoveNegAction = new JointDMoveNegAction(robot, provider);
         jointEMoveNegAction = new JointEMoveNegAction(robot, provider);

*/

         jointAMovePosAction = new JointAMovePosAction(robot, provider);
         jointBMovePosAction = new JointBMovePosAction(robot, provider);
/*
         jointCMovePosAction = new JointCMovePosAction(robot, provider);
         jointDMovePosAction = new JointDMovePosAction(robot, provider);
         jointEMovePosAction = new JointEMovePosAction(robot, provider);

*/

        incrementAIncrementB = new IncrementAIncrementB(robot, provider);
        incrementBDecrementA = new IncrementBDecrementA(robot, provider);
        decrementBIncrementA = new DecrementBIncrementA(robot, provider);
        decrementADecrementB = new DecrementADecrementB(robot, provider);

        noOpAction = new NoOpAction(robot, provider);

        actionsList.add(jointAMoveNegAction);
        actionsList.add(jointAMovePosAction);
        actionsList.add(jointBMoveNegAction);
        actionsList.add(jointBMovePosAction);

        //actionsList.add(incrementAIncrementB);
        //actionsList.add(incrementBDecrementA);
        //actionsList.add(decrementADecrementB);
        //actionsList.add(decrementBIncrementA);

        //actionsList.add(jointCMoveNegAction);
        //actionsList.add(jointCMovePosAction);
        //actionsList.add(jointDMoveNegAction);
        //actionsList.add(jointDMovePosAction);
        //actionsList.add(jointEMoveNegAction);
        //actionsList.add(jointEMovePosAction);


        actionsList.add(noOpAction);
    }

    public void setTargetPoint(Point3D targetPoint) {
        jointAMoveNegAction.setTarget(targetPoint);
        jointBMoveNegAction.setTarget(targetPoint);
        incrementAIncrementB.setTarget(targetPoint);
        incrementBDecrementA.setTarget(targetPoint);
        decrementADecrementB.setTarget(targetPoint);
        decrementBIncrementA.setTarget(targetPoint);
        continuousAction.setTarget(targetPoint);
        //jointCMoveNegAction.setTarget(targetPoint);
        //jointDMoveNegAction.setTarget(targetPoint);
        //jointEMoveNegAction.setTarget(targetPoint);
        jointAMovePosAction.setTarget(targetPoint);
        jointBMovePosAction.setTarget(targetPoint);
        //jointCMovePosAction.setTarget(targetPoint);
        //jointDMovePosAction.setTarget(targetPoint);
        //jointEMovePosAction.setTarget(targetPoint);
        noOpAction.setTarget(targetPoint);
    }

    public StepReply<Observation> executeActionAt(Integer actionIndex) {
        return actionsList.get(actionIndex).performAction();
    }

    public StepReply<Observation> executeContinuousAction(double angleA, double angleB, double angleC, double angleD, double angleE) {
        return continuousAction.performAction(angleA, angleB, angleC, angleD, angleE);
    }


    @Override
    public Action randomAction() {
        return actionsList.get(getRandomNumberInRange(0,actionsList.size()-1));
    }

    @Override
    public Object encode(Action action) {
        return action.getEncoding();
    }

    @Override
    public int getSize() {
        return actionsList.size();
    }

    @Override
    public Action noOp() {
        return noOpAction;
    }


}
