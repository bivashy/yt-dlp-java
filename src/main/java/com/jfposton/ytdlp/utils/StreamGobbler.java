package com.jfposton.ytdlp.utils;

import java.io.IOException;
import java.io.InputStream;

import com.jfposton.ytdlp.StreamOutputCallback;

public class StreamGobbler extends Thread {

    private final InputStream stream;
    private final StringBuilder buffer;
    private StreamOutputCallback streamOutputCallback;

    public StreamGobbler(StringBuilder buffer, InputStream stream, StreamOutputCallback streamOutputCallback) {
        this.stream = stream;
        this.buffer = buffer;
        this.streamOutputCallback = streamOutputCallback;
        start();
    }

    public StreamGobbler(StringBuilder buffer, InputStream stream) {
        this.stream = stream;
        this.buffer = buffer;
        start();
    }

    protected void setStreamOutputCallback(StreamOutputCallback callback) {
        this.streamOutputCallback = callback;
    }

    @Override
    public void run() {
        try {
            String lineSeparator = System.lineSeparator();
            StringBuilder currentLine = new StringBuilder();
            int nextChar;
            while ((nextChar = stream.read()) != -1) {
                buffer.append((char) nextChar);
                if (endsWithLineSeparator(currentLine, lineSeparator)) {
                    String currentLineString = currentLine.toString();
                    if (streamOutputCallback != null)
                        streamOutputCallback.onOutput(currentLineString);
                    currentLine.setLength(0);
                    currentLine.append((char) nextChar);
                    continue;
                }
                currentLine.append((char) nextChar);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean endsWithLineSeparator(StringBuilder sb, String lineSeparator) {
        if (sb.length() == 0)
            return false;

        int lsLength = lineSeparator.length();

        if (sb.length() < lsLength)
            return false;

        for (int i = 0; i < lsLength; i++) {
            if (sb.charAt(sb.length() - lsLength + i) != lineSeparator.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}
