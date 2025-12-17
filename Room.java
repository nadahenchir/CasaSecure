package smarthome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {

    private final String id;
    private String name;
    private final Map<String, SmartDevice> devices;

    public Room(String id, String name) {
        this.id = id;
        this.name = name;
        this.devices = new HashMap<String, SmartDevice>();
    }

    public void addDevice(SmartDevice device) {
        devices.put(device.getId(), device);
        System.out.println("Added " + device.getName() + " to " + name);
    }

    public void removeDevice(String deviceId) {
        SmartDevice removed = devices.remove(deviceId);
        if (removed != null) {
            System.out.println("Removed " + removed.getName() + " from " + name);
        }
    }

    public SmartDevice getDevice(String deviceId) {
        return devices.get(deviceId);
    }

    public List<SmartDevice> getAllDevices() {
        return new ArrayList<SmartDevice>(devices.values());
    }

    public void turnAllOn() {
        System.out.println("\n--- Turning on all devices in " + name + " ---");
        for (SmartDevice device : devices.values()) {
            device.turnOn();
        }
    }

    public void turnAllOff() {
        System.out.println("\n--- Turning off all devices in " + name + " ---");
        for (SmartDevice device : devices.values()) {
            device.turnOff();
        }
    }

    public void showStatus() {
        System.out.println("\n=== " + name + " Status ===");
        for (SmartDevice device : devices.values()) {
            System.out.println("  " + device.getStatus());
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDeviceCount() {
        return devices.size();
    }
}
