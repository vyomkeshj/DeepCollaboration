package edu.vsb.realCollaborationn.learning;


import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.deeplearning4j.rl4j.policy.DQNPolicy;
import org.deeplearning4j.rl4j.space.Box;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.stats.StatsListener;
import org.deeplearning4j.ui.storage.InMemoryStatsStorage;
import org.nd4j.linalg.learning.config.Adam;

import java.io.IOException;
import java.util.logging.Logger;

public class URAgent {
    public static UR3Model robotModel = new UR3Model();

    public static QLearning.QLConfiguration UR_QL_CONF =
            new QLearning.QLConfiguration(
                    123,    //Random seed
                    1000,    //Max step By epoch
                    50000, //Max step
                    15000, //Max size of experience replay
                    32,     //size of batches
                    300,    //target update (hard)
                    10,     //num step noop warmup
                    0.01,   //reward scaling
                    0.99,   //gamma
                    1.0,    //td-error clipping
                    0.1f,   //min epsilon
                    1000,   //num step for eps greedy anneal
                    true    //double DQN
            );


    public static DQNFactoryStdDense.Configuration.ConfigurationBuilder UR_NET =
            DQNFactoryStdDense.Configuration.builder()
                    .l2(0.001).updater(new Adam(0.0005)).numHiddenNodes(16).numLayer(10);


    public static void main(String[] args) throws IOException {
        urAgent();
        loadAgent();
    }

    public static void urAgent() throws IOException {


        MDP mdp = new RobotDecisionProcess(robotModel);
        //define the training
        QLearningDiscreteDense dql = new QLearningDiscreteDense(mdp, UR_NET.build(), UR_QL_CONF);
        //train
        dql.train();

        //Initialize the user interface backend


        //get the final policy
        DQNPolicy pol = dql.getPolicy();
        //serialize and save (serialization showcase, but not required)
        pol.save("saved_policy");
        //close the mdp (close http)
        mdp.close();
    }
    public static void loadAgent() throws IOException {
        //define the mdp from gym (name, render)
        MDP mdp2 = new RobotDecisionProcess(robotModel);
        //load the previous agent
        DQNPolicy<Box> pol2 = DQNPolicy.load("saved_policy");
        //evaluate the agent
        double rewards = 0;
        for (int i = 0; i < 1000; i++) {
            mdp2.reset();
            double reward = pol2.play(mdp2);
            rewards += reward;
            Logger.getAnonymousLogger().info("Reward: " + reward);
        }
        Logger.getAnonymousLogger().info("average: " + rewards/3000);
    }
}