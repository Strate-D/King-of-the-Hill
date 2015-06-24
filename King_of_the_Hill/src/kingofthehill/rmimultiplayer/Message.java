/**
 *
 */
package kingofthehill.rmimultiplayer;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Message class for holding messages for the VoiceServer.
 *
 * @author Bas
 */
public class Message implements Serializable {

    private String header;
    private Object data;
    private Date time;
    private int sender;
    private String senderName;

    /**
     * Return the header of the Message
     *
     * @return The header of the message
     */
    public String getHeader() {
        return this.header;
    }

    /**
     * Set a new header for the Message
     *
     * @param value The new header value
     */
    public void setHeader(String value) {
        this.header = value;
    }

    /**
     * Return the send time of the Message
     *
     * @return The send time of the message
     */
    public String getTime() {
        SimpleDateFormat dt = new SimpleDateFormat("hh:mm:ss");
        return dt.format(this.time);
    }

    /**
     * Sets the send time of the Message
     *
     * @param time The new send time of the message
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * Return the clientID of the sender of the Message
     *
     * @return The clientID of the sender
     */
    public int getSender() {
        return this.sender;
    }

    /**
     * Sets a new sender for the Message
     *
     * @param sender The new sender of the message
     */
    public void setSender(int sender) {
        this.sender = sender;
    }

    /**
     * Returns the data inside the Message
     *
     * @return The data of the message
     */
    public Object getData() {
        return this.data;
    }

    /**
     * Sets new data into the Message
     *
     * @param value The new value of the message
     */
    public void setData(Object value) {
        this.data = value;
    }

    /**
     * Returns the name of the sender placed into the Message
     *
     * @return The name of the sender
     */
    public String getSenderName() {
        return this.senderName;
    }

    /**
     * Sets a new name of the sender in the Message
     *
     * @param value The new name of the sender
     */
    public void setSenderName(String value) {
        this.senderName = value;
    }
}
