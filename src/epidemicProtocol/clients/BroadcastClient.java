package epidemicProtocol.clients;

import static java.lang.Thread.sleep;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * This thread sends simple broadcast messages in the network periodically. With it we can make a
 * list of the available SpredServers in the local network.
 *
 * @author Lucas S Bueno
 */
public class BroadcastClient extends Thread {

   private int targetPort; //target port used to make the broadcast communication 
   private int repeatTime; //time of wait to repeat the operation in milliseconds
   private InetAddress broadcastAddress;
   private DatagramSocket socket;
   private DatagramPacket packet;
   private final byte[] message = "#".getBytes(); //default message to be sent

   /**
    * Initialize the object with a defined {@link epidemicProtocol.servers.TargetsListServer} port.
    * This port was defined in {@link epidemicProtocol.view.MainWindow#TARGETS_LIST_PORT}.
    *
    * @param port Port used in the {@link epidemicProtocol.servers.TargetsListServer}
    */
   public BroadcastClient(int port) {
      this.targetPort = port;
      this.repeatTime = 5000;

      try {
         broadcastAddress = InetAddress.getByName("255.255.255.255");
      } catch (Exception ex) {
         System.err.println("Error: " + ex.getMessage());
      }
   }

   @Override
   public void run() {
      try {
         sleep(repeatTime);
      } catch (Exception ex) {
         System.err.println("Error: " + ex.getMessage());
      }

      /*
       * loop that send packets in broadcast periodically, based in the repeatTime variable.
       * the broadcast messages are sent more frequently in the start of the thread and with time
       * it stabilizes in 4 minutes.
       */
      while (true) {
         try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
            packet = new DatagramPacket(message, message.length, broadcastAddress, targetPort);
            socket.send(packet);

            socket.close();

            if (repeatTime < 240000) {
               repeatTime += 5000;
            }

            sleep(repeatTime);
         } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
         }
      }
   }
}
