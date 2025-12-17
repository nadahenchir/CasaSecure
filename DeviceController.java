package smarthome;

import java.util.ArrayList;
import java.util.List;

public class DeviceController {

    private final Home home;
    private final List<AutomationRule> rules;

    public DeviceController(Home home) {
        this.home = home;
        this.rules = new ArrayList<AutomationRule>();
    }

    public Home getHome() {
        return home;
    }

    public void addRule(AutomationRule rule) {
        rules.add(rule);
        System.out.println("Added rule: " + rule);
    }

    public List<AutomationRule> getRules() {
        return rules;
    }

    // Find device by ID (keep for backward compatibility)
    public SmartDevice findDeviceById(String id) throws DeviceNotFoundException {
        SmartDevice device = home.findDevice(id);
        if (device == null) {
            throw new DeviceNotFoundException(id);
        }
        return device;
    }

    // NEW: Find device by name
    public SmartDevice findDeviceByName(String name) throws DeviceNotFoundException {
        for (SmartDevice device : home.getAllDevices()) {
            if (device.getName().equalsIgnoreCase(name)) {
                return device;
            }
        }
        throw new DeviceNotFoundException(name);
    }

    public void listAllDevices() {
        System.out.println("\n--- All Devices ---");
        for (SmartDevice device : home.getAllDevices()) {
            System.out.println(device.getName() + " [" + device.getId() + "] -> " + device.getStatus());
        }
    }

    // Execute command using device name
    public void executeOnDeviceByName(String deviceName, String command, Object value)
            throws DeviceNotFoundException, DeviceOfflineException, InvalidOperationException {

        SmartDevice device = findDeviceByName(deviceName);

        if (!device.isOnline()) {
            throw new DeviceOfflineException(device.getName());
        }

        if (command.equalsIgnoreCase("turn_on")) {
            device.turnOn();
            return;
        } else if (command.equalsIgnoreCase("turn_off")) {
            device.turnOff();
            return;
        }

        if (device instanceof Controllable) {
            Controllable c = (Controllable) device;
            c.control(command, value);
        } else {
            throw new InvalidOperationException(command, "Device '" + device.getName() + "' is not controllable");
        }
    }

    // Keep the old method for backward compatibility with automation rules
    public void executeOnDevice(String deviceId, String command, Object value)
            throws DeviceNotFoundException, DeviceOfflineException, InvalidOperationException {

        SmartDevice device = findDeviceById(deviceId);

        if (!device.isOnline()) {
            throw new DeviceOfflineException(device.getName());
        }

        if (command.equalsIgnoreCase("turn_on")) {
            device.turnOn();
            return;
        } else if (command.equalsIgnoreCase("turn_off")) {
            device.turnOff();
            return;
        }

        if (device instanceof Controllable) {
            Controllable c = (Controllable) device;
            c.control(command, value);
        } else {
            throw new InvalidOperationException(command, "Device is not controllable");
        }
    }

    // Automation event handling
    public void handleEvent(String triggerDeviceId, String condition) {
        for (AutomationRule rule : rules) {
            if (!rule.isEnabled()) {
                continue;
            }
            if (rule.getTriggerDeviceId().equals(triggerDeviceId)
                    && rule.getTriggerCondition().equalsIgnoreCase(condition)) {
                try {
                    SmartDevice target = findDeviceById(rule.getActionDeviceId());
                    if (rule.getAction().equalsIgnoreCase("turn_on")) {
                        target.turnOn();
                    } else if (rule.getAction().equalsIgnoreCase("turn_off")) {
                        target.turnOff();
                    }
                    System.out.println("Automation executed: " + rule.getName());
                } catch (Exception e) {
                    System.out.println("Failed to execute rule " + rule.getName() + ": " + e.getMessage());
                }
            }
        }
    }
}