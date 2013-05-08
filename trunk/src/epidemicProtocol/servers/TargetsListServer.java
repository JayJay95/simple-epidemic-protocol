package epidemicProtocol.servers;

import epidemicProtocol.control.TargetsList;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * This server is bind in the port defined in
 * {@link epidemicProtocol.view.MainWindow#TARGETS_LIST_PORT}. It receives packets and try to add
 * the sender host to the {@link epidemicProtocol.control.TargetsList}.
 *
 * @author Lucas S Bueno
 */
public class TargetsListServer extends Thread {

   private TargetsList targetsList;
   private int bindPort;
   private DatagramSocket socket;
   private DatagramPacket packet;
   private String messageReceived;
   private byte[] packetBytes;
   private String anAddress;

   public TargetsListServer(TargetsList targetsList, int port) {
      this.targetsList = targetsList;
      this.bindPort = port;
      this.packetBytes = new byte["#".length()];

      try {
         socket = new DatagramSocket(this.bindPort);
         packet = new DatagramPacket(packetBytes, packetBytes.length);
      } catch (Exception ex) {
         System.err.println("Error: " + ex.getMessage());
         ex.printStackTrace();
      }
   }

   @Override
   public void run() {
      while (true) {
         try {
            socket.receive(packet);
            messageReceived = new String(packet.getData());

            /*
             * verify if the message is valid
             */
            if (messageReceived.equals("#")) {
               anAddress = packet.getAddress().getHostAddress();
               targetsList.tryToAdd(anAddress);
            }
         } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace();
         }
      }
   }
}
