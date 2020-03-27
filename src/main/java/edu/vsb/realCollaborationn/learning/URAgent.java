package edu.vsb.realCollaborationn.learning;


import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.nn.api.NeuralNetwork;
import org.deeplearning4j.rl4j.learning.IEpochTrainer;
import org.deeplearning4j.rl4j.learning.ILearning;
import org.deeplearning4j.rl4j.learning.async.a3c.discrete.A3CDiscreteDense;
import org.deeplearning4j.rl4j.learning.listener.TrainingListener;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.network.NeuralNet;
import org.deeplearning4j.rl4j.network.ac.ActorCriticFactoryCompGraphStdDense;
import org.deeplearning4j.rl4j.network.ac.IActorCritic;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.deeplearning4j.rl4j.policy.ACPolicy;
import org.deeplearning4j.rl4j.policy.DQNPolicy;
import org.deeplearning4j.rl4j.space.Box;
import org.deeplearning4j.rl4j.util.IDataManager;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.stats.StatsListener;
import org.deeplearning4j.ui.storage.FileStatsStorage;
import org.nd4j.linalg.api.memory.conf.WorkspaceConfiguration;
import org.nd4j.linalg.api.memory.enums.AllocationPolicy;
import org.nd4j.linalg.api.memory.enums.LearningPolicy;
import org.nd4j.linalg.api.memory.enums.MirroringPolicy;
import org.nd4j.linalg.api.memory.enums.SpillPolicy;
import org.nd4j.linalg.api.ops.impl.layers.recurrent.config.LSTMConfiguration;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Logger;

public class URAgent {
    public static UR3Model robotModel = new UR3Model();


    public static A3CDiscreteDense.A3CConfiguration A3C_CONF = new A3CDiscreteDense.A3CConfiguration(
            123,    //Random seed
            500,    //Max step By epoch
            10000000, //Max step
            6, //Max size of experience replay
            125,     //size of batches
            10,    //target update (hard)
            0.01,     //num step noop warmup
            0.9,   //reward scaling
            1.0   //gamma
    );
        public static ActorCriticFactoryCompGraphStdDense.Configuration.ConfigurationBuilder A3C_NET =
        ActorCriticFactoryCompGraphStdDense.Configuration.builder().l2(0.001).numHiddenNodes(12)
                .numLayer(25).useLSTM(true).updater(new Adam(0.0005));


    public static void main(String[] args) throws IOException {
        //System.setErr(new PrintStream("/dev/null"));
        urAgent();
        //loadAgent();
        //testAgentPolicy()
    }

    public static void urAgent() throws IOException {


        MDP mdp = new RobotDecisionProcess(robotModel);
        //define the training
        A3CDiscreteDense a3c = new A3CDiscreteDense(mdp, A3C_NET.build(), A3C_CONF);
        //train

        TrainingListener iterationListener = new TrainingListener() {

            @Override
            public ListenerResponse onTrainingStart() {
                return null;
            }

            @Override
            public void onTrainingEnd() {

            }

            @Override
            public ListenerResponse onNewEpoch(IEpochTrainer trainer) {
                robotModel.reset();
                return null;
            }

            @Override
            public ListenerResponse onEpochTrainingResult(IEpochTrainer trainer, IDataManager.StatEntry statEntry) {
                ACPolicy policy = a3c.getPolicy();
                if(statEntry.getReward()<0)
                    System.out.println("_________NegativeRewardInEpoch____________"+statEntry.getReward()+"__FromThread___"+Thread.currentThread().getName()+"TIME="+System.currentTimeMillis());

                try {
                    policy.save("saved_pol_lstm_viz/saved_policy_ep_"+trainer.getEpochCounter());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            public ListenerResponse onTrainingProgress(ILearning learning) {
                return null;
            }
        };

        a3c.addListener(iterationListener);
        a3c.train();
        mdp.close();
    }
    public static void retrainAgent() throws IOException {
        //define the mdp from gym (name, render)
        initWorkspace();
        MDP mdp2 = new RobotDecisionProcess(robotModel);
        //load the previous agent
        ACPolicy pol2 = ACPolicy.load("saved_pol_constr/saved_policy_ep_299");

        IActorCritic preTrainedNetwork = pol2.getNeuralNet();

        A3CDiscreteDense a3c = new A3CDiscreteDense(mdp2,preTrainedNetwork, A3C_CONF);



        TrainingListener iterationListener = new TrainingListener() {

            @Override
            public ListenerResponse onTrainingStart() {
                return null;
            }

            @Override
            public void onTrainingEnd() {

            }

            @Override
            public ListenerResponse onNewEpoch(IEpochTrainer trainer) {
                return null;
            }

            @Override
            public ListenerResponse onEpochTrainingResult(IEpochTrainer trainer, IDataManager.StatEntry statEntry) {
                ACPolicy pol = a3c.getPolicy();
                try {
                    pol.save("saved_pol_lstm/saved_policy_ep_"+trainer.getEpochCounter());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            public ListenerResponse onTrainingProgress(ILearning learning) {
                return null;
            }
        };


        a3c.addListener(iterationListener);
        a3c.train();


    }
    public static void testAgentPolicy() throws IOException {
        MDP mdp2 = new RobotDecisionProcess(robotModel);
        //load the previous agent
        DQNPolicy pol2 = DQNPolicy.load("saved_pol_constr/saved_policy_ep_299");

        double reward = pol2.play(mdp2);

        System.out.println(reward);
    }

     private static void initWorkspace() {
         WorkspaceConfiguration basicConfig = WorkspaceConfiguration.builder()
                 .policyAllocation(AllocationPolicy.STRICT)
                 .policyLearning(LearningPolicy.FIRST_LOOP)
                 .policySpill(SpillPolicy.EXTERNAL)
                 .build();

         Nd4j.getWorkspaceManager().destroyWorkspace();
         Nd4j.getWorkspaceManager().createNewWorkspace(basicConfig);

     }
}