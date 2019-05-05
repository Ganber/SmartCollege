package com.example.smartcollege.Response;

import java.util.List;

public class DeviceResponse {
    private String jsonrpc;
    private Response result;

    public class Response{
        private IdResponse productId;
        private IdResponse deviceId;
        private IdResponse gatewayId;
        private String serial;
        private String type;
        private String name;
        private List<String>  groups;
        private List<AttributesResponse> attributes;
        private String parentId;
        private List<String> children;
        private String status;
        private String partitions;
        private String protocol;
        private String batteryStatus;
        private int batteryLevel;
        private int graceTimeSeconds;
        private String sensorTriggerModeWhenSystemArmed;
        private String sensorTriggerModeWhenSystemCustomized;
        private String sensorTriggerModeWhenSystemDisarmed;
        private String masterId;
        private WifiSettingsResponse wifiSettings;
        private String  wifiSettingsChanged;
        private int level;
        private String room;
        private String introspection;
        private boolean active;
        private boolean recordVideoOnAlert;
        private boolean newlyAdded;
        private boolean defaultCamera;
        private boolean builtInCamera;
        private boolean builtInSiren;
        private boolean slaveModeAvailable;
        private List<String> deviceProductGroups;
        private boolean dimmer;
        private boolean doorLock;
        private boolean parentDevice;
        private String doorLockSettings;
        private String deviceExternalSerial;
        private String state;
        private boolean general;
        private List<String> deviceFaults;
        private String markedForDelete;
        private ManufacturerProductResponse manufacturerProduct;
        private String multiLevelSensorValue;
        private List<String> multiLevelSensorValues;
        private String functionalityGroup;
    }

    public long getProductId() {
        return result.productId.getId();
    }

    public long getDeviceId() {
        return result.deviceId.getId();
    }

    public long getGatewayId() {
        return result.gatewayId.getId();
    }

    public String getSerial() {
        return result.serial;
    }

    public String getType() {
        return result.type;
    }

    public String getName() {
        return result.name;
    }

    public List<String> getGroups() {
        return result.groups;
    }

    public List<AttributesResponse> getAttributes() {
        return result.attributes;
    }

    public String getParentId() {
        return result.parentId;
    }

    public List<String> getChildren() {
        return result.children;
    }

    public String getStatus() {
        return result.status;
    }

    public String getPartitions() {
        return result.partitions;
    }

    public String getProtocol() {
        return result.protocol;
    }

    public String getBatteryStatus() {
        return result.batteryStatus;
    }

    public int getBatteryLevel() {
        return result.batteryLevel;
    }

    public int getGraceTimeSeconds() {
        return result.graceTimeSeconds;
    }

    public String getSensorTriggerModeWhenSystemArmed() {
        return result.sensorTriggerModeWhenSystemArmed;
    }

    public String getSensorTriggerModeWhenSystemCustomized() {
        return result.sensorTriggerModeWhenSystemCustomized;
    }

    public String getSensorTriggerModeWhenSystemDisarmed() {
        return result.sensorTriggerModeWhenSystemDisarmed;
    }

    public String getMasterId() {
        return result.masterId;
    }

    public WifiSettingsResponse getWifiSettings() {
        return result.wifiSettings;
    }

    public String getWifiSettingsChanged() {
        return result.wifiSettingsChanged;
    }

    public int getLevel() {
        return result.level;
    }

    public String getRoom() {
        return result.room;
    }

    public String getIntrospection() {
        return result.introspection;
    }

    public boolean isActive() {
        return result.active;
    }

    public boolean isRecordVideoOnAlert() {
        return result.recordVideoOnAlert;
    }

    public boolean isNewlyAdded() {
        return result.newlyAdded;
    }

    public boolean isDefaultCamera() {
        return result.defaultCamera;
    }

    public boolean isBuiltInCamera() {
        return result.builtInCamera;
    }

    public boolean isBuiltInSiren() {
        return result.builtInSiren;
    }

    public boolean isSlaveModeAvailable() {
        return result.slaveModeAvailable;
    }

    public List<String> getDeviceProductGroups() {
        return result.deviceProductGroups;
    }

    public boolean isDimmer() {
        return result.dimmer;
    }

    public boolean isDoorLock() {
        return result.doorLock;
    }

    public boolean isParentDevice() {
        return result.parentDevice;
    }

    public String getDoorLockSettings() {
        return result.doorLockSettings;
    }

    public String getDeviceExternalSerial() {
        return result.deviceExternalSerial;
    }

    public String getState() {
        return result.state;
    }

    public boolean isGeneral() {
        return result.general;
    }

    public List<String> getDeviceFaults() {
        return result.deviceFaults;
    }

    public String getMarkedForDelete() {
        return result.markedForDelete;
    }

    public ManufacturerProductResponse getManufacturerProduct() {
        return result.manufacturerProduct;
    }

    public String getMultiLevelSensorValue() {
        return result.multiLevelSensorValue;
    }

    public List<String> getMultiLevelSensorValues() {
        return result.multiLevelSensorValues;
    }

    public String getFunctionalityGroup() {
        return result.functionalityGroup;
    }


}
