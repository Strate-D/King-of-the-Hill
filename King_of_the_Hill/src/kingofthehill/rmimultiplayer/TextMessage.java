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
public class TextMessage extends Message {

    public TextMessage(int sender, Object data) {
        super.setHeader("text");
        super.setData(data);
        super.setTime(new Date());
        super.setSender(sender);
    }

}
