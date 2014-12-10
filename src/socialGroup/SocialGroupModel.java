package socialGroup;

import exceptions.WrongDateException;
import exceptions.WrongProbabilityValue;
import uchicago.src.sim.engine.SimpleModel;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Network2DDisplay;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.gui.OvalNetworkItem;
import uchicago.src.sim.network.DefaultDrawableNode;
import util.Date;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class SocialGroupModel extends SimpleModel {
    private int numberOfAgents;
    private String name;
    private final Integer MAX_DISTANCE = 100;
    private final Integer MAX_PRICE = 100;
    public static DisplaySurface surface;
    static public ArrayList<AgentNode> drawableAgents = new ArrayList<AgentNode>();

    public SocialGroupModel(String name, int numberOfAgents) {
        this.name = name;
        this.numberOfAgents = numberOfAgents;
    }

    public void setup() {
        super.setup();
        autoStep = true;
        shuffle = true;
        if (surface != null) surface.dispose();
        surface = new DisplaySurface(this, "NETWORK DISPLAY");
        registerDisplaySurface("Display", surface);
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
                Requester req = new Requester(r.nextInt(MAX_DISTANCE), r.nextInt(MAX_PRICE), arr[0], arr[1], Responder.music.get(r.nextInt(Responder.music.size())), i);
                if (i > 0) {
                    req.setNeighbour((Requester) (agentList.get(i - 1)));
                }
                int x = r.nextInt(80);
                int y = r.nextInt(80);
                AgentNode node = new AgentNode(x, y, "Requester", i);
                node.setColor(Color.BLUE);
                drawableAgents.add(node);
                agentList.add(req);
            } catch (WrongDateException e) {
                System.err.println(e.getMessage());
            }
        }

        Requester req = (Requester) (agentList.get(0));
        req.setNeighbour((Requester) (agentList.get(agentList.size() - 1)));

        for (int i = 0; i < numberOfResponders; i++) {
            try {
                agentList.add(new Responder(r.nextFloat(), r.nextFloat(), r.nextFloat(), r.nextFloat(), numberOfRequesters));
                int x = r.nextInt(80);
                int y = r.nextInt(80);
                AgentNode node = new AgentNode(x, y, "Responder", numberOfRequesters);
                node.setColor(Color.RED);
                node.setNodeLabel(Integer.toString(i));
                drawableAgents.add(node);
                numberOfRequesters++;
            } catch (WrongProbabilityValue e) {
                System.err.println(e.getMessage());
            }
        }

        System.out.println("AgentList size: " + agentList.size());
    }

    public void buildDisplay() {
        Network2DDisplay display = new Network2DDisplay(drawableAgents, 100, 100);
        surface.addDisplayableProbeable(display, "Display");
        surface.addZoomable(display);
        addSimEventListener(surface);
    }

    protected void preStep() {
        System.out.println("\n\n\nInitiating step " + getTickCount());
    }

    protected void postStep() {
        System.out.println("Done step " + getTickCount());
    }

    public ArrayList getAgentList() {
        return agentList;
    }

    public Integer getMaxDistance() {
        return MAX_DISTANCE;
    }

    public Integer getMaxPrice() {
        return MAX_PRICE;
    }

    public void begin() {
        buildModel();
        buildDisplay();
        buildSchedule();
        surface.display();
    }

}
