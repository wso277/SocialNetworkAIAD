package socialGroup;

import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi;
import exceptions.WrongDateException;
import exceptions.WrongProbabilityValue;
import util.Date;

/**
 * Created by Fabio on 12/12/2014.
 */
public class ModelTester {

    public static void main(String[] args){
        try {
            Requester r1 = new Requester(20, 30, new Date(12, 12, 2014), new Date(1, 2, 2015), "ROCK", 0);
            Requester r2 = new Requester(20, 30, new Date(12, 12, 2014), new Date(1, 2, 2015), "ROCK", 1);
            Requester r3 = new Requester(20, 30, new Date(12, 12, 2014), new Date(1, 2, 2015), "ROCK", 2);
            Requester r4 = new Requester(20, 30, new Date(12, 12, 2014), new Date(1, 2, 2015), "ROCK", 3);
            Requester r5 = new Requester(20, 30, new Date(12, 12, 2014), new Date(1, 2, 2015), "ROCK", 4);
        }catch(WrongDateException ex){

        }

        try {
            Responder rp1 = new Responder(0.8f, 0.5f, 0.4f, 0.6f, 0);
            Responder rp2 = new Responder(0.7f, 0.6f, 0.8f, 0.5f, 1);
            Responder rp3 = new Responder(0.5f, 0.8f, 0.6f, 0.7f, 2);
            Responder rp4 = new Responder(0.9f, 0.3f, 0.2f, 0.2f, 3);
        }catch(WrongProbabilityValue value){

        }
    }
}
