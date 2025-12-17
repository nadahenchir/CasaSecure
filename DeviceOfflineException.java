package smarthome;

public class DeviceOfflineException extends Exception {

    private final String deviceName;

    public DeviceOfflineException(String deviceName) {
        super("Device is offline: " + deviceName);
        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        return deviceName;
    }
}
