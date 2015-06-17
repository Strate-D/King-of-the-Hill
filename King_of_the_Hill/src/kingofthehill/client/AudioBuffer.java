/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kingofthehill.client;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 *
 * @author Bas
 */
public class AudioBuffer {

    private byte[] buf;
    private int pointer;
    private int endpointer;
    private int buffersize = 10;
    private boolean isWriting;
    private boolean isReading;

    public AudioBuffer(int buffersize) {
        this.buffersize = buffersize;
        buf = new byte[buffersize];
        pointer = 0;
        endpointer = 0;
        isWriting = false;
        isReading = false;
    }

    public synchronized void addToBuffer(byte[] data) throws Exception {
        while(isReading)
        {
            Thread.sleep(1);
        }
        
        isWriting = true;
        for (byte b : data) {
            buf[endpointer] = b;
            endpointer++;
            if(endpointer == pointer)
            {
                throw new Exception("Buffer overflow");
            }
            if (endpointer >= buffersize) {
                endpointer = 0;
            }
        }
        
        isWriting = false;
    }

    public synchronized byte[] readBuffer() throws Exception {
        byte[] data;
        
        while(isWriting)
        {
            Thread.sleep(1);
        }

        isReading = true;
        if (pointer < endpointer) {
            data = Arrays.copyOfRange(buf, pointer, endpointer);
        } else if (pointer == endpointer) {
            throw new Exception("Buffer is empty");
        } else {
            data = concatArray(Arrays.copyOfRange(buf, pointer, buffersize), Arrays.copyOfRange(buf, 0, endpointer));
        }

        pointer = endpointer;
        
        isReading = false;

        return data;
    }

    private byte[] concatArray(byte[] a, byte[] b) {
        int aLen = a.length;
        int bLen = b.length;
        byte[] c = new byte[aLen + bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }
}
