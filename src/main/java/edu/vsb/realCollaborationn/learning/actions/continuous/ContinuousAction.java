package edu.vsb.realCollaborationn.learning.actions.continuous;

import edu.vsb.realCollaborationn.learning.PointProvider;
import edu.vsb.realCollaborationn.learning.model.Action;
import edu.vsb.realCollaborationn.learning.model.Observation;
import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import javafx.geometry.Point3D;
import org.deeplearning4j.gym.StepReply;
import org.json.JSONObject;


public class ContinuousAction implements Action {
    UR3Model currentModel;
    Point3D targetPoint;
    PointProvider provider;

    public ContinuousAction(UR3Model currentModel, PointProvider targetPointProvider) {
        this.provider = targetPointProvider;
        this.currentModel = currentModel;
        this.targetPoint = provider.renewPointTarget();
    }

    public StepReply<Observation> performAction(double angleA, double angleB, double angleC, double angleD, double angleE) {
        currentModel.updateJointAnglesBy(angleA, angleB, angleC, angleD, angleE);
        return performAction();
    }

        @Override
    public StepReply<Observation> performAction() {
        targetPoint = provider.renewPointTarget();
        Observation currentObservation = new Observation(currentModel, targetPoint);
        double reward = currentObservation.getReward(provider.renewPointTarget());

        double distanceFromTarget = currentObservation.getDistanceFromTarget(targetPoint);

        boolean isDone = (distanceFromTarget< DISTANCE_THRESH);
        if(isDone) {
            reward = reward+REWARD_SUCCESS;
            System.out.println("DONE__"+"REWARD__"+reward+"__TIME="+System.currentTimeMillis());

            provider.setMadeItToTarget(true);
            //currentModel.reset(); fixme: returning zeros
        }
        StepReply<Observation> reply = new StepReply<Observation>(currentObservation, reward, isDone, new JSONObject(currentObservation));
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
