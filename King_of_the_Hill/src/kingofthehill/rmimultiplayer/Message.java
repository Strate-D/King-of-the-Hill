/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.rmimultiplayer;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Bas
 */
public class Message implements Serializable {
    private String header;
    private Object data;
    private Date time;
    private int sender;
    private String senderName;
    
    
    public String getHeader()
    {
        return this.header;
    }
    
    public void setHeader(String value)
    {
        this.header = value;
    }
    
    public String getTime()
    {
        SimpleDateFormat dt = new SimpleDateFormat("hh:mm:ss");
        return dt.format(this.time);
    }
    
    public void setTime(Date time)
    {
        this.time = time;
    }
    
    public int getSender()
    {
        return this.sender;
    }
    
    public void setSender(int sender)
    {
        this.sender = sender;
    }
    
    public Object getData()
    {
        return this.data;
    }
    
    public void setData(Object value)
    {
        this.data = value;
    }
    
    public String getSenderName()
    {
        return this.senderName;
    }
    
    public void setSenderName(String value)
    {
        this.senderName = value;
    }
}
