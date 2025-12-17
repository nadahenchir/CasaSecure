package smarthome;

import java.util.ArrayList;
import java.util.List;

public class Light extends SmartDevice implements Controllable, EnergyConsumer, Schedulable {

    private int brightness = 100;
    private String color = "#FFFFFF";
    private final List<String> schedules = new ArrayList<String>();

    public Light(String id, String name, String roomId) {
        super(id, name, roomId);
    }

    public void turnOn() {
        if (!isOnline) {
            System.out.println(name + " is offline and cannot be turned on.");
        } else {
            isOn = true;
            System.out.println(name + " turned ON at " + brightness + "% brightness");
        }
    }

    public void turnOff() {
        isOn = false;
        System.out.println(name + " turned OFF");
    }

    public String getStatus() {
        return String.format(
                "Light: %s | %s | Brightness: %d%% | Color: %s",
                name,
                isOn ? "ON" : "OFF",
                brightness,
                color
        );
    }

    public void control(String command, Object value) {
        if (command == null) {
            return;
        }
        switch (command.toLowerCase()) {
            case "brightness":
                setBrightness((Integer) value);
                break;
            case "color":
                setColor((String) value);
                break;
            default:
                System.out.println("Unknown command: " + command);
        }
    }

    public String[] getAvailableCommands() {
        return new String[]{"brightness", "color"};
    }

    public double getEnergyConsumption() {
        return isOn ? (brightness / 100.0) * 10.0 : 0.0;
    }

    public double getDailyUsage() {
        return getEnergyConsumption() * 8.0 / 1000.0;
    }

    public void scheduleAction(String action, String time) {
        String entry = action + " at " + time;
        schedules.add(entry);
        System.out.println("Scheduled: " + name + " will " + entry);
    }

    public void cancelSchedule(String schedule) {
        schedules.remove(schedule);
    }

    public String[] getScheduledActions() {
        return schedules.toArray(new String[0]);
    }

    public void setBrightness(int value) {
        brightness = Math.max(0, Math.min(100, value));
        System.out.println(name + " brightness set to " + brightness + "%");
    }

    public void setColor(String color) {
        this.color = color;
        System.out.println(name + " color set to " + color);
    }

    public int getBrightness() {
        return brightness;
    }

    public String getColor() {
        return color;
    }
}
