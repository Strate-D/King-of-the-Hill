/**
 */
package kingofthehill.client;

import java.io.ByteArrayOutputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import kingofthehill.rmimultiplayer.AudioMessage;

/**
 * AudioCapture class captures audio from the microphone and make packages of
 * data from it
 *
 * @author Bas
 */
public class AudioCapture {

    private boolean stopped;
    private VoiceClient client;

    public AudioCapture(VoiceClient client) {
        this.stopped = false;
        this.client = client;
    }

    /**
     * Start capturing the audio from the microphone
     */
    public void startCapture() {
        this.stopped = false;
        Thread t = new Thread(() -> {
            /**
             * Open the microphone input line
             */
            TargetDataLine line;
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, StaticAudioAttributes.AudioDataFormat);
            if (!AudioSystem.isLineSupported(info)) {
                this.client.printMessage("Something went wrong while setting up the audio format");
                return;
            }

            try {
                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(StaticAudioAttributes.AudioDataFormat);
            } catch (LineUnavailableException | NullPointerException ex) {
                this.client.printMessage("Could not open the microphone line");
                System.out.println("kingofthehill.client.AudioCapture startCapture(): " + ex.getMessage());
                return;
            }

            int numBytesRead;
            byte[] data = new byte[line.getBufferSize() / 4];

            /**
             * Begin audio capture.
             */
            line.start();

            /**
             * A number indicating the order in with the audio needs to be
             * played back
             */
            int volgnummer = 0;

            /**
             * Here, stopped is a global boolean set by another thread.
             */
            this.client.printMessage("<< Started recording >>");

            while (!stopped) {
                /**
                 * Read the next chunk of data from the TargetDataLine.
                 */
                ByteArrayOutputStream out = new ByteArrayOutputStream();

                numBytesRead = line.read(data, 0, data.length);
                /**
                 * Save this chunk of data.
                 */
                out.write(data, 0, numBytesRead);

                /**
                 * Writes a message with the data of the audio
                 */
                AudioMessage message = new AudioMessage(client.getClientId(), out.toByteArray(), volgnummer);
                client.sendMessage(message); 
                /*this.client.printMessage("<< New package created and send >>");*/

                volgnummer++;
            }

            client.sendMessage(new AudioMessage(client.getClientId(), "reset", volgnummer));

            line.close();

            this.client.printMessage("<< Audio recording stopped >>");
        });
        t.start();
    }

    /**
     * Stop the capture of audio from the microphone
     */
    public void stopCapture() {
        this.stopped = true;
    }

    /**
     * Check if the audio capture is active
     *
     * @return true if the audio capture is active; otherwise false
     */
    public boolean isRunning() {
        return !this.stopped;
    }
}
