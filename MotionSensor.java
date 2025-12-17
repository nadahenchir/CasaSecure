package smarthome;

public class MotionSensor extends SmartDevice implements EnergyConsumer {

    private boolean motionDetected = false;
    private long lastMotionTime = 0L;
    private int sensitivityLevel = 5; // 1–10

    public MotionSensor(String id, String name, String roomId) {
        super(id, name, roomId);
    }

    public void turnOn() {
        if (!isOnline) {
            System.out.println(name + " is offline.");
        } else {
            isOn = true;
            System.out.println(name + " sensor activated. Sensitivity: " + sensitivityLevel);
        }
    }

    public void turnOff() {
        isOn = false;
        motionDetected = false;
        System.out.println(name + " sensor deactivated");
    }

    public String getStatus() {
        return String.format(
                "MotionSensor: %s | %s | Motion: %s | Sensitivity: %d",
                name,
                isOn ? "ACTIVE" : "INACTIVE",
                motionDetected ? "DETECTED" : "NONE",
                sensitivityLevel
        );
    }

    public double getEnergyConsumption() {
        return isOn ? 2.0 : 0.5;
    }

    public double getDailyUsage() {
        return getEnergyConsumption() * 24.0 / 1000.0;
    }

    public void detectMotion() {
        if (isOn) {
            motionDetected = true;
            lastMotionTime = System.currentTimeMillis();
            System.out.println("ALERT: Motion detected by " + name + "!");
        }
    }

    public void clearMotion() {
        motionDetected = false;
    }

    public void setSensitivity(int level) {
        sensitivityLevel = Math.max(1, Math.min(10, level));
    }

    public boolean isMotionDetected() {
        return motionDetected;
    }

    public long getLastMotionTime() {
        return lastMotionTime;
    }

    public int getSensitivityLevel() {
        return sensitivityLevel;
    }
}
