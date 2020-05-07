package edu.vsb.realCollaborationn.learning.methods.definitions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.learning.Learning;
import org.deeplearning4j.rl4j.learning.sync.Transition;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.TDTargetAlgorithm.DoubleDQN;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.TDTargetAlgorithm.ITDTargetAlgorithm;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.TDTargetAlgorithm.StandardDQN;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.deeplearning4j.rl4j.observation.Observation;
import org.deeplearning4j.rl4j.policy.EpsGreedy;
import org.deeplearning4j.rl4j.space.Encodable;
import org.deeplearning4j.rl4j.util.LegacyMDPWrapper;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.rng.Random;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.factory.Nd4j;

import java.util.ArrayList;


/**
 * @author vyomkesh
 */
public abstract class QLearningContinuous<O extends Encodable> extends QLearning<O, RobotAngleUpdate, ContinuousSpace> {

    @Getter
    final private QLConfiguration configuration;
    private final LegacyMDPWrapper<O, RobotAngleUpdate, ContinuousSpace> mdp;
    @Getter
    private DQNContinuousPolicy<O> policy;
    @Getter
    private EpsGreedy<O, RobotAngleUpdate, ContinuousSpace> egPolicy;

    @Getter
    final private IDQN qNetwork;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private IDQN targetQNetwork;

    private RobotAngleUpdate lastAction;
    private double accuReward = 0;

    ITDTargetAlgorithm tdTargetAlgorithm;

    protected LegacyMDPWrapper<O, RobotAngleUpdate, ContinuousSpace> getLegacyMDPWrapper() {
        return mdp;
    }

    public QLearningContinuous(MDP<O, RobotAngleUpdate, ContinuousSpace> mdp, IDQN dqn, QLConfiguration conf,
                               int epsilonNbStep) {
        this(mdp, dqn, conf, epsilonNbStep, Nd4j.getRandomFactory().getNewRandomInstance(conf.getSeed()));
    }

    public QLearningContinuous(MDP<O, RobotAngleUpdate, ContinuousSpace> mdp, IDQN dqn, QLConfiguration conf,
                               int epsilonNbStep, Random random) {
        super(conf);
        this.configuration = conf;
        this.mdp = new LegacyMDPWrapper<O, RobotAngleUpdate, ContinuousSpace>(mdp, this);
        qNetwork = dqn;
        targetQNetwork = dqn.clone();
        policy = new DQNContinuousPolicy<>(getQNetwork());
        egPolicy = new EpsGreedy(policy, mdp, conf.getUpdateStart(), epsilonNbStep, random, conf.getMinEpsilon(),
                this);

        tdTargetAlgorithm = conf.isDoubleDQN()
                ? new DoubleDQN(this, conf.getGamma(), conf.getErrorClamp())
                : new StandardDQN(this, conf.getGamma(), conf.getErrorClamp());

    }

    public MDP<O, RobotAngleUpdate, ContinuousSpace> getMdp() {
        return mdp.getWrappedMDP();
    }

    public void postEpoch() {

        if (getHistoryProcessor() != null)
            getHistoryProcessor().stopMonitor();

    }

    public void preEpoch() {
        lastAction = new RobotAngleUpdate(0, 0);         //fixme: change initialisation
        accuReward = 0;
    }

    /**
     * Single step of training
     *
     * @param obs last obs
     * @return relevant info for next step
     */
    protected QLStepReturn<Observation> trainStep(Observation obs) {


        RobotAngleUpdate action;
        boolean isHistoryProcessor = getHistoryProcessor() != null;


        int skipFrame = isHistoryProcessor ? getHistoryProcessor().getConf().getSkipFrame() : 1;
        int historyLength = isHistoryProcessor ? getHistoryProcessor().getConf().getHistoryLength() : 1;
        int updateStart = getConfiguration().getUpdateStart()
                + ((getConfiguration().getBatchSize() + historyLength) * skipFrame);

        Double maxQ = Double.NaN; //ignore if Nan for stats

        //if step of training, just repeat lastAction
        if (getStepCounter() % skipFrame != 0) {
            action = lastAction;
        } else {
            INDArray qs = getQNetwork().output(obs);
            int maxAction = Learning.getMaxAction(qs);
            maxQ = qs.getDouble(maxAction);

            action = getEgPolicy().nextAction(obs);
        }

        lastAction = action;

        StepReply<Observation> stepReply = mdp.step(action);

        Observation nextObservation = stepReply.getObservation();

        accuReward += stepReply.getReward() * configuration.getRewardFactor();


            //if it's not a skipped frame, you can do a step of training
            if (getStepCounter() % skipFrame == 0 || stepReply.isDone()) {

                Transition<RobotAngleUpdate> trans = new Transition<RobotAngleUpdate>(obs, action, accuReward, stepReply.isDone(), nextObservation);
                getExpReplay().store(trans);

                if (getStepCounter() > updateStart) {
                    try {
                        System.out.println("Trying "+tdTargetAlgorithm);
                        DataSet targets = setTarget(getExpReplay().getBatch()); //fixme: trace in/ ER issue
                        System.out.println("QNet ="+getQNetwork());

                        getQNetwork().fit(targets.getFeatures(), targets.getLabels());

                    } catch (Exception e) {
                        System.out.println("Wrong C= "+e.getCause());
                        DataSet targets = setTarget(getExpReplay().getBatch()); //fixme: trace in/ ER issue
                        System.out.println("QNet ="+getQNetwork());

                        getQNetwork().fit(targets.getFeatures(), targets.getLabels());

                    }

                }

                accuReward = 0;
            }

        return new QLStepReturn<Observation>(maxQ, getQNetwork().getLatestScore(), stepReply);
    }

    protected DataSet setTarget(ArrayList<Transition<RobotAngleUpdate>> transitions) {
        if (transitions.size() == 0)
            throw new IllegalArgumentException("too few transitions");

        return tdTargetAlgorithm.computeTDTargets(transitions);
    }

    @Override
    public QLConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public DQNContinuousPolicy<O> getPolicy() {
        return policy;
    }

    public void setPolicy(DQNContinuousPolicy<O> policy) {
        this.policy = policy;
    }

    @Override
    public EpsGreedy<O, RobotAngleUpdate, ContinuousSpace> getEgPolicy() {
        return egPolicy;
    }

    public void setEgPolicy(EpsGreedy<O, RobotAngleUpdate, ContinuousSpace> egPolicy) {
        this.egPolicy = egPolicy;
    }

    public IDQN getQNetwork() {
        return qNetwork;
    }

    @Override
    public IDQN getTargetQNetwork() {
        return targetQNetwork;
    }

    @Override
    public void setTargetQNetwork(IDQN targetQNetwork) {
        this.targetQNetwork = targetQNetwork;
    }

}