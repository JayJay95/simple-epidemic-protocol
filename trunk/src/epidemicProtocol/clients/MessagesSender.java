package epidemicProtocol.clients;

import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Thread that send a message from some input of a user to local
 * {@link epidemicProtocol.servers.SpreadServer}. In this way this message will be spread to another
 * computers in the local network.
 *
 * @author Lucas S Bueno
 */
public class MessagesSender extends Thread {

   private InetAddress localHost; //local host adress, used to send the message in a computer
   private int targetPort; //port of the SreadServer
   private Socket socket; //socket used to make the connection with the SpreadServer
   private String message; //user message that will be sent 
   private PrintStream socketWriter; //stream used to write in the socket output

   /**
    * Initialize the object with a defined {@link epidemicProtocol.servers.SpreadServer} port. This
    * port was defined in {@link epidemicProtocol.Main}.
    *
    * @param port Port used in the {@link epidemicProtocol.servers.SpreadServer}
    */
   public MessagesSender(int port) {
      this.targetPort = port;

      try {
         this.localHost = InetAddress.getLocalHost();
      } catch (Exception ex) {
         System.err.println("Error: " + ex.getMessage());
      }
   }

   /**
    * Line of execution of this thread. It wait for a user to enter a message to spread. When the
    * user enters the message, a socket is open with the targetPort defined in the constructor. So
    * the message is sent and it wait again for a new message to be sent.
    */
   @Override
   public void run() {
      try {
         sleep(10000);
      } catch (Exception ex) {
         System.err.println("Error: " + ex.getMessage());
      }

      Scanner input = new Scanner(System.in);
      System.out.println("Epidemic Protocol Online...\n");

      /*
       * Main loop, that take the user's input and send the messages to the SpredServer
       */
      while (true) {
         System.out.print(">> Enter a message to spread: ");

         while ((message = input.nextLine()).isEmpty()) {
            System.out.print(">> Enter a message to spread: ");
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
         }
      }
   }
}
