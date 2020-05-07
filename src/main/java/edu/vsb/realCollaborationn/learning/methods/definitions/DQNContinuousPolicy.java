package edu.vsb.realCollaborationn.learning.methods.definitions;

import org.deeplearning4j.rl4j.network.dqn.DQN;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.deeplearning4j.rl4j.policy.DQNPolicy;
import org.deeplearning4j.rl4j.policy.Policy;
import org.deeplearning4j.rl4j.space.Encodable;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.io.IOException;

public class DQNContinuousPolicy<O extends Encodable> extends Policy<O, RobotAngleUpdate> {

    final private IDQN dqn;

    public DQNContinuousPolicy(IDQN dqn) {
        this.dqn = dqn;
    }

    public static <O extends Encodable> DQNPolicy<O> load(String path) throws IOException {
        return new DQNPolicy<O>(DQN.load(path));
    }

    public IDQN getNeuralNet() {
        return dqn;
    }

    public RobotAngleUpdate nextAction(INDArray input) {
        System.out.println("Action is being taken");
        INDArray output = dqn.output(input);
        return new RobotAngleUpdate(output.getDouble(1), output.getDouble(2));      //fixme: check if it works
    }

    public void save(String filename) throws IOException {
        dqn.save(filename);
    }

}
