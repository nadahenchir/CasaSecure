package smarthome;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SmartLock extends SmartDevice implements Controllable {

    private boolean isLocked = true;
    private String accessCode = "1234";
    private final List<String> accessLog = new ArrayList<String>();

    public SmartLock(String id, String name, String roomId) {
        super(id, name, roomId);
    }

    public void turnOn() {
        isOn = true;
        System.out.println(name + " smart lock activated");
    }

    public void turnOff() {
        isOn = false;
        System.out.println(name + " smart lock deactivated (manual mode)");
    }

    public String getStatus() {
        return String.format(
                "SmartLock: %s | %s | %s",
                name,
                isOn ? "ACTIVE" : "MANUAL",
                isLocked ? "LOCKED" : "UNLOCKED"
        );
    }

    public void control(String command, Object value) {
        if (command == null) return;
        switch (command.toLowerCase()) {
            case "lock":
                lock();
                break;
            case "unlock":
                unlock((String) value);
                break;
            case "setcode":
                setAccessCode((String) value);
                break;
            default:
                System.out.println("Unknown command: " + command);
        }
    }

    public String[] getAvailableCommands() {
        return new String[]{"lock", "unlock", "setcode"};
    }

    public void lock() {
        isLocked = true;
        logAccess("LOCKED");
        System.out.println(name + " is now LOCKED");
    }

    public void unlock(String code) {
        if (code != null && code.equals(accessCode)) {
            isLocked = false;
            logAccess("UNLOCKED - Valid code");
            System.out.println(name + " is now UNLOCKED");
        } else {
            logAccess("FAILED ATTEMPT - Invalid code");
            System.out.println("Invalid access code for " + name);
        }
    }

    public void setAccessCode(String newCode) {
        accessCode = newCode;
        System.out.println(name + " access code updated");
    }

    private void logAccess(String entry) {
        accessLog.add(new Date().toString() + " - " + entry);
    }

    public boolean isLocked() {
        return isLocked;
    }

    public List<String> getAccessLog() {
        return accessLog;
    }
}
