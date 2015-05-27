/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author Bas
 */
public class AudioPlayer {

    private ArrayList<AudioMessage> bufferedMessages;
    private boolean playing;
    private int lastPlayedClip;
    private VoiceClient parent;

    public AudioPlayer(VoiceClient parent) {
        this.bufferedMessages = new ArrayList<>();
        this.playing = true;
        this.parent = parent;
    }

    public void play() throws Exception {
        Thread t = new Thread(() -> {

            // Find and create a speaker object
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, StaticAudioAttributes.AudioDataFormat);
            SourceDataLine speakers = null;

            try {
                speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
                speakers.open(StaticAudioAttributes.AudioDataFormat);
            } catch (LineUnavailableException ex) {
            }

            if (speakers == null) {
                this.parent.printMessage("Could not open the spreakers for playing audio");
                return;
            }

            // "Start" the speakers. 
            speakers.start();

            this.resetMessageCounter();

            this.parent.printMessage("<< Speakers are set up and ready to play >>");

            while (playing) {
                // Find the next clip
                ArrayList<Integer> canRemove = new ArrayList();

                for (int i = 0; i < bufferedMessages.size(); i++) {
                    if (bufferedMessages.get(i).getVolgnummer() == lastPlayedClip + 1) {
                        // Write an audio clip to the spreakers
                        Object dat = bufferedMessages.get(i).getData();
                        if (dat instanceof String) {
                            this.resetMessageCounter();
                        } else {
                            byte[] data = (byte[]) dat;
                            speakers.write(data, 0, data.length);
                            lastPlayedClip++;
                            canRemove.add(i);
                            break;
                        }
                    }
                }

                for (int i = 0; i < canRemove.size(); i++) {
                    bufferedMessages.remove((int) canRemove.get(i));
                }

                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AudioPlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            // Clear the last bits from the speakers and close them
            speakers.drain();
            speakers.close();

        });

        t.start();
    }

    public synchronized void addAudioMessage(AudioMessage message) {
        this.bufferedMessages.add(message);
    }

    public void stopPlayback() {
        this.playing = false;
    }

    public void resetMessageCounter() {
        this.lastPlayedClip = -1;
    }
}
