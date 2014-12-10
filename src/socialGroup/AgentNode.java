package socialGroup;

import java.awt.Color;

import commerce.DeliveryCompany;
import uchicago.src.sim.gui.DrawableEdge;
import uchicago.src.sim.gui.OvalNetworkItem;
import uchicago.src.sim.network.DefaultDrawableNode;
import uchicago.src.sim.network.DefaultNode;
import uchicago.src.sim.network.Edge;

public class AgentNode extends DefaultDrawableNode{
    private String type;
    private int id;
    private final int DELTA_DISTANCE = 100;

    public AgentNode(int x, int y, String type, int id){
        OvalNetworkItem oval = new OvalNetworkItem(x,y);
        oval.allowResizing(false);
        oval.setHeight(5);
        oval.setWidth(3);
        setDrawable(oval);
        this.id = id;
        this.type = type;
    }

    public void makeEdgeTo(DefaultNode destNode, float betaTrust){
        if(betaTrust < 0.5) {
            setColor(Color.CYAN);
            return;
        }
        else{
            setColor(Color.GREEN);
        }

        if(!hasEdgeTo(destNode)){
            AgentEdge edge = new AgentEdge(this, destNode, 1);
            addOutEdge(edge);
            destNode.addInEdge(edge);
        }
        else{
            double myX = getX();
            double myY = getY();
            double destX = ((DefaultDrawableNode)destNode).getX();
            double destY = ((DefaultDrawableNode)destNode).getY();
            int proximityFactor = 0;
            double diffX = Math.abs(myX - destX);
            double diffY = Math.abs(myY - destY);

            if(diffX <= 1 || diffX >= 80 || diffY <= 1 || diffY >= 80) return;

           if(betaTrust >= 0.5 && betaTrust < 0.6) proximityFactor = 0;
            else if(betaTrust >= 0.6 && betaTrust < 0.7) proximityFactor = 1;
            else if(betaTrust >= 0.7 && betaTrust < 0.8) proximityFactor = 2;
            else if(betaTrust >= 0.8 && betaTrust < 0.9) proximityFactor = 3;
            else if(betaTrust >= 0.9 && betaTrust <= 1.0) proximityFactor = 4;

            proximityFactor *= (DELTA_DISTANCE/5);

            if(myX > destX){
                setX(myX - diffX / (DELTA_DISTANCE - proximityFactor));
            }
            else{
                setX(myX + diffX / (DELTA_DISTANCE - proximityFactor));
            }

            if(myY > destY){
               setY(myY - diffY / (DELTA_DISTANCE - proximityFactor));
            }
            else{
                setY(myY + diffY / (DELTA_DISTANCE - proximityFactor));
            }
        }
    }

    public String getType(){
        return type;
    }

    public int getAgentId(){
        return id;
    }
}