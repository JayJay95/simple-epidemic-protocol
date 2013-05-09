package epidemicProtocol.servers;

import epidemicProtocol.control.MessagesHistory;
import epidemicProtocol.control.TargetsList;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Thread that accept other computers requests and open a
 * {@link epidemicProtocol.servers.MessageSpreadThread} to handle it.
 *
 * @author Lucas S Bueno
 */
public class SpreadServer extends Thread {

   private MessagesHistory messagesHistory;
   private TargetsList targetsList;
   private int targetPort;
   private Scanner socketReader;
   private String message;
   private ServerSocket server;
   private Socket socket;

   public SpreadServer(TargetsList targetsList, int port) {
      this.messagesHistory = new MessagesHistory();
      this.targetsList = targetsList;
      this.targetPort = port;

      try {
         server = new ServerSocket(targetPort, 1000);
      } catch (Exception ex) {
         System.err.println("Error: " + ex.getMessage());
         ex.printStackTrace();
      }
   }

   @Override
   public void run() {
      System.out.println("Epidemic Protocol Online...\n");

      while (true) {
         try {
            socket = server.accept();
            socketReader = new Scanner(socket.getInputStream());
            message = socketReader.nextLine();
            InetAddress senderHost = socket.getInetAddress();

            new MessageSpreadThread(targetsList, messagesHistory, targetPort, message, senderHost).start();

            socketReader.close();
            socket.close();
         } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace();
         }
      }
   }
}