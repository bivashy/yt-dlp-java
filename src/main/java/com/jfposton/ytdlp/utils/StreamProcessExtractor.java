package com.jfposton.ytdlp.utils;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfposton.ytdlp.DownloadProgressCallback;
import com.jfposton.ytdlp.StreamOutputCallback;

public class StreamProcessExtractor extends StreamGobbler {
    private static final String GROUP_PERCENT = "percent";
    private static final String GROUP_MINUTES = "minutes";
    private static final String GROUP_SECONDS = "seconds";
    private final DownloadProgressCallback progressCallback;
    private final StreamOutputCallback streamOutputCallback;

    private Pattern p = Pattern.compile(
            "\\[download\\]\\s+(?<percent>\\d+\\.\\d)% .* ETA (?<minutes>\\d+):(?<seconds>\\d+)");

    public StreamProcessExtractor(
            StringBuilder buffer, InputStream stream,
            DownloadProgressCallback progressCallback,
            StreamOutputCallback streamOutputCallback) {
        super(buffer, stream);
        this.progressCallback = progressCallback;
        this.streamOutputCallback = streamOutputCallback;
        setStreamOutputCallback(this::processLine);
    }

    private void processLine(String line) {
        if (streamOutputCallback != null)
            streamOutputCallback.onOutput(line);
        detectProgressInLine(line);
    }

    private void detectProgressInLine(String line) {
        if (progressCallback == null)
            return;
        Matcher m = p.matcher(line);
        if (m.matches()) {
            float progress = Float.parseFloat(m.group(GROUP_PERCENT));
            long eta = convertToSeconds(m.group(GROUP_MINUTES), m.group(GROUP_SECONDS));
            progressCallback.onProgressUpdate(progress, eta);
        }
    }

    private int convertToSeconds(String minutes, String seconds) {
        return Integer.parseInt(minutes) * 60 + Integer.parseInt(seconds);
    }

}
