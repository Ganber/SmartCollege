package com.example.smartcollege.Response;

public class StartImageResponse {
    private String jsonrpc;
    private Response result;

    public String getDeviceId() {
        return Long.toString(result.deviceId.getId());
    }

    public String getImageUrl() {
        return result.imageUrl;
    }

    public String getCreationDate() {
        return result.creationDate;
    }


    public class Response{
        private IdResponse deviceId;
        private String imageUrl;
        private String creationDate;
    }
}
