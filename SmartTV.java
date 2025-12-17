package smarthome;

public class SmartTV extends SmartDevice implements Controllable, EnergyConsumer {

    private int volume = 30;
    private int channel = 1;
    private String input = "HDMI1";

    public SmartTV(String id, String name, String roomId) {
        super(id, name, roomId);
    }

    public void turnOn() {
        if (!isOnline) {
            System.out.println(name + " is offline.");
        } else {
            isOn = true;
            System.out.println(name + " turned ON - Channel " + channel + " | Volume " + volume);
        }
    }

    public void turnOff() {
        isOn = false;
        System.out.println(name + " turned OFF");
    }

    public String getStatus() {
        return String.format(
                "SmartTV: %s | %s | Volume: %d | Channel: %d | Input: %s",
                name,
                isOn ? "ON" : "OFF",
                volume,
                channel,
                input
        );
    }

    public void control(String command, Object value) {
        if (command == null) return;
        switch (command.toLowerCase()) {
            case "volume":
                setVolume((Integer) value);
                break;
            case "channel":
                setChannel((Integer) value);
                break;
            case "input":
                setInput((String) value);
                break;
            default:
                System.out.println("Unknown command: " + command);
        }
    }

    public String[] getAvailableCommands() {
        return new String[]{"volume", "channel", "input"};
    }

    public double getEnergyConsumption() {
        return isOn ? 150.0 : 5.0;
    }

    public double getDailyUsage() {
        return getEnergyConsumption() * 6.0 / 1000.0;
    }

    public void setVolume(int v) {
        volume = Math.max(0, Math.min(100, v));
        System.out.println(name + " volume set to " + volume);
    }

    public void setChannel(int c) {
        channel = Math.max(1, c);
        System.out.println(name + " switched to channel " + channel);
    }

    public void setInput(String input) {
        this.input = input;
        System.out.println(name + " input changed to " + input);
    }

    public int getVolume() {
        return volume;
    }

    public int getChannel() {
        return channel;
    }

    public String getInput() {
        return input;
    }
}
