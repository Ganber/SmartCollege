package com.example.smartcollege.Request;

import java.util.Date;

public class VideoRecordsRequest {
    public Long gatewayId;//, deviceId = null;
    //public Date startDate, endDate;
    //public String triggerType, pagination;

    public VideoRecordsRequest(long gatewayID) {
        this.gatewayId = gatewayID;
    }



}
