/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.client;

import javax.sound.sampled.AudioFormat;

/**
 *
 * @author Bas
 */
public class StaticAudioAttributes {
    public static AudioFormat AudioDataFormat = 
            //new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 16000.0F, 8, 2, 4, 16000.0F, false);
            //new AudioFormat(8000.0f, 8, 1, true, false);
            new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,44100.0F, 16, 2, 4, 44100.0F, false);
}
