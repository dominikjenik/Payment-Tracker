import actors.Router;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * Created by Jenik on 11/24/2015.
 */
public class PaymentTrackerRunner {

  public static final String TERMINATE_MESSAGE = "quit";

  public static void main(String[] args) throws TimeoutException {
        ActorSystem system = ActorSystem.create("PaymentTracker", ConfigFactory.load("debug.conf"));
        ActorRef router = system.actorOf(Props.create(Router.class), "router");
        Inbox inbox = Inbox.create(system);

        ConsoleLogger consoleLogger=new ConsoleLogger(inbox, system);
        ExecutorService executorService= Executors.newSingleThreadExecutor();
        executorService.execute(consoleLogger);

        Scanner in= new Scanner(System.in);
        while(in.hasNextLine()){
          final String line = in.nextLine();
          if( TERMINATE_MESSAGE.equals( line ) ) {
            break;
          }
          inbox.send(router, line);
        }

        System.out.println("closing...");
        consoleLogger.setRunning(false);
        executorService.shutdown();
        system.shutdown();
    }
}