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
   private final int MAX_LIST_SIZE = 100;

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
   private boolean addMessage(String message) {
      if (messagesList.size() >= MAX_LIST_SIZE) {
         messagesList.remove(0);

         return messagesList.add(new MessageEntry(message));
      } else {
         return messagesList.add(new MessageEntry(message));
      }
   }

   public boolean contains(String message) {
      MessageEntry messageWrapped = new MessageEntry(message);

      return messagesList.contains(messageWrapped);
   }

   public boolean tryToAdd(String message) {
      if (!contains(message)) {
         return addMessage(message);
      }

      return false;
   }

   public MessageEntry getMessageEntry(String message) {
      MessageEntry messageWrapped = new MessageEntry(message);
      int index = messagesList.indexOf(messageWrapped);

      return messagesList.get(index);
   }
}
