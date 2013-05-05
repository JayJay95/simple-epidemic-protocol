package epidemicProtocol.control;

import java.util.ArrayList;
import java.util.List;

public class MessagesHistory {
    
    private List<MessageEntry> messagesList;
    private final int MAX_LIST_SIZE = 100;

    public MessagesHistory() {
        this.messagesList = new ArrayList<>();
    }
    
    private boolean addMessage(MessageEntry message) {
        if (messagesList.size() >= MAX_LIST_SIZE) {
            messagesList.remove(0);
            
            return messagesList.add(message);
        } else {
            return messagesList.add(message);
        }
    }
    
    public boolean tryToAdd (MessageEntry message) {
        if (!messagesList.contains(message)) {            
            return addMessage(message);
        }
        
        return false;
    }
}
