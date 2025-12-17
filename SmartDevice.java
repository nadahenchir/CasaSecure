package smarthome;

public abstract class SmartDevice {

    protected String id;
    protected String name;
    protected String location;
    protected boolean isOn;
    protected boolean isOnline;

    public SmartDevice(String id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.isOn = false;
        this.isOnline = true;
    }

    public abstract void turnOn();

    public abstract void turnOff();

    public abstract String getStatus();

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public boolean isOn() {
        return isOn;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        this.isOnline = online;
    }

    @Override
    public String toString() {
        return name + " (" + location + ") - " + (isOn ? "ON" : "OFF");
    }
}
