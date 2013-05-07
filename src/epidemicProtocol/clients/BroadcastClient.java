package epidemicProtocol.clients;

/**
 * This thread sends simple broadcast messages in the network periodically. With it we can make a
 * list of the available SpredServers in the local network.
 *
 * @author Lucas S Bueno
 */
public class BroadcastClient extends Thread {

   private int targetPort; //target port used to make the broadcast communication 
   private final int REPEAT_TIME = 600000; //time of wait to repeat the operation in milliseconds
   
   /**
    * Initialize the object with a defined {@link epidemicProtocol.servers.TargetsListServer} port.
    * This port was defined in {@link epidemicProtocol.view.MainWindow#TARGETS_LIST_PORT}.
    *
    * @param port Port used in the {@link epidemicProtocol.servers.TargetsListServer}
    */
   public BroadcastClient(int port) {
      this.targetPort = port;
   }

   @Override
   public void run() {
   }
}
