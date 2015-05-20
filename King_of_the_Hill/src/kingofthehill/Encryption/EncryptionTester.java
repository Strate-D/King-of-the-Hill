/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.Encryption;

/**
 *
 * @author Dennis
 */
public class EncryptionTester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        String text1 = "Hallo Rick";
        String text2 = "Döner";
        String text1Enc = AES.encrypt(text1);
        String text2Enc = AES.encrypt(text2);
        
        System.out.println("Encryption test 1");
        System.out.println("-------------------------");
        System.out.println("Text 1: " + text1);
        System.out.println("Encrypted: " + text1Enc);
        System.out.println("Decrypted: " + AES.decrypt(AES.encrypt(text1)));
        System.out.println("");
        System.out.println("");
        System.out.println("Encryption test 2");
        System.out.println("-------------------------");
        System.out.println("Text 1: " + text2);
        System.out.println("Encrypted: " + text2Enc);
        System.out.println("Decrypted: " + AES.decrypt(AES.encrypt(text2)));
        
        
    }
    
}
