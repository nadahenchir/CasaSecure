package smarthome;

import java.util.ArrayList;
import java.util.List;

public class Thermostat extends SmartDevice implements Controllable, EnergyConsumer, Schedulable {

    private double targetTemperature = 22.0; // Celsius
    private double currentTemperature = 21.0;
    private String mode = "auto";
    private final List<String> schedules = new ArrayList<String>();

    public Thermostat(String id, String name, String roomId) {
        super(id, name, roomId);
    }

    public void turnOn() {
        if (!isOnline) {
            System.out.println(name + " is offline.");
        } else {
            isOn = true;
            System.out.println(name + " activated in " + mode + " mode. Target: " + targetTemperature + " C");
        }
    }

    public void turnOff() {
        isOn = false;
        System.out.println(name + " turned OFF");
    }

    public String getStatus() {
        return String.format(
                "Thermostat: %s | %s | Current: %.1f C | Target: %.1f C | Mode: %s",
                name,
                isOn ? "ON" : "OFF",
                currentTemperature,
                targetTemperature,
                mode
        );
    }

    public void control(String command, Object value) {
        if (command == null) return;
        switch (command.toLowerCase()) {
            case "temperature":
                setTargetTemperature((Double) value);
                break;
            case "mode":
                setMode((String) value);
                break;
            default:
                System.out.println("Unknown command: " + command);
        }
    }

    public String[] getAvailableCommands() {
        return new String[]{"temperature", "mode"};
    }

    public double getEnergyConsumption() {
        if (!isOn) {
            return 0.0;
        }
        double diff = Math.abs(targetTemperature - currentTemperature);
        return diff * 100.0;
    }

    public double getDailyUsage() {
        return getEnergyConsumption() * 12.0 / 1000.0;
    }

    public void scheduleAction(String action, String time) {
        String entry = action + " at " + time;
        schedules.add(entry);
        System.out.println("Scheduled: " + name + " - " + entry);
    }

    public void cancelSchedule(String schedule) {
        schedules.remove(schedule);
    }

    public String[] getScheduledActions() {
        return schedules.toArray(new String[0]);
    }

    public void setTargetTemperature(double temp) {
        targetTemperature = Math.max(15.0, Math.min(30.0, temp));
        System.out.println(name + " target temperature set to " + targetTemperature + " C");
    }

    public void setMode(String mode) {
        this.mode = mode;
        System.out.println(name + " mode set to " + mode);
    }

    public void setCurrentTemperature(double temp) {
        this.currentTemperature = temp;
    }

    public double getTargetTemperature() {
        return targetTemperature;
    }

    public double getCurrentTemperature() {
        return currentTemperature;
    }

    public String getMode() {
        return mode;
    }
}
