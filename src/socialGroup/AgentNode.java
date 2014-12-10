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
    private final int DELTA_DISTANCE = 1000;

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
        int doApproximate = 1;
        if(betaTrust < 0.5) doApproximate = -1;

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

            double diffX = Math.abs(myX - destX);
            double diffY = Math.abs(myY - destY);

            if(diffX <= 5 || diffX >= 50 || diffY <= 5 || diffY >= 50) return;

            if(myX > destX){
                setX(myX - doApproximate * diffX / DELTA_DISTANCE);
                ((DefaultDrawableNode)destNode).setX(destX + doApproximate * diffX / DELTA_DISTANCE);
            }
            else{
                setX(myX + doApproximate * diffX / DELTA_DISTANCE);
                ((DefaultDrawableNode)destNode).setX(destX - doApproximate * diffX / DELTA_DISTANCE);
            }

            if(myY > destY){
                setY(myY - doApproximate * diffY / DELTA_DISTANCE);
                ((DefaultDrawableNode)destNode).setY(destY + doApproximate * diffY / DELTA_DISTANCE);
            }
            else{
                setY(myY + doApproximate * diffY / DELTA_DISTANCE);
                ((DefaultDrawableNode)destNode).setY(destY - doApproximate * diffY / DELTA_DISTANCE);
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