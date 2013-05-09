package epidemicProtocol.servers;

import epidemicProtocol.control.MessagesHistory;
import epidemicProtocol.control.TargetsList;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Thread that make a TCP socket connection with a host and send it some message depending on the
 * message received as parameter. If the connection is refused, remove the host from the
 * {@link epidemicProtocol.control.TargetsList}.
 *
 * @author Lucas S Bueno
 */
public class MessageSpreadThread extends Thread {

   private InetAddress senderHost; //address of the request connection
   private InetAddress localHost;
   private TargetsList targetsList; //list of target hosts
   private MessagesHistory messagesHistory; //list of messages received in this node
   private String targetHost; //address used to send the message to other computer
   private int targetPort; //port used in SreadServer
   private Socket socket; //socket used to make the connection with other SpreadServer
   private String message; //user message that will be sent 
   private PrintStream socketWriter; //stream used to write in the socket output

   public MessageSpreadThread(TargetsList targetsList, MessagesHistory messagesHistory,
                              int targetPort, String message, InetAddress senderHost) {
      this.targetsList = targetsList;
      this.messagesHistory = messagesHistory;
      this.targetPort = targetPort;
      this.message = message;
      this.senderHost = senderHost;

      try {
         this.localHost = InetAddress.getLocalHost();
      } catch (Exception ex) {
         System.err.println("Error: " + ex.getMessage());
         ex.printStackTrace();
      }
   }

   /**
    * This thread verify the message passed as parameter. There are 6 possible situations: 1- the
    * message was sent by this computer, but it is already stored in the messagesList. 2- the
    * message was sent by this computer and it will be sent to another computer present in
    * {@link epidemicProtocol.control.TargetsList}. 3- message starting with "#0". 4- message
    * starting with "#1". 5- the message was sent from another computer and this node already have
    * this message. 6- the message was sent from another computer and will be sent to another
    * computer. If an error occur with the creation of a communication socket, the target host is
    * deleted from the {@link epidemicProtocol.control.TargetsList}.
    */
   @Override
   public void run() {
      if (senderHost.getHostAddress().equals(localHost.getHostAddress())) {
         if (!messagesHistory.tryToAdd(message, targetsList.getSize())) {
            System.out.println(">> This message is already known...");
         } else {
            System.out.println(">> Sending a new message: " + message);
            targetHost = targetsList.getOneAddress();

            if (targetHost != null) {
               sendMessage();
            }
         }
      } else {
         if (message.charAt(0) == '#') {
            int response = Character.getNumericValue(message.charAt(1));
            String responseMessage = message.substring(2);

            if (response == 0) {
               messagesHistory.addMessageEntryNegativeCount(responseMessage);
               targetHost = targetsList.getOneAddress();

               if (targetHost != null && messagesHistory.getMessageEntryStatus(responseMessage)) {
                  message = responseMessage;
                  sendMessage();
               }
            } else {
               targetHost = targetsList.getOneAddress();

               if (targetHost != null && messagesHistory.getMessageEntryStatus(responseMessage)) {
                  message = responseMessage;
                  sendMessage();
               }
            }
         } else {
            if (!messagesHistory.tryToAdd(message, targetsList.getSize())) {
               targetHost = senderHost.getHostAddress();
               message = "#0" + message;
               sendMessage();
            } else {
               System.out.println(">> You've received a new message: " + message);
               targetHost = targetsList.getOneAddress();

               if (targetHost != null) {
                  sendMessage();
               }

               targetHost = senderHost.getHostAddress();
               message = "#1" + message;
               sendMessage();
            }
         }
      }
   }

   public void sendMessage() {
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
         ex.printStackTrace();
      }
   }
}