package edu.vsb.realCollaborationn.learning;


import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.rl4j.learning.IEpochTrainer;
import org.deeplearning4j.rl4j.learning.ILearning;
import org.deeplearning4j.rl4j.learning.listener.TrainingListener;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.deeplearning4j.rl4j.policy.DQNPolicy;
import org.deeplearning4j.rl4j.space.Box;
import org.deeplearning4j.rl4j.util.IDataManager;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.stats.StatsListener;
import org.deeplearning4j.ui.storage.FileStatsStorage;
import org.deeplearning4j.ui.storage.InMemoryStatsStorage;
import org.nd4j.linalg.learning.config.Adam;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class URAgent {
    public static UR3Model robotModel = new UR3Model();

    public static QLearning.QLConfiguration UR_QL_CONF =
            new QLearning.QLConfiguration(
                    123,    //Random seed
                    1000,    //Max step By epoch
                    500000, //Max step
                    15000, //Max size of experience replay
                    64,     //size of batches
                    1000,    //target update (hard)
                    10,     //num step noop warmup
                    0.01,   //reward scaling
                    0.90,   //gamma
                    1.0,    //td-error clipping
                    0.1f,   //min epsilon
                    10000,   //num step for eps greedy anneal
                    true    //double DQN
            );


    public static DQNFactoryStdDense.Configuration.ConfigurationBuilder UR_NET =
            DQNFactoryStdDense.Configuration.builder()
                    .l2(0.001).updater(new Adam(0.0005)).numHiddenNodes(16).numLayer(25);


    public static void main(String[] args) throws IOException {

        urAgent();
        //loadAgent();
    }

    public static void urAgent() throws IOException {
        UIServer uiServer = UIServer.getInstance();
        File statsStorageFile = new File("training_stats_1");
        StatsStorage statsStorage = new FileStatsStorage(statsStorageFile);
        org.deeplearning4j.optimize.api.TrainingListener[] listeners = {new StatsListener(statsStorage)};
        UR_NET.listeners(listeners);

        uiServer.attach(statsStorage);

        MDP mdp = new RobotDecisionProcess(robotModel);
        //define the training
        QLearningDiscreteDense dql = new QLearningDiscreteDense(mdp, UR_NET.build(), UR_QL_CONF);


        TrainingListener iterationListener = new TrainingListener() {
            @Override
            public ListenerResponse onTrainingStart() {
                return null;
            }

            @Override
            public void onTrainingEnd() {}

            @Override
            public ListenerResponse onNewEpoch(IEpochTrainer trainer) {
                return null;
            }

            @Override
            public ListenerResponse onEpochTrainingResult(IEpochTrainer trainer, IDataManager.StatEntry statEntry) {
                DQNPolicy pol = dql.getPolicy();
                try {
                    pol.save("saved_policies/saved_policy_ep_"+trainer.getEpochCounter());
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


        dql.addListener(iterationListener);
        dql.train();

        mdp.close();
    }
    public static void retrainAgent() throws IOException {
        //define the mdp from gym (name, render)
        MDP mdp2 = new RobotDecisionProcess(robotModel);
        //load the previous agent
        DQNPolicy pol2 = DQNPolicy.load("saved_policies/saved_policy_ep_249");

        IDQN preTrainedNetwork = pol2.getNeuralNet();
        QLearningDiscreteDense dql = new QLearningDiscreteDense(mdp2, preTrainedNetwork, UR_QL_CONF);
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
                DQNPolicy pol = dql.getPolicy();
                try {
                    pol.save("saved_policies_2/saved_policy_ep_"+trainer.getEpochCounter());
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


        dql.addListener(iterationListener);
        dql.train();


    }
}