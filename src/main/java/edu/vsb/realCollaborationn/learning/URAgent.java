package edu.vsb.realCollaborationn.learning;


import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import org.deeplearning4j.rl4j.learning.IEpochTrainer;
import org.deeplearning4j.rl4j.learning.ILearning;
import org.deeplearning4j.rl4j.learning.listener.TrainingListener;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.deeplearning4j.rl4j.policy.DQNPolicy;
import org.deeplearning4j.rl4j.util.IDataManager;
import org.nd4j.linalg.api.memory.conf.WorkspaceConfiguration;
import org.nd4j.linalg.api.memory.enums.AllocationPolicy;
import org.nd4j.linalg.api.memory.enums.LearningPolicy;
import org.nd4j.linalg.api.memory.enums.SpillPolicy;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;

import java.io.IOException;
import java.io.PrintStream;

public class URAgent {
    public static UR3Model robotModel = new UR3Model();

    public static QLearning.QLConfiguration CARTPOLE_QL =
            new QLearning.QLConfiguration(
                    123,    //Random seed
                    200,    //Max step By epoch
                    2500000, //Max step
                    25000, //Max size of experience replay
                    128,     //size of batches
                    2,    //target update (hard)
                    10,     //num step noop warmup
                    0.01,   //reward scaling
                    0.90,   //gamma
                    1.0,    //td-error clipping
                    0.05f,   //min epsilon
                    1000000,   //num step for eps greedy anneal
                    true    //double DQN
            );

    public static DQNFactoryStdDense.Configuration CARTPOLE_NET =
            DQNFactoryStdDense.Configuration.builder()
                    .l2(0.001).updater(new Adam(0.0001)).numHiddenNodes(300).numLayer(2).build();

    public static void main(String[] args) throws IOException {
        System.setErr(new PrintStream("/dev/null"));
        urAgent();
        //loadAgent();
        //testAgentPolicy()
    }

    public static void urAgent() throws IOException {


        MDP mdp = new RobotDecisionProcessDiscrete();
        //define the training
        QLearningDiscreteDense a3c = new QLearningDiscreteDense(mdp, CARTPOLE_NET, CARTPOLE_QL);
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
                return null;
            }

            @Override
            public ListenerResponse onEpochTrainingResult(IEpochTrainer trainer, IDataManager.StatEntry statEntry) {
                DQNPolicy policy = a3c.getPolicy();
                //if(statEntry.getReward()<0)
                   //System.out.println("_________NegativeRewardInEpoch____________"+statEntry.getReward()+"__FromThread___"+Thread.currentThread().getName()+"TIME="+System.currentTimeMillis());

                try {
                    policy.save("dqn/saved_policy_ep_"+trainer.getEpochCounter());
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

    public static void testAgentPolicy() throws IOException {
        MDP mdp2 = new RobotDecisionProcessDiscrete(robotModel);
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