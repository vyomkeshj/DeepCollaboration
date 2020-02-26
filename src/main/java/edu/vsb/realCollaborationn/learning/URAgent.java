package edu.vsb.realCollaborationn.learning;


import edu.vsb.realCollaborationn.learning.model.Observation;
import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.nn.api.Model;
import org.deeplearning4j.optimize.api.TrainingListener;
import org.deeplearning4j.rl4j.learning.async.a3c.discrete.A3CDiscrete;
import org.deeplearning4j.rl4j.learning.async.a3c.discrete.A3CDiscreteDense;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.network.ac.ActorCriticFactoryCompGraphStdDense;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.deeplearning4j.rl4j.policy.ACPolicy;
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
    static UIServer uiServer = UIServer.getInstance();


    public static A3CDiscreteDense.A3CConfiguration A3C_CONF = new A3CDiscreteDense.A3CConfiguration(
            123,    //Random seed
            1000,    //Max step By epoch
            1000000, //Max step
            6, //Max size of experience replay
            1000,     //size of batches
            10,    //target update (hard)
            0.01,     //num step noop warmup
            0.99,   //reward scaling
            1.0   //gamma
    );
        public static ActorCriticFactoryCompGraphStdDense.Configuration.ConfigurationBuilder A3C_NET =
        ActorCriticFactoryCompGraphStdDense.Configuration.builder().l2(0.001).numHiddenNodes(20)
                .numLayer(20).useLSTM(false).updater(new Adam(0.0005));


    public static void main(String[] args) throws IOException {
        urAgent();
        //loadAgent();
    }

    public static void urAgent() throws IOException {
        //Initialize the user interface backend

        //Configure where the network information (gradients, score vs. time etc) is to be stored. Here: store in memory.
        StatsStorage statsStorage = new InMemoryStatsStorage();         //Alternative: new FileStatsStorage(File), for saving and loading later

        //Attach the StatsStorage instance to the UI: this allows the contents of the StatsStorage to be visualized
        uiServer.attach(statsStorage);

        //Then add the StatsListener to collect this information from the network, as it trains
        TrainingListener[] listeners = {new StatsListener(statsStorage)};

        MDP mdp = new RobotDecisionProcess(robotModel);
        //define the training
        A3CDiscreteDense a3c = new A3CDiscreteDense(mdp, A3C_NET.build(), A3C_CONF);
        //train
        a3c.train();

        //Initialize the user interface backend


        //get the final policy
        ACPolicy pol = a3c.getPolicy();
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