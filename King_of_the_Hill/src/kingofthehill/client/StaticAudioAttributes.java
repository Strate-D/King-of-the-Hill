/**
 * 
 */
package kingofthehill.client;

import javax.sound.sampled.AudioFormat;

/**
 *
 * @author Bas
 */
public class StaticAudioAttributes {

    public static AudioFormat AudioDataFormat
            = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0F, 16, 2, 4, 44100.0F, false);
}
