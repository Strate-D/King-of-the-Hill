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

    public AudioBuffer(int buffersize) {
        this.buffersize = buffersize;
        buf = new byte[buffersize];
        pointer = 0;
        endpointer = 0;
    }

    public synchronized void addToBuffer(byte[] data) {
        for (byte b : data) {
            buf[endpointer] = b;
            endpointer++;
            if (endpointer >= buffersize) {
                endpointer = 0;
            }
        }
    }

    public synchronized byte[] readBuffer() {
        byte[] data;

        if (pointer < endpointer) {
            data = Arrays.copyOfRange(buf, pointer, endpointer);
        } else if (pointer == endpointer) {
            data = new byte[] {0, 0, 0, 0};
        } else {
            data = concatArray(Arrays.copyOfRange(buf, pointer, buffersize), Arrays.copyOfRange(buf, 0, endpointer));
        }

        pointer = endpointer;

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
