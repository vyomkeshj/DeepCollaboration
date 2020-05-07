package edu.vsb.realCollaborationn.learning.methods.definitions;

import edu.vsb.realCollaborationn.learning.Utils;
import org.deeplearning4j.rl4j.space.ActionSpace;
import org.nd4j.linalg.api.rng.Random;
import org.nd4j.linalg.factory.Nd4j;

public class ContinuousSpace implements ActionSpace<RobotAngleUpdate> {
    protected final int size;

    public ContinuousSpace(int size) {
        this.size = (size);
    }


    public RobotAngleUpdate randomAction() {
        return new RobotAngleUpdate(Utils.getRandomDoubleInRange(0, Utils.PI), Utils.getRandomDoubleInRange(0, Utils.PI));
    }

    @Override
    public Object encode(RobotAngleUpdate robotAngleUpdate) {
        return new double[] {robotAngleUpdate.getTheta1(), robotAngleUpdate.getTheta2()};
    }

    public RobotAngleUpdate noOp() {
        return new RobotAngleUpdate(0, 0);
    }

    public int getSize() {
        return this.size;
    }


}
