package epidemicProtocol.clients;

import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class MessagesSender extends Thread {

   private InetAddress localHost;
   private int targetPort;
   private Socket socket;
   private String message;
   private PrintStream socketWriter;

   public MessagesSender(int port) {
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
         sleep(10000);
      } catch (Exception ex) {
         System.err.println("Error: " + ex.getMessage());
      }

      Scanner input = new Scanner(System.in);
      System.out.println("Epidemic Protocol Online...\n");

      while (true) {
         System.out.print(">> Enter a message to spread: ");

         while ((message = input.nextLine()).isEmpty()) {
            System.out.print(">> Enter a message to spread: ");
         }

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
