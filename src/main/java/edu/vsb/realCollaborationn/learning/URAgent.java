package edu.vsb.realCollaborationn.learning;


import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.rl4j.learning.IEpochTrainer;
import org.deeplearning4j.rl4j.learning.ILearning;
import org.deeplearning4j.rl4j.learning.async.a3c.discrete.A3CDiscreteDense;
import org.deeplearning4j.rl4j.learning.listener.TrainingListener;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.network.ac.ActorCriticFactoryCompGraphStdDense;
import org.deeplearning4j.rl4j.network.ac.IActorCritic;
import org.deeplearning4j.rl4j.policy.ACPolicy;
import org.deeplearning4j.rl4j.util.IDataManager;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.stats.StatsListener;
import org.deeplearning4j.ui.storage.InMemoryStatsStorage;
import org.nd4j.linalg.api.memory.conf.WorkspaceConfiguration;
import org.nd4j.linalg.api.memory.enums.AllocationPolicy;
import org.nd4j.linalg.api.memory.enums.LearningPolicy;
import org.nd4j.linalg.api.memory.enums.SpillPolicy;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.shade.guava.math.Stats;

import java.io.IOException;
import java.io.PrintStream;

public class URAgent {
    public static UR3Model robotModel = new UR3Model();


    public static A3CDiscreteDense.A3CConfiguration A3C_CONF = new A3CDiscreteDense.A3CConfiguration(
            123,    //Random seed
            500,    //Max step By epoch
            10000000, //Max step
            24, //Max size of experience replay
            10000000,     //size of batches
            10,    //target update (hard)
            0.01,     //num step noop warmup
            1,   //reward scaling
            1.0   //gamma
    );
        public static ActorCriticFactoryCompGraphStdDense.Configuration.ConfigurationBuilder A3C_NET =
        ActorCriticFactoryCompGraphStdDense.Configuration.builder().l2(0.001).numHiddenNodes(12)
                .numLayer(25).useLSTM(false).updater(new Adam(0.0005));


    public static void main(String[] args) throws IOException {
        urAgent();
        //testAgentPolicy();
    }

    public static void urAgent() throws IOException {

        UIServer uiServer = UIServer.getInstance();
        StatsStorage statsStorage = new InMemoryStatsStorage();
        org.deeplearning4j.optimize.api.TrainingListener[] listeners = {new StatsListener(statsStorage)};
        A3C_NET.listeners(listeners);

        uiServer.attach(statsStorage);


        MDP mdp = new RobotDecisionProcess(robotModel);
        A3CDiscreteDense a3c = new A3CDiscreteDense(mdp, A3C_NET.build(), A3C_CONF);

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
                ACPolicy policy = a3c.getPolicy();

                try {
                    policy.save("saved_policies_2/saved_policy_ep_"+trainer.getEpochCounter());
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

        ACPolicy pol2 = ACPolicy.load("saved_policies/saved_policy_ep_848");

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