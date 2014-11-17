package socialGroup;

import exceptions.WrongDateException;
import exceptions.WrongProbabilityValue;
import uchicago.src.sim.engine.SimpleModel;
import util.Date;

import java.util.Random;

public class SocialGroupModel extends SimpleModel {
    private int numberOfAgents;
    private String name;
    final int MAX_DISTANCE = 50;
    final int MAX_PRICE = 100;

    public SocialGroupModel(String name, int numberOfAgents) {
        this.name = name;
        this.numberOfAgents = numberOfAgents;
    }

    public void setup() {
        super.setup();
        autoStep = true;
        shuffle = true;
    }

    public void buildModel() {
        Random r = new Random();
        float requesterValue;
        int numberOfRequesters, numberOfResponders;
        do {
            requesterValue = r.nextFloat();
        } while (requesterValue < 0.3 || requesterValue > 0.7);

        numberOfRequesters = (int) (requesterValue * numberOfAgents);

        System.out.println("Number of requesters: " + numberOfRequesters);
        numberOfResponders = numberOfAgents - numberOfRequesters;

        System.out.println("Number of responders: " + numberOfResponders);

        for (int i = 0; i < numberOfRequesters; i++) {
            Date[] arr = Date.getNewDates();
            try {
                agentList.add(new Requester(r.nextInt(MAX_DISTANCE), r.nextInt(MAX_PRICE), arr[0], arr[1], Requester
                        .MusicTypes.get(r.nextInt(Requester.MusicTypes.size()))));
            } catch (WrongDateException e) {
                System.err.println(e.getMessage());
            }
        }

        for (int i = 0; i < numberOfResponders; i++) {
            try {
                agentList.add(new Responder(r.nextFloat(), r.nextFloat(), r.nextFloat(), r.nextFloat()));
            } catch (WrongProbabilityValue e) {
                System.err.println(e.getMessage());
            }
        }

        System.out.println("AgentList size: " + agentList.size());
    }

    protected void preStep() {
        System.out.println("Initiating step " + getTickCount());
    }

    protected void postStep() {
        System.out.println("Done step " + getTickCount());
    }
}