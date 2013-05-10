package epidemicProtocol.control;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that maintain a history of messages received in the local computer. It has a limit of
 * {@link epidemicProtocol.control.MessagesHistory#MAX_LIST_SIZE} messages stored.
 *
 * @author Lucas S Bueno
 */
public class MessagesHistory {

   private List<MessageEntry> messagesList;
   private final int MAX_LIST_SIZE = 200;

   public MessagesHistory() {
      this.messagesList = new ArrayList<>();
   }

   /**
    * Add a message in the messagesList. If the current messagesList has
    * {@link epidemicProtocol.control.MessagesHistory#MAX_LIST_SIZE} messages, the first message is
    * removed to the new message be added.
    *
    * @param message Message to be added.
    * @return True if the message was added.
    */
   private boolean addMessage(String message, int maxNegativeCount) {
      if (messagesList.size() >= MAX_LIST_SIZE) {
         messagesList.remove(0);

         return messagesList.add(new MessageEntry(message, maxNegativeCount));
      } else {
         return messagesList.add(new MessageEntry(message, maxNegativeCount));
      }
   }

   private boolean contains(String message) {
      MessageEntry messageWrapped = new MessageEntry(message, 0);

      return messagesList.contains(messageWrapped);
   }

   /**
    *
    * @param message
    * @param maxNegativeCount
    * @return
    */
   public boolean tryToAdd(String message, int maxNegativeCount) {
      synchronized (this) {
         if (!contains(message)) {
            return addMessage(message, maxNegativeCount);
         }

         return false;
      }
   }

   /**
    * Get the status of a message.
    * 
    * @return true for {@link epidemicProtocol.control.MessageStatus#INFECTIVE} and false for
    * {@link epidemicProtocol.control.MessageStatus#REMOVED} or in case of a message not found.
    */
   public boolean getMessageEntryStatus(String message) {
      synchronized (this) {
         if (!contains(message)) {
            return false;
         }

         MessageEntry messageWrapped = new MessageEntry(message, 0);
         int index = messagesList.indexOf(messageWrapped);
         MessageStatus status = messagesList.get(index).getStatus();

         return (status == MessageStatus.INFECTIVE) ? true : false;
      }
   }

   /**
    * Add a negative send count to the specified message.     
    */
   public void addMessageEntryNegativeCount(String message) {
      synchronized (this) {
         if (!contains(message)) {
            return;
         }

         MessageEntry messageWrapped = new MessageEntry(message, 0);
         int index = messagesList.indexOf(messageWrapped);
         MessageEntry messageEntry = messagesList.get(index);
         messageEntry.addNegativeSendCount();
      }
   }
}