package edu.vsb.realCollaborationn.learning;


import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import org.deeplearning4j.nn.api.NeuralNetwork;
import org.deeplearning4j.rl4j.learning.IEpochTrainer;
import org.deeplearning4j.rl4j.learning.ILearning;
import org.deeplearning4j.rl4j.learning.listener.TrainingListener;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.network.NeuralNet;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.deeplearning4j.rl4j.policy.DQNPolicy;
import org.deeplearning4j.rl4j.space.Box;
import org.deeplearning4j.rl4j.util.IDataManager;
import org.nd4j.linalg.api.memory.conf.WorkspaceConfiguration;
import org.nd4j.linalg.api.memory.enums.AllocationPolicy;
import org.nd4j.linalg.api.memory.enums.LearningPolicy;
import org.nd4j.linalg.api.memory.enums.MirroringPolicy;
import org.nd4j.linalg.api.memory.enums.SpillPolicy;
import org.nd4j.linalg.api.ops.impl.layers.recurrent.config.LSTMConfiguration;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;

import java.io.IOException;
import java.util.logging.Logger;

public class URAgent {
    public static UR3Model robotModel = new UR3Model();

    public static QLearning.QLConfiguration UR_QL_CONF =
            new QLearning.QLConfiguration(
                    123,    //Random seed
                    500,    //Max step By epoch
                    150000, //Max step
                    15000, //Max size of experience replay
                    32,     //size of batches
                    250,    //target update (hard)
                    10,     //num step noop warmup
                    0.1,   //reward scaling
                    1,   //gamma
                    1.0,    //td-error clipping
                    0.0001f,   //min epsilon
                    10000,   //num step for eps greedy anneal
                    true    //double DQN
            );


    public static DQNFactoryStdDense.Configuration.ConfigurationBuilder UR_NET =
            DQNFactoryStdDense.Configuration.builder()
                    .l2(0.001).updater(new Adam(0.0001)).numHiddenNodes(12).numLayer(5);



    public static void main(String[] args) throws IOException {
        //retrainAgent();
        //loadAgent();
        //testAgentPolicy();



        urAgent();



    }

    public static void urAgent() throws IOException {

        MDP mdp = new RobotDecisionProcess(robotModel);
        //define the training
        QLearningDiscreteDense dql = new QLearningDiscreteDense(mdp, UR_NET.build(), UR_QL_CONF);

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
                    pol.save("saved_pol_constr_2/saved_policy_ep_"+trainer.getEpochCounter());
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
        initWorkspace();
        MDP mdp2 = new RobotDecisionProcess(robotModel);
        //load the previous agent
        DQNPolicy pol2 = DQNPolicy.load("saved_pol_constr/saved_policy_ep_299");

        IDQN preTrainedNetwork = pol2.getNeuralNet();

        QLearningDiscreteDense dql = new QLearningDiscreteDense(mdp2, preTrainedNetwork, UR_QL_CONF);

        NeuralNetwork[] trainingNets = dql.getNeuralNet().getNeuralNetworks();


        for (int i = 0; i < trainingNets.length; i++) {

        }

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