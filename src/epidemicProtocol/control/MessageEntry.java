package epidemicProtocol.control;

public class MessageEntry {

   private String message;
   private MessageStatus status;

   public MessageEntry(String message, MessageStatus status) {
      this.message = message;
      this.status = status;
   }

   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public MessageStatus getStatus() {
      return status;
   }

   public void setStatus(MessageStatus status) {
      this.status = status;
   }

   @Override
   public boolean equals(Object other) {
      if (this.getClass() != other.getClass()) {
         return false;
      }
      
      MessageEntry otherMessage = (MessageEntry) other;
      
      return this.getMessage().equals(otherMessage.getMessage());
   }
}
