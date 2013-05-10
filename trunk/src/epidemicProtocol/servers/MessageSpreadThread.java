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
            /*
             * the user tries to send a message that already was received before
             * just print a message alerting it
             */
            System.out.println(">> This message is already known...");
         } else {
            /*
             * the user want to send a new message
             * get one address from the targetsList and try to send the message
             */
            System.out.println(">> Sending a new message: " + message);
            targetHost = targetsList.getOneAddress();

            if (targetHost != null) {
               sendMessage();
            }
         }
      } else {
         if (message.charAt(0) == '#') {
            /*
             * the server received a response from other server            
             */
            int response = Character.getNumericValue(message.charAt(1));
            String responseMessage = message.substring(2);

            if (response == 0) {
               /*
                * the response was negative, so this server sent a message to that server, 
                * but it already known the message. Add a negative count in this message and try 
                * to send the same massage to another computer
                */
               messagesHistory.addMessageEntryNegativeCount(responseMessage);
               targetHost = targetsList.getOneAddress();

               if (targetHost != null && messagesHistory.getMessageEntryStatus(responseMessage)) {
                  message = responseMessage;
                  sendMessage(); //sending message to another computer
               }
            } else {
               /*
                * the response was positive, so this server sent a message to that server and it
                * didn't know the message yet. Just try to send the message to another computer.
                */
               targetHost = targetsList.getOneAddress();

               if (targetHost != null && messagesHistory.getMessageEntryStatus(responseMessage)) {
                  message = responseMessage;
                  sendMessage(); //sending message to another computer
               }
            }
         } else {
            /*
             * someone is sending a message to this server. This can be a new message or not.
             */
            if (!messagesHistory.tryToAdd(message, targetsList.getSize())) {
               /*
                * the message is old, so send a negative response to the sender host.
                */
               targetHost = senderHost.getHostAddress();
               message = "#0" + message;
               sendMessage(); //sending a negative response to the sender host 
            } else {
               /*
                * the message is new, so send the message to another computer and a positive
                * response to the sender host.
                */
               System.out.println(">> You've received a new message: " + message);
               targetHost = targetsList.getOneAddress();

               if (targetHost != null) {
                  sendMessage(); //sending message to another computer
               }

               targetHost = senderHost.getHostAddress();
               message = "#1" + message;
               sendMessage(); //sending a positive response to the sender host 
            }
         }
      }
   }

   /**
    * Creates a socket with the targetHost and the targetPort. So send a message in this socket.
    */
   private void sendMessage() {
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