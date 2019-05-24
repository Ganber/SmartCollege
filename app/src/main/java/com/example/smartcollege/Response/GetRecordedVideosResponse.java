package com.example.smartcollege.Response;

import java.util.ArrayList;

public class GetRecordedVideosResponse {
    private String jsonrpc;
    private Response result;


    public boolean isSuccess() {
        return result.success;
    }

    public String getFaultCode() {
        return result.faultCode;
    }

    public String getFaultDescription() {
        return result.faultDescription;
    }

    public ArrayList<VideoRecord> getRecordedVideos() {
        return result.recordedVideos;
    }

    public class Response{
        private boolean success;
        private String faultCode;
        private String faultDescription;
        private ArrayList<VideoRecord> recordedVideos;
    }
}
