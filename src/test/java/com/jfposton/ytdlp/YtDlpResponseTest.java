package com.jfposton.ytdlp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.jfposton.ytdlp.mapper.VideoInfo;

public class YtDlpResponseTest {

    @Test
    public void getFormatsCanHandleLargerVideos() throws YtDlpException {
        List<VideoInfo> videoInfos = YtDlp.getVideoInfo("https://www.youtube.com/watch?v=jPTO3lcPpik");
        assertEquals(videoInfos.size(), 1);

        VideoInfo videoInfo = videoInfos.get(0);
        assertEquals("jPTO3lcPpik", videoInfo.getId());
        assertNotNull(videoInfo.getFormats());
        assertFalse(videoInfo.getFormats().isEmpty());
    }
}
