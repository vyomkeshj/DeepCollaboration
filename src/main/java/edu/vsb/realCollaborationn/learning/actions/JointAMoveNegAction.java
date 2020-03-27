package edu.vsb.realCollaborationn.learning.actions;

import edu.vsb.realCollaborationn.learning.PointProvider;
import edu.vsb.realCollaborationn.learning.Utils;
import edu.vsb.realCollaborationn.learning.model.Action;
import edu.vsb.realCollaborationn.learning.model.Observation;
import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import javafx.geometry.Point3D;
import org.deeplearning4j.gym.StepReply;
import org.json.JSONObject;

import static edu.vsb.realCollaborationn.learning.Utils.MADE_IT_TO_TARGET;


public class JointAMoveNegAction implements Action {
    UR3Model currentModel;
    Point3D targetPoint;
    PointProvider provider;

    public JointAMoveNegAction(UR3Model currentModel, PointProvider targetPointProvider) {
        this.provider = targetPointProvider;
        this.currentModel = currentModel;
        this.targetPoint = provider.renewPointTarget();
    }

    @Override
    public StepReply<Observation> performAction() {
        //System.out.println("Joint A-");
        if(provider.hasMadeItToTarget())
            targetPoint = provider.renewPointTarget();

        currentModel.decrementA();
        Observation currentObservation = new Observation(currentModel, targetPoint);
        double reward = currentObservation.getReward(targetPoint);

        double distanceFromTarget = currentObservation.getDistanceFromTarget(targetPoint);

        boolean isDone = (distanceFromTarget< MAX_REWARD);
        if(isDone) {
            reward = reward+100;
            System.out.println("___________DONE____________"+"__FromThread___"+Thread.currentThread().getName()+"TIME="+System.currentTimeMillis());

            provider.setMadeItToTarget(true);
            currentModel.reset();
        }
        StepReply<Observation> reply = new StepReply<Observation>(currentObservation, reward, false, new JSONObject(currentObservation));
        return reply;
    }

    @Override
    public void setTarget(Point3D target) {
        this.targetPoint = target;
    }

    @Override
    public Integer getEncoding() {
        return 1;
    }
}
