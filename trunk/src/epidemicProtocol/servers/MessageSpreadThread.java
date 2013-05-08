package epidemicProtocol.servers;

import epidemicProtocol.control.TargetsList;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Thread that make a TCP socket connection with a host and send it a message. If the connection is
 * refused, remove the host from the {@link epidemicProtocol.control.TargetsList}.
 *
 * @author Lucas S Bueno
 */
public class MessageSpreadThread extends Thread {

   private TargetsList targetsList; //list of target hosts
   private String targetHost; //local host address, used to send the message in a computer
   private int targetPort; //port of the SreadServer
   private Socket socket; //socket used to make the connection with other SpreadServer
   private String message; //user message that will be sent 
   private PrintStream socketWriter; //stream used to write in the socket output

   public MessageSpreadThread(TargetsList targetsList, String targetHost, int targetPort, String message) {
      this.targetsList = targetsList;
      this.targetHost = targetHost;
      this.targetPort = targetPort;
      this.message = message;
   }

   /**
    * This thread only try to send the specified message to a target host:port. If an error accur
    * with the creation of the socket, the target host is deleted from the
    * {@link epidemicProtocol.control.TargetsList}.
    */
   @Override
   public void run() {
      try {
         socket = new Socket(targetHost, targetPort);
         socketWriter = new PrintStream(socket.getOutputStream());
         socketWriter.println(message);
         socketWriter.flush();

         socketWriter.close();
         socket.close();
      } catch (Exception ex) {
         System.err.println("Error: " + ex.getMessage());
         System.err.println("Deleting " + targetHost + " from targets list...");
         targetsList.tryToRemove(targetHost);
      }
   }
}
