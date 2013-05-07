package epidemicProtocol.servers;

import epidemicProtocol.control.TargetsList;

/**
 * 
 * @author Lucas S Bueno
 */
public class TargetsListServer extends Thread {

   private TargetsList targetsList;
   private int bindPort;

   public TargetsListServer(TargetsList targetsList, int port) {
      this.targetsList = targetsList;
      this.bindPort = port;
   }

   @Override
   public void run() {
   }
}
