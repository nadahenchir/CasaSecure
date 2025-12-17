package smarthome;

public class SecurityCamera extends SmartDevice implements Controllable, EnergyConsumer {

    private boolean isRecording = false;
    private String resolution = "1080p";
    private boolean nightVision = true;

    public SecurityCamera(String id, String name, String roomId) {
        super(id, name, roomId);
    }

    public void turnOn() {
        if (!isOnline) {
            System.out.println(name + " is offline.");
        } else {
            isOn = true;
            System.out.println(name + " camera activated. Resolution: " + resolution);
        }
    }

    public void turnOff() {
        isOn = false;
        isRecording = false;
        System.out.println(name + " camera deactivated");
    }

    public String getStatus() {
        return String.format(
                "SecurityCamera: %s | %s | Recording: %s | Resolution: %s | NightVision: %s",
                name,
                isOn ? "ON" : "OFF",
                isRecording ? "YES" : "NO",
                resolution,
                nightVision ? "ON" : "OFF"
        );
    }

    public void control(String command, Object value) {
        if (command == null) return;
        switch (command.toLowerCase()) {
            case "record":
                setRecording((Boolean) value);
                break;
            case "resolution":
                setResolution((String) value);
                break;
            case "nightvision":
                setNightVision((Boolean) value);
                break;
            default:
                System.out.println("Unknown command: " + command);
        }
    }

    public String[] getAvailableCommands() {
        return new String[]{"record", "resolution", "nightvision"};
    }

    public double getEnergyConsumption() {
        if (!isOn) {
            return 1.0;
        }
        return isRecording ? 15.0 : 8.0;
    }

    public double getDailyUsage() {
        return getEnergyConsumption() * 24.0 / 1000.0;
    }

    public void setRecording(boolean recording) {
        isRecording = recording;
        System.out.println(name + (recording ? " started recording" : " stopped recording"));
    }

    public void setResolution(String res) {
        resolution = res;
        System.out.println(name + " resolution set to " + res);
    }

    public void setNightVision(boolean enabled) {
        nightVision = enabled;
        System.out.println(name + " night vision " + (enabled ? "enabled" : "disabled"));
    }

    public boolean isRecording() {
        return isRecording;
    }

    public String getResolution() {
        return resolution;
    }

    public boolean hasNightVision() {
        return nightVision;
    }
}
