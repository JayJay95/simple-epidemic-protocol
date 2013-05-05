package epidemicProtocol.control;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

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
      if((!adressesList.contains(adress)) && (!adress.equals(localHost))) {
         return adressesList.add(adress);
      }
      
      return false;
   }
   
   public String getOneAdress() {
      int listSize = adressesList.size();
      
      if (listSize != 0) {
         String oneAdress = adressesList.get(auxCount);         
         auxCount = (auxCount + 1) % listSize;
         
         return oneAdress;
      }
      
      return null;
   }
}
