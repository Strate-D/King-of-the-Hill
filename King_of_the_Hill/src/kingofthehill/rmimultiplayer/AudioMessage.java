/**
 * 
 */
package kingofthehill.rmimultiplayer;

import java.util.Date;

/**
 *
 * @author Bas
 */
public class AudioMessage extends Message {

    private int followupnumber;
    
    /**
     * Constructor for creating a AudioMessage object. 
     * AudioMessage extends the Message class
     * @param sender The ID of the sender
     * @param data The data inside the message, this is most of the time a byte[]
     * @param followupnumber A followupnumber to play the data back in the correct order
     */
    public AudioMessage(int sender, Object data, int followupnumber) {
        super.setHeader("audio");
        super.setData(data);
        super.setTime(new Date());
        super.setSender(sender);
        this.followupnumber = followupnumber;
    }
    
    /**
     * Returns the followupnumber
     * @return The followupnumber
     */
    public int getFollowupnumber()
    {
        return followupnumber;
    }
    
    /**
     * Sets a new followupnumber
     * @param nummer The new followupnumber
     */
    public void setFollowupnumber(int nummer)
    {
        followupnumber = nummer;
    }
}
