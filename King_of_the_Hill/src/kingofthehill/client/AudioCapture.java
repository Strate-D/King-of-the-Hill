/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.client;

import java.io.ByteArrayOutputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import kingofthehill.rmimultiplayer.AudioMessage;

/**
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

    public void startCapture() throws Exception {
        this.stopped = false;
        Thread t = new Thread(() -> {
            // Open the microphone input line
            TargetDataLine line = null;
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, StaticAudioAttributes.AudioDataFormat);
            if (!AudioSystem.isLineSupported(info)) {
                this.client.printMessage("Something went wrong while setting up the audio format");
                return;
            }

            try {
                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(StaticAudioAttributes.AudioDataFormat);
            } catch (LineUnavailableException ex) {
                this.client.printMessage("Could not open the microphone line");
                return;
            }

            if (line == null) {
                this.client.printMessage("Could not open the microphone line");
                return;
            }

            int numBytesRead;
            byte[] data = new byte[line.getBufferSize() / 5];

            // Begin audio capture.
            line.start();

            // A number indicating the order in with the audio needs to be played back
            int volgnummer = 0;

            // Here, stopped is a global boolean set by another thread.
            this.client.printMessage("<< Started recording >>");

            while (!stopped /*&& line.isRunning()*/) {
                this.client.printMessage("<< Recording ... >>");
                // Read the next chunk of data from the TargetDataLine.
                ByteArrayOutputStream out = new ByteArrayOutputStream();

                numBytesRead
                        = line.read(data, 0, data.length);
                // Save this chunk of data.
                out.write(data, 0, numBytesRead);

                // Writes a message with the data of the audio
                AudioMessage message = new AudioMessage(client.getClientId(), out.toByteArray(), volgnummer);
                client.sendMessage(message);

                volgnummer++;
            }

            client.sendMessage(new AudioMessage(client.getClientId(), "reset", volgnummer));

            line.close();

            this.client.printMessage("<< Audio recording stopped >>");
        });
        t.start();
    }

    public void stopCapture() {
        this.stopped = true;
    }
}
