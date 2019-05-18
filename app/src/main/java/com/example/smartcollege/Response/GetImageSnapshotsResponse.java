package com.example.smartcollege.Response;

public class GetImageSnapshotsResponse {
    private String jsonrpc;
    private Response result;

    public String getSuccess() {
        return result.success;
    }

    public String getFaultCode() {
        return result.faultCode;
    }

    public String getFaultDescription() {
        return result.faultDescription;
    }

    public String getUrl() {
        return result.url;
    }


    public class Response{
        private String success;
        private String faultCode;
        private String faultDescription;
        private String url;
    }
}
