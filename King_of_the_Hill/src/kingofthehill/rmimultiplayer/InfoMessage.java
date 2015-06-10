/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.rmimultiplayer;

/**
 * An InfoMessage contains information from the server towards the client. The
 * class extends the Message class
 *
 * @author Bas
 */
public class InfoMessage extends Message {

    private String define;

    public InfoMessage(Object information, String defined) {
        super.setHeader("info");
        super.setData(information);
        super.setTime(null);
        super.setSender(-10);
        this.define = defined;
    }

    /**
     * This is an extra header inside the message for speaking to parts of the
     * client application
     *
     * @return The header value inside the message
     */
    public String getDefine() {
        return this.define;
    }
}
