package actors;

import akka.actor.UntypedActor;

/**
 * Created by Jenik on 11/24/2015.
 */
public class Router extends UntypedActor {
    private String inputPattern= "([A-Z]{3}) (([+]?[\\d*\\.?[0-9]*) (\\(USD ([\\d*\\.?[0-9]*)\\))?|([+-]?[\\d*\\.?[0-9]*))";

    @Override
    public void onReceive(Object o) throws Exception {
        System.out.println(o);
        getSender().tell("Yeah sure sir", getSelf());
    }
}
