package epidemicProtocol.control;

/**
 * Enum that represents all the possible status of a message.
 *
 * @author Lucas S Bueno
 */
public enum MessageStatus {

   /**
    * Message can be sent again to more targets
    */
   INFECTIVE,
   /**
    * Message will not be sent more
    */
   REMOVED
}
