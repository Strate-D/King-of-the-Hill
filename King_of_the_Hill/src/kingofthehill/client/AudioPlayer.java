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

    private ArrayList<AudioMessage> bufferedMessages;
    private boolean playing;
    //private int lastPlayedClip;
    private VoiceClient parent;

    public AudioPlayer(VoiceClient parent) {
        this.bufferedMessages = new ArrayList<>();
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

            //this.resetMessageCounter();

            this.parent.printMessage("<< Speakers are set up and ready to play >>");

            while (playing) {
//                /**
//                 * Find the next clip
//                 */
//                ArrayList<Integer> canRemove = new ArrayList();
//
//                for (int i = 0; i < bufferedMessages.size(); i++) {
//                    if (bufferedMessages.get(i).getFollowupnumber() == lastPlayedClip + 1) {
//                        /**
//                         * Write an audio clip to the spreakers
//                         */
//                        Object dat = bufferedMessages.get(i).getData();
//                        if (dat instanceof String) {
//                            this.resetMessageCounter();
//                        } else {
//                            byte[] data = (byte[]) dat;
//                            speakers.write(data, 0, data.length);
//                            lastPlayedClip++;
//                            canRemove.add(i);
//                            break;
//                        }
//                    }
//                }
//
//                for (int i = 0; i < canRemove.size(); i++) {
//                    bufferedMessages.remove((int) canRemove.get(i));
//                }
                
                /**
                 * Play the next audio clip
                 */
                if(bufferedMessages.size() > 0)
                {
                    /**
                     * Write it to the speakers
                     */
                    Object dat = bufferedMessages.get(0).getData();
                    if(dat instanceof String)
                    {
                        /**
                         * Old message kind, ignore
                         */
                        System.out.println("Microphone of sender reset");
                    }
                    else
                    {
                        byte[] data = (byte[]) dat;
                        speakers.write(data, 0, data.length);
                        bufferedMessages.remove(0);
                        System.out.println("Message: " + dat.toString());
                    }
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

        });

        t.start();
    }

    /**
     * Add an AudioMessage to the list for playback
     *
     * @param message The message to add
     */
    public synchronized void addAudioMessage(AudioMessage message) {
        this.bufferedMessages.add(message);
    }

    /**
     * Stop playing the audio
     */
    public void stopPlayback() {
        this.playing = false;
    }

    /**
     * Reset the counter for finding the next AudioMessage
     */
//    public void resetMessageCounter() {
//        this.lastPlayedClip = -1;
//    }
}
