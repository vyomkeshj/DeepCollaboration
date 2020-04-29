package edu.vsb.realCollaborationn.learning.actions.pairwise;

import edu.vsb.realCollaborationn.learning.PointProvider;
import edu.vsb.realCollaborationn.learning.model.Action;
import edu.vsb.realCollaborationn.learning.model.Observation;
import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import javafx.geometry.Point3D;
import org.deeplearning4j.gym.StepReply;
import org.json.JSONObject;


public class IncrementAIncrementB implements Action {

    UR3Model currentModel;
    Point3D targetPoint;
    PointProvider provider;
    public IncrementAIncrementB(UR3Model currentModel, PointProvider targetPointProvider) {
        this.provider = targetPointProvider;
        this.currentModel = currentModel;
        this.targetPoint = provider.renewPointTarget();
    }

    @Override
    public StepReply<Observation> performAction() {

        currentModel.incrementA();
        currentModel.incrementB();
        Observation currentObservation = new Observation(currentModel, targetPoint);
        double reward = currentObservation.getReward(targetPoint);

        double distanceFromTarget = currentObservation.getDistanceFromTarget(targetPoint);

        boolean isDone = (distanceFromTarget< DISTANCE_THRESH);
        if(isDone) {
            System.out.println("__DONE__%"+"REWARD="+reward+"TIME="+System.currentTimeMillis());
            reward = reward+REWARD_SUCCESS;
            provider.setMadeItToTarget(true);
            currentModel.reset();
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
        return 2;
    }
}
