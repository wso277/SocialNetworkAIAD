package socialGroup;

import java.awt.Color;
import uchicago.src.sim.gui.DrawableEdge;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.network.DefaultEdge;
import uchicago.src.sim.network.Node;

public class AgentEdge extends DefaultEdge implements DrawableEdge {
    private Color color = Color.WHITE;

    public AgentEdge(){}

    public AgentEdge(Node from, Node to, float strength){
        super(from, to, "", strength);
        this.color = Color.WHITE;
    }

    public void setColor(Color c){
        color = c;
    }

    public Color getColor(){
        return color;
    }
    public void draw(SimGraphics g, int fromX, int toX, int fromY, int toY){
        g.drawDirectedLink(color, fromX, toX, fromY, toY);
    }
}
