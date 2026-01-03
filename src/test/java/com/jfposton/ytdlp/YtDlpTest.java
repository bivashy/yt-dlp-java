package com.jfposton.ytdlp;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.jfposton.ytdlp.mapper.VideoFormat;
import com.jfposton.ytdlp.mapper.VideoInfo;
import com.jfposton.ytdlp.mapper.VideoThumbnail;

public class YtDlpTest {

    private static final String DIRECTORY = System.getProperty("java.io.tmpdir");
    private static final String VIDEO_URL = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
    private static final String NONE_EXISTENT_VIDEO_URL = "https://www.youtube.com/watch?v=dQw4w9WgXcZ";

    /**
     * @Test public void testUsingOwnExecutablePath() throws YtDlpException {
     *       YtDlp.setExecutablePath("/usr/bin/yt-dlp");
     *       Assert.assertNotNull(YtDlp.getVersion()); }
     */
    @Test
    public void testGetVersion() throws YtDlpException {
        Assert.assertNotNull(YtDlp.getVersion());
    }

    @Test
    public void testElapsedTime() throws YtDlpException {

        long startTime = System.nanoTime();

        YtDlpRequest request = new YtDlpRequest();
        request.setOption("version");
        YtDlpResponse response = YtDlp.execute(request);

        int elapsedTime = (int) (System.nanoTime() - startTime);

        Assert.assertTrue(elapsedTime > response.getElapsedTime());
    }

    @Test
    public void testSimulateDownload() throws YtDlpException {

        YtDlpRequest request = new YtDlpRequest();
        request.setUrl(VIDEO_URL);
        request.setOption("simulate");

        YtDlpResponse response = YtDlp.execute(request);

        Assert.assertEquals("yt-dlp " + VIDEO_URL + " --simulate", response.getCommand());
    }

    @Test
    public void testDirectory() throws YtDlpException {

        YtDlpRequest request = new YtDlpRequest(VIDEO_URL, DIRECTORY);
        request.setOption("simulate");

        YtDlpResponse response = YtDlp.execute(request);

        Assert.assertEquals(DIRECTORY, response.getDirectory());
    }

    @Test
    public void testGetVideoInfo() throws YtDlpException {
        List<VideoInfo> videoInfos = YtDlp.getVideoInfo(VIDEO_URL);
        Assert.assertNotNull(videoInfos);
        Assert.assertEquals(1, videoInfos.size());
    }

    @Test
    public void testGetFormats() throws YtDlpException {
        List<VideoFormat> formats = YtDlp.getFormats(VIDEO_URL);
        Assert.assertNotNull(formats);
        Assert.assertTrue(formats.size() > 0);
    }

    @Test
    public void testGetThumbnails() throws YtDlpException {
        List<VideoThumbnail> thumbnails = YtDlp.getThumbnails(VIDEO_URL);
        Assert.assertNotNull(thumbnails);
        Assert.assertTrue(thumbnails.size() > 0);
    }

    @Test
    public void testGetTags() throws YtDlpException {
        List<String> tags = YtDlp.getTags(VIDEO_URL);
        Assert.assertNotNull(tags);
        Assert.assertTrue(tags.size() > 0);
    }

    @Test
    public void testGetCategories() throws YtDlpException {
        List<String> categories = YtDlp.getCategories(VIDEO_URL);
        Assert.assertNotNull(categories);
        Assert.assertTrue(categories.size() > 0);
    }

    @Test(expected = YtDlpException.class)
    public void testFailGetNonExistentVideoInfo() throws YtDlpException {
        YtDlp.getVideoInfo(NONE_EXISTENT_VIDEO_URL);
    }
}
