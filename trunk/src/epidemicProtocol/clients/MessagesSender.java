package epidemicProtocol.clients;

import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Thread that send a message from some input of a user to local
 * {@link epidemicProtocol.servers.SpreadServer}. In this way this message will be spread to another
 * computers in the local network.
 *
 * @author Lucas S Bueno
 */
public class MessagesSender extends Thread {

   private InetAddress localHost; //local host address, used to send the message in a computer
   private int targetPort; //port of the SreadServer
   private Socket socket; //socket used to make the connection with the SpreadServer
   private String message; //user message that will be sent 
   private PrintStream socketWriter; //stream used to write in the socket output

   /**
    * Initialize the object with a defined message and {@link epidemicProtocol.servers.SpreadServer}
    * port. This port was defined in {@link epidemicProtocol.view.MainWindow#SPREAD_SERVER_PORT}.
    *
    * @param message Message that will be sent to the {@link epidemicProtocol.servers.SpreadServer}
    * using a socket.
    * @param port Port used in the {@link epidemicProtocol.servers.SpreadServer}.
    */
   public MessagesSender(String message, int port) {
      this.message = message;
      this.targetPort = port;

      try {
         this.localHost = InetAddress.getLocalHost();
      } catch (Exception ex) {
         System.err.println("Error: " + ex.getMessage());
         ex.printStackTrace();
      }
   }

   /**
    * Line of execution of this thread. It wait for a user to enter a message to spread. When the
    * user enters the message, a socket is open with the targetPort defined in the constructor. So
    * the message is sent and it wait again for a new message to be sent.
    */
   @Override
   public void run() {
      /*
       * The message can't start with '#'. If it occur, delete the '#' characters 
       */
      while (!message.isEmpty() && message.charAt(0) == '#') {
         message = message.substring(1);
      }

      /*
       * If the message is empty we don't have something to send, so alert it in the user view
       */
      if (message.isEmpty()) {
         System.out.println(">> You need to enter something to send...");

         return;
      }

      /*
       * Create a new socket and write in it's output the user's message
       */
      try {
         socket = new Socket(localHost, targetPort);
         socketWriter = new PrintStream(socket.getOutputStream());
         socketWriter.println(message);
         socketWriter.flush();

         socketWriter.close();
         socket.close();
      } catch (Exception ex) {
         System.err.println("Error: " + ex.getMessage());
         ex.printStackTrace();
      }
   }
}