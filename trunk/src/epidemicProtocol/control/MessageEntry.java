package epidemicProtocol.control;

/**
 * Structure of the messages that will be stored in the
 * {@link epidemicProtocol.control.MessagesHistory}. The message have two possible status:
 * {@link epidemicProtocol.control.MessageStatus#INFECTIVE} and
 * {@link epidemicProtocol.control.MessageStatus#REMOVED}. It starts in the
 * {@link epidemicProtocol.control.MessageStatus#INFECTIVE} status, and when it has more than
 * {@link epidemicProtocol.control.MessageEntry#MAX_NEGATIVE_COUNT} of rejections the status change
 * to {@link epidemicProtocol.control.MessageStatus#REMOVED}. A rejection occur when another
 * computer sends a negative reply to the local {@link epidemicProtocol.servers.SpreadServer}.
 *
 * @author Lucas S Bueno
 */
public class MessageEntry {

   private String message;
   private MessageStatus status;
   private int negativeSendCount;
   private int maxNegativeCount;

   /**
    * Creates a new message entry with the message and maxNegativeCount parameters. The
    * maxNegativeCount is used to change the status of the message from
    * {@link epidemicProtocol.control.MessageStatus#INFECTIVE} to
    * {@link epidemicProtocol.control.MessageStatus#REMOVED}.    
    */
   public MessageEntry(String message, int targetsListSize) {
      this.message = message;
      this.status = MessageStatus.INFECTIVE;
      negativeSendCount = 0;
      
      /*
       * calculates the maxNegativeCount based in the TargetsList size in that moment 
       */
      this.maxNegativeCount = targetsListSize / 2;

      if (this.maxNegativeCount < 2) {
         this.maxNegativeCount = 3;
      }
   }
  
   public String getMessage() {
      return message;
   }
   
   public MessageStatus getStatus() {
      return status;
   }
   
   public int getNegativeSendCount() {
      return negativeSendCount;
   }

   /**
    * Increases the rejection count of this message. When this count exceeds
    * {@link epidemicProtocol.control.MessageEntry#MAX_NEGATIVE_COUNT} the status of the message is
    * changed to {@link epidemicProtocol.control.MessageStatus#REMOVED}.
    */
   public void addNegativeSendCount() {
      this.negativeSendCount++;

      if (negativeSendCount > maxNegativeCount) {
         this.status = MessageStatus.REMOVED;
      }
   }

   /**
    * Override the standard equals method to make the message attribute the search key.
    *
    * @param other
    */
   @Override
   public boolean equals(Object other) {
      if (this.getClass() != other.getClass()) {
         return false;
      }

      MessageEntry otherMessage = (MessageEntry) other;

      return this.getMessage().equals(otherMessage.getMessage());
   }
}