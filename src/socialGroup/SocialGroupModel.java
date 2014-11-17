package socialGroup;

import exceptions.WrongDateException;
import exceptions.WrongProbabilityValue;
import uchicago.src.sim.engine.SimpleModel;
import util.Date;

import java.util.Random;

public class SocialGroupModel extends SimpleModel {
    private int numberOfAgents;
    private String name;

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

        int numberOfRequesters = (int) (r.nextFloat() * numberOfAgents);
        System.out.println("Number of requesters: " + numberOfRequesters);
        int numberOfResponders = numberOfAgents - numberOfRequesters;

        System.out.println("Number of responders: " + numberOfResponders);
        for (int i = 0; i < numberOfRequesters; i++) {
            Date[] arr = Date.getNewDates();
            try {
                agentList.add(new Requester(r.nextInt(50) + 1, r.nextInt(100) + 1, arr[0], arr[1], Requester
                        .MusicTypes.get(r.nextInt(12))));
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
