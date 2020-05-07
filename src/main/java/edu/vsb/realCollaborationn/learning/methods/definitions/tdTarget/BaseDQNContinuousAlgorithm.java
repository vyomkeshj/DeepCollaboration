package edu.vsb.realCollaborationn.learning.methods.definitions.tdTarget;

import org.deeplearning4j.rl4j.learning.sync.qlearning.TargetQNetworkSource;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.nd4j.linalg.api.ndarray.INDArray;

public class BaseDQNContinuousAlgorithm extends BaseContinuousTDTargetAlgorithm {
    private final TargetQNetworkSource qTargetNetworkSource;

    /**
     * In litterature, this corresponds to Q{net}(s(t+1), a)
     */
    protected INDArray qNetworkNextObservation;

    /**
     * In litterature, this corresponds to Q{tnet}(s(t+1), a)
     */
    protected INDArray targetQNetworkNextObservation;

    protected BaseDQNContinuousAlgorithm(TargetQNetworkSource qTargetNetworkSource, double gamma) {
        super(qTargetNetworkSource, gamma);
        this.qTargetNetworkSource = qTargetNetworkSource;
    }

    protected BaseDQNContinuousAlgorithm(TargetQNetworkSource qTargetNetworkSource, double gamma, double errorClamp) {
        super(qTargetNetworkSource, gamma, errorClamp);
        this.qTargetNetworkSource = qTargetNetworkSource;
    }

    @Override
    protected void initComputation(INDArray observations, INDArray nextObservations) {
        super.initComputation(observations, nextObservations);

        qNetworkNextObservation = qNetworkSource.getQNetwork().output(nextObservations);

        IDQN targetQNetwork = qTargetNetworkSource.getTargetQNetwork();
        targetQNetworkNextObservation = targetQNetwork.output(nextObservations);
    }

    @Override
    protected double computeTarget(int batchIdx, double reward, boolean isTerminal) {
        return 0;
    }
}
