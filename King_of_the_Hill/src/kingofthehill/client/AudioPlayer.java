/**
 *
 */
package kingofthehill.client;

import kingofthehill.rmimultiplayer.AudioMessage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * The AudioPlayer class for playing the recieved Audio fragments
 *
 * @author Bas
 */
public class AudioPlayer {

    private boolean playing;
    private VoiceClient parent;
    private AudioBuffer buffer;

    public AudioPlayer(VoiceClient parent) {
        this.buffer = new AudioBuffer(500000);
        this.playing = true;
        this.parent = parent;
    }

    /**
     * Starts playing recieved fragments
     */
    public void play() {
        Thread t = new Thread(() -> {

            /**
             * Find and create a speaker object
             */
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, StaticAudioAttributes.AudioDataFormat);
            SourceDataLine speakers = null;

            try {
                speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
                speakers.open(StaticAudioAttributes.AudioDataFormat);
            } catch (LineUnavailableException ex) {
                System.out.println("kingofthehill.client.AudioPlayer play(): " + ex.getMessage());
            }

            if (speakers == null) {
                this.parent.printMessage("Could not open the spreakers for playing audio");
                return;
            }

            /**
             * "Start" the speakers.
             */
            speakers.start();

            this.parent.printMessage("<< Speakers are set up and ready to play >>");

            while (playing || speakers.isOpen()) {
                /**
                 * Play the next audio clip
                 */
                byte[] data = buffer.readBuffer();
                if (data != null) {
                    speakers.write(data, 0, data.length);
                } else {
                    speakers.flush();
                }

                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    System.out.println("kingofthehill.client.AudioPlayer play(): " + ex.getMessage());
                }
            }

            /**
             * Clear the last bits from the speakers and close them
             */
            speakers.drain();
            speakers.close();
            this.parent.printMessage("<< Audio player has stopped >>");

        });

        t.start();
    }

    /**
     * Add an AudioMessage to the list for playback
     *
     * @param message The message to add
     */
    public synchronized void addAudioMessage(AudioMessage message) {
        //this.bufferedMessages.add(message);
        if (message.getData() instanceof byte[]) {
            try {
                buffer.addToBuffer((byte[]) message.getData());
            } catch (Exception ex) {
                Logger.getLogger(AudioPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Stop playing the audio
     */
    public void stopPlayback() {
        this.playing = false;
    }
}
