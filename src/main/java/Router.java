import akka.actor.UntypedActor;

/**
 * Created by Jenik on 11/24/2015.
 */
public class Router extends UntypedActor {
    @Override
    public void onReceive(Object o) throws Exception {
        System.out.println(o);
        getSender().tell("Yeah sure sir", getSelf());
    }
}
