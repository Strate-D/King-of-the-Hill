/**
 * 
 */
package kingofthehill.rmimultiplayer;

import java.util.Date;

/**
 * Holds a TextMessage to be send over for the VoiceServer. Extend the Message class
 * @author Bas
 */
public class TextMessage extends Message {

    public TextMessage(int sender, Object data) {
        super.setHeader("text");
        super.setData(data);
        super.setTime(new Date());
        super.setSender(sender);
    }

}
