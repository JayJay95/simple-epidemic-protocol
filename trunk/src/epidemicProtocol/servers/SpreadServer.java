package epidemicProtocol.servers;

import epidemicProtocol.control.MessageEntry;
import epidemicProtocol.control.MessagesHistory;
import epidemicProtocol.control.TargetsList;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Lucas S Bueno
 */
public class SpreadServer extends Thread {

   private MessagesHistory messagesHistory;
   private TargetsList targetsList;
   private InetAddress localHost;
   private String targetAddress;
   private int targetPort;
   private Scanner socketReader;
   private String message;
   private ServerSocket server;
   private Socket socket;
   private final int SPREAD_LEVEL = 2;
   private PrintStream socketWriter; 

   public SpreadServer(TargetsList targetsList, int port) {
      this.messagesHistory = new MessagesHistory();
      this.targetsList = targetsList;
      this.targetPort = port;

      try {
         this.localHost = InetAddress.getLocalHost();
      } catch (Exception ex) {
         System.err.println("Error: " + ex.getMessage());
         ex.printStackTrace();
      }
   }

   @Override
   public void run() {
      System.out.println("Epidemic Protocol Online...\n");

      try {
         server = new ServerSocket(targetPort);
      } catch (IOException ex) {
         System.err.println("Error: " + ex.getMessage());
         ex.printStackTrace();
      }

      while (true) {
         try {
            socket = server.accept();
            socketReader = new Scanner(socket.getInputStream());
            message = socketReader.nextLine();
            
            socket.close();

            if (socket.getInetAddress().getHostAddress().equals(localHost.getHostAddress())) {
               if (!messagesHistory.tryToAdd(message)) {
                  System.out.println(">> This message was already sent...");
               } else {
                  System.out.println(">> Sending a new message: " + message);

                  for (int i = 0; i < SPREAD_LEVEL; i++) {
                     targetAddress = targetsList.getOneAddress();

                     if (targetAddress != null) {
                        sendMessage();
                     }
                  }
               }
            } else {
               if (message.charAt(0) == '#') {
                  int response = Character.getNumericValue(message.charAt(1));
                  String responseMessage = message.substring(2);

                  if (response == 0) {
                     MessageEntry messageEntry = messagesHistory.getMessageEntry(responseMessage);
                     messageEntry.addNegativeSendCount();
                  } else {
                     for (int i = 0; i < SPREAD_LEVEL; i++) {
                        targetAddress = targetsList.getOneAddress();

                        if (targetAddress != null) {
                           sendMessage();
                        }
                     }
                  }
               } else {
                  if (!messagesHistory.tryToAdd(message)) {
                     targetAddress = socket.getInetAddress().getHostAddress();
                     message = "#0" + message;
                     sendMessage();
                  } else {
                     System.out.println(">> You've received a new message: " + message);

                     for (int i = 0; i < SPREAD_LEVEL; i++) {
                        targetAddress = targetsList.getOneAddress();

                        if (targetAddress != null) {
                           sendMessage();
                        }
                     }

                     message = "#1" + message;
                     sendMessage();
                  }

               }
            }

            socketReader.close();
            socket.close();
         } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace();
         }
      }
   }

   public void sendMessage() {
      try {         
         Socket socket = new Socket(targetAddress, targetPort);
         socketWriter = new PrintStream(socket.getOutputStream());
         socketWriter.println(message);
         socketWriter.flush();

         socketWriter.close();
         socket.close();
      } catch (Exception ex) {
         System.err.println("Error: " + ex.getMessage());
         System.err.println("Deleting " + targetAddress + " from targets list...");
         targetsList.tryToRemove(targetAddress);
         ex.printStackTrace();
      }
   }
}
