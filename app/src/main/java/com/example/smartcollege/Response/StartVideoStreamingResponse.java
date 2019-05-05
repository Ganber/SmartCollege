package com.example.smartcollege.Response;

public class StartVideoStreamingResponse {
    private String jsonrpc;
    private Response result;

    public String getStreamId() {
        return result.streamId;
    }

    public String getVideoServerIp() {
        return result.videoServerIp;
    }

    public String getStreamUrl() {
        return result.streamUrl;
    }

    public String getStreamRtspUrl() {
        return result.streamRtspUrl;
    }

    public String getStreamHlsUrl() {
        return result.streamHlsUrl;
    }

    public int getVideoPort() {
        return result.videoPort;
    }

    public int getAudioPort() {
        return result.audioPort;
    }

    public class Response{
        private String streamId;
        private String videoServerIp;
        private String streamUrl;
        private String streamRtspUrl;
        private String streamHlsUrl;
        private int videoPort;
        private int audioPort;
    }
}
