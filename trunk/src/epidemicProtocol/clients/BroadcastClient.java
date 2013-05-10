package epidemicProtocol.clients;

import static java.lang.Thread.sleep;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * This thread sends simple broadcast messages in the network periodically. With it we can make a
 * list of the available SpredServers in the local network.
 *
 * @author Lucas S Bueno
 */
public class BroadcastClient extends Thread {

   private int targetPort; //target port used to make the broadcast communication 
   private int repeatTime; //time of wait to repeat the operation in milliseconds   
   List<InetAddress> broadcastList; //list of all network interfaces broadcast addresses 
   private DatagramSocket socket; //socket used to send the broadcast packet
   private DatagramPacket packet; //packet sent in broadcast
   private final byte[] message = "#".getBytes(); //default message to be sent
   private final int MAX_REPEAT_TIME = 60000; //max timr of wait to repeat the operation in milliseconds 

   /**
    * Initialize the object with a defined {@link epidemicProtocol.servers.TargetsListServer} port.
    * This port was defined in {@link epidemicProtocol.view.MainWindow#TARGETS_LIST_PORT}.
    *
    * @param port Port used in the {@link epidemicProtocol.servers.TargetsListServer}.
    */
   public BroadcastClient(int port) {
      this.targetPort = port;
      this.repeatTime = 5000;

      try {
         broadcastList = new ArrayList<>();

         //get network interfaces
         Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
         InetAddress localHost = InetAddress.getLocalHost();

         while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();

            //verify if the interface is loopback. it will not be used.
            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
               continue;
            }

            //for each interface, get it's broadcast address
            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
               InetAddress address = interfaceAddress.getAddress();

               /*
                * verify if the interface's address is equal to the local host
                * local host normally is the LAN entry 
                */
               if (!localHost.getHostAddress().equals(address.getHostAddress())) {
                  continue;
               }

               InetAddress broadcastAddress = interfaceAddress.getBroadcast();

               //IPv6 test
               if (broadcastAddress == null) {
                  continue;
               } else {
                  broadcastList.add(broadcastAddress);
               }
            }
         }
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
       * it stabilizes in MAX_REPEAT_TIME milliseconds.
       */
      while (true) {
         try {
            for (InetAddress broadcastAddress : broadcastList) {
               socket = new DatagramSocket();
               socket.setBroadcast(true);
               packet = new DatagramPacket(message, message.length, broadcastAddress, targetPort);
               socket.send(packet); //send broadcast packet

               socket.close();
            }

            //update the repeatTime value
            if (repeatTime < MAX_REPEAT_TIME) {
               repeatTime += 1000;
            }

            sleep(repeatTime); //suspends the thread for repeatTime time milliseconds
         } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());            
         }
      }
   }
}