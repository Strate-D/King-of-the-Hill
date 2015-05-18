/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.rmimultiplayer;

import java.util.Date;

/**
 *
 * @author Bas
 */
public class AudioMessage extends Message {

    private int volgNummer;
    
    public AudioMessage(int sender, Object data, int volgnummer) {
        super.setHeader("audio");
        super.setData(data);
        super.setTime(new Date());
        super.setSender(sender);
        this.volgNummer = volgnummer;
    }
    
    public int getVolgnummer()
    {
        return volgNummer;
    }
    
    public void setVolgnummer(int nummer)
    {
        volgNummer = nummer;
    }
}
