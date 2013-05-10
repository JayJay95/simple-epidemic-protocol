package epidemicProtocol.control;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;

/**
 * Structure that store a list of local computers addresses. These address are found with the
 * broadcast messages (sent with {@link epidemicProtocol.clients.BroadcastClient}) received in
 * {@link epidemicProtocol.servers.TargetsListServer}. This list is used to spread the messages
 * received in the {@link epidemicProtocol.servers.SpreadServer}.
 *
 * @author Lucas S Bueno
 */
public class TargetsList {

   private List<String> addressesList;
   private String localHost;
   private JTextArea output;
   /**
    * variable used as index to get a address from the addressesList. It is being added in a way to
    * make a circular rotation in the list.
    */
   private int auxCount;

   /**
    * Create a TargetsList with a JTextArea parameter to show the list of IPs in the user interface.     
    */
   public TargetsList(JTextArea output) {
      this.output = output;
      addressesList = new ArrayList<>();
      auxCount = 0;

      try {
         localHost = InetAddress.getLocalHost().getHostAddress();
      } catch (Exception ex) {
         System.err.println("Error: " + ex.getMessage());         
      }
   }

   /**
    * Try to add a address in the addressesList. If the address being added is equal to the
    * localHost address, it don't need to be added.
    *
    * @param address Some address to be added to the addressesList.
    * @return True if the address was added to the addressesList successfully.
    */
   public boolean tryToAdd(String address) {
      synchronized (this) {
         boolean result;

         if ((!addressesList.contains(address)) && (!address.equals(localHost))) {
            result = addressesList.add(address);
            updateIpList();
            return result;
         }

         return false;
      }
   }

   /**
    * Try to remove the address passed in the param from the addressesList.
    *
    * @param address address to be removed.
    * @return True if the address was removed successfully.
    */
   public boolean tryToRemove(String address) {
      synchronized (this) {
         boolean result = addressesList.remove(address);
         updateIpList();

         return result;
      }
   }

   /**
    * Return one String address. It uses the auxCount attribute to make a circular rotation with the
    * addresses stored.
    *
    * @return One of the addresses of the addressList attribute.
    */
   public String getOneAddress() {
      synchronized (this) {
         int listSize = addressesList.size();

         if (listSize <= auxCount) {
            auxCount = 0;
         }

         if (listSize != 0) {
            String oneAddress = addressesList.get(auxCount);
            auxCount = (auxCount + 1) % listSize;

            return oneAddress;
         }

         return null;
      }
   }
   
   public int getSize() {
      synchronized (this) {
         return addressesList.size();
      }
   }

   /**
    * Update the user's interface with a list of IPs in this class.
    */
   private void updateIpList() {
      output.setText("");

      for (String address : addressesList) {
         output.append(address);
         output.append("\n");
      }
   }
}