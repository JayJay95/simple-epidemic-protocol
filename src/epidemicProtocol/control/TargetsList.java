package epidemicProtocol.control;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Struct that store a list of local computers adresses. These adress are found with the broadcast
 * messages (sent with {@link epidemicProtocol.clients.BroadcastClient}) received in
 * {@link epidemicProtocol.servers.TargetsListServer}. This list is used to spred the messages
 * received in the {@link epidemicProtocol.servers.SpreadServer}.
 *
 * @author Lucas S Bueno
 */
public class TargetsList {

   private List<String> adressesList;
   private String localHost;
   private int auxCount;

   public TargetsList() {
      adressesList = new ArrayList<>();
      auxCount = 0;

      try {
         localHost = InetAddress.getLocalHost().getHostAddress();
      } catch (Exception ex) {
         System.err.println("Error: " + ex.getMessage());
      }
   }

   public boolean tryToAdd(String adress) {
      synchronized (this) {
         if ((!adressesList.contains(adress)) && (!adress.equals(localHost))) {
            return adressesList.add(adress);
         }

         return false;
      }
   }

   /**
    * Return one String adress. It uses the auxCount attribute to make a circular rotation with the
    * adresses stored.
    *
    * @return One of the adresses of the adressList attribute.
    */
   public String getOneAdress() {
      synchronized (this) {
         int listSize = adressesList.size();

         if (listSize != 0) {
            String oneAdress = adressesList.get(auxCount);
            auxCount = (auxCount + 1) % listSize;

            return oneAdress;
         }

         return null;
      }
   }
}
