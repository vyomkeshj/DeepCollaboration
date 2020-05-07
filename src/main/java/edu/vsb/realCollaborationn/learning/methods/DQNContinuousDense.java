package edu.vsb.realCollaborationn.learning.methods;

import edu.vsb.realCollaborationn.learning.methods.definitions.ContinuousSpace;
import edu.vsb.realCollaborationn.learning.methods.definitions.QLearningContinuous;
import edu.vsb.realCollaborationn.learning.methods.definitions.RobotAngleUpdate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.learning.Learning;
import org.deeplearning4j.rl4j.learning.sync.Transition;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.TDTargetAlgorithm.*;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.network.dqn.DQNFactory;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.deeplearning4j.rl4j.observation.Observation;
import org.deeplearning4j.rl4j.policy.DQNPolicy;
import org.deeplearning4j.rl4j.policy.EpsGreedy;
import org.deeplearning4j.rl4j.policy.IPolicy;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.Encodable;
import org.deeplearning4j.rl4j.util.DataManagerTrainingListener;
import org.deeplearning4j.rl4j.util.IDataManager;
import org.deeplearning4j.rl4j.util.LegacyMDPWrapper;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.rng.Random;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.factory.Nd4j;

import java.util.ArrayList;


/**
 * @author rubenfiszel (ruben.fiszel@epfl.ch) 7/18/16.
 *
 * DQN or Deep Q-Learning in the Discrete domain
 *
 * https://arxiv.org/abs/1312.5602
 *
 */
public class DQNContinuousDense<O extends Encodable> extends QLearningContinuous<O> {


    @Deprecated
    public DQNContinuousDense(MDP<O, RobotAngleUpdate, ContinuousSpace> mdp, IDQN dqn, QLearning.QLConfiguration conf,
                                  IDataManager dataManager) {
        this(mdp, dqn, conf);
        addListener(new DataManagerTrainingListener(dataManager));
    }
    public DQNContinuousDense(MDP<O, RobotAngleUpdate, ContinuousSpace> mdp, IDQN dqn, QLearning.QLConfiguration conf) {
        super(mdp, dqn, conf, conf.getEpsilonNbStep());
    }

    @Deprecated
    public DQNContinuousDense(MDP<O, RobotAngleUpdate, ContinuousSpace> mdp, DQNFactory factory,
                                  QLearning.QLConfiguration conf, IDataManager dataManager) {
        this(mdp, factory.buildDQN(mdp.getObservationSpace().getShape(), mdp.getActionSpace().getSize()), conf,
                dataManager);
    }
    public DQNContinuousDense(MDP<O, RobotAngleUpdate, ContinuousSpace> mdp, DQNFactory factory,
                                  QLearning.QLConfiguration conf) {
        this(mdp, factory.buildDQN(mdp.getObservationSpace().getShape(), mdp.getActionSpace().getSize()), conf);
    }

    @Deprecated
    public DQNContinuousDense(MDP<O, RobotAngleUpdate, ContinuousSpace> mdp, DQNFactoryStdDense.Configuration netConf,
                                  QLearning.QLConfiguration conf, IDataManager dataManager) {
        this(mdp, new DQNFactoryStdDense(netConf), conf, dataManager);
    }
    public DQNContinuousDense(MDP<O, RobotAngleUpdate, ContinuousSpace> mdp, DQNFactoryStdDense.Configuration netConf,
                                  QLearning.QLConfiguration conf) {
        this(mdp, new DQNFactoryStdDense(netConf), conf);
    }

}