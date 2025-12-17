package smarthome;

public class DeviceNotFoundException extends Exception {

    private final String deviceId;

    public DeviceNotFoundException(String deviceId) {
        super("Device not found: " + deviceId);
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }
}
