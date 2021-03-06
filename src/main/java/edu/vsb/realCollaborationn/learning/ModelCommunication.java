package edu.vsb.realCollaborationn.learning;

import edu.vsb.realCollaborationn.learning.model.Observation;
import org.deeplearning4j.gym.StepReply;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class ModelCommunication {
    public static RobotDecisionProcess decisionProcess = new RobotDecisionProcess();

    public static void main(String[] args) {
        run();
    }
    public static void run() {

        while (true) {

            try (ZContext context = new ZContext()) {
                // Socket to talk to clients
                ZMQ.Socket socket = context.createSocket(ZMQ.REP);
                socket.bind("tcp://*:5555");
                while (!Thread.currentThread().isInterrupted()) {
                    // Block until a message is received
                    byte[] reply = socket.recv(0);
                    // Print the message
                    String s = new String(reply, ZMQ.CHARSET);
                    //System.out.println("input__"+s);
                    if (s.contains(",")) {
                        String[] data = s.split(",");
                        double angleA = Double.parseDouble(data[0]);
                        double angleB = Double.parseDouble(data[1]);
                        double angleC = Double.parseDouble(data[2]);
                        double angleD = Double.parseDouble(data[3]);
                        double angleE = Double.parseDouble(data[4]);

                        StepReply<Observation> observationStepReply = decisionProcess.step(angleA, angleB, angleC, angleD, angleE);
                        String response = observationStepReply.getObservation().toString()
                                + "," + observationStepReply.getReward()
                                + "," + (observationStepReply.isDone() ? 1 : 0);
                        //System.out.println("Observation" + response);

                        socket.send(response.getBytes(ZMQ.CHARSET), 0);
                    } else {
                        decisionProcess.reset();
                        StepReply<Observation> observationStepReply = decisionProcess.step(4);
                        String response = observationStepReply.getObservation().toString()
                                + "," + observationStepReply.getReward()
                                + "," + (observationStepReply.isDone() ? 1 : 0);
                        socket.send(response.getBytes(ZMQ.CHARSET), 0);

                    }
                }
            }
        }
    }
}
