package com.example.smartcollege;

public class VideoSession {
    private String streamId, videoServerIp, streamUrl, streamRtspUrl, streamFlsUrl, streamHlsUrl;
    private int videoPort, audioPort;

    public VideoSession(String streamId, String videoServerIp, String streamUrl, String streamRtspUrl, String streamFlsUrl, String streamHlsUrl, int videoPort, int audioPort) {
        this.streamId = streamId;
        this.videoServerIp = videoServerIp;
        this.streamUrl = streamUrl;
        this.streamRtspUrl = streamRtspUrl;
        this.streamFlsUrl = streamFlsUrl;
        this.streamHlsUrl = streamHlsUrl;
        this.videoPort = videoPort;
        this.audioPort = audioPort;
    }

}
