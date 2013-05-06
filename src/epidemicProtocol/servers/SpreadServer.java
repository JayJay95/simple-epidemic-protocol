package epidemicProtocol.servers;

import epidemicProtocol.control.MessagesHistory;
import epidemicProtocol.control.TargetsList;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SpreadServer extends Thread {

   private MessagesHistory messagesHistory;
   private TargetsList targetsList;
   private InetAddress localHost;
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
         this.localHost = InetAddress.getLocalHost();
      } catch (Exception ex) {
         System.err.println("Error: " + ex.getMessage());
      }
   }

   @Override
   public void run() {
      try {
         server = new ServerSocket(targetPort);
      } catch (IOException ex) {
         System.err.println("Error: " + ex.getMessage());
      }

      while (true) {
         try {
            socket = server.accept();
            socketReader = new Scanner(socket.getInputStream());
            message = socketReader.nextLine();

            if (socket.getInetAddress().getHostAddress().equals(localHost.getHostAddress())) {
               System.out.print("\n>> Trying to send a new message: " + message);
            } else {
               System.out.print("\n>> You've received a new message: " + message);
            }

            socketReader.close();
            socket.close();
         } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
         }
      }
   }
}
