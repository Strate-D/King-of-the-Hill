/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.Encryption;

import kingofthehill.Encryption.AES;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rick
 */
public class AESTest {

    @Test
    public void TestAES() throws Exception {

        String text1 = "Hallo Rick";
        String text2 = "Döner";
        String text1Enc = AES.encrypt(text1);
        String text2Enc = AES.encrypt(text2);
        assertEquals(AES.decrypt(text1Enc), text1);
        assertEquals(AES.decrypt(text2Enc), text2);

    }

}


