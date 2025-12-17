package smarthome;

import java.time.LocalTime;

public class AutomationRule {

    private final String id;
    private String name;
    private String triggerType;        // "motion" or "time"
    private String triggerDeviceId;    // For motion-based triggers
    private String actionDeviceId;
    private String action;             // e.g. "turn_on", "turn_off"
    private LocalTime scheduledTime;   // For time-based triggers
    private String days;               // Days for time-based rules
    private boolean enabled;

    // Constructor for motion-based rules (OLD - keep for backward compatibility)
    public AutomationRule(String id,
                          String name,
                          String triggerDeviceId,
                          String triggerCondition,
                          String actionDeviceId,
                          String action) {

        this.id = id;
        this.name = name;
        this.triggerType = triggerCondition.toLowerCase();
        this.triggerDeviceId = triggerDeviceId;
        this.actionDeviceId = actionDeviceId;
        this.action = action;
        this.enabled = true;
        this.scheduledTime = null;
        this.days = null;
    }

    // Constructor for time-based rules (NEW) - THIS IS THE ONE YOU'RE MISSING!
    public AutomationRule(String id,
                          String name,
                          String actionDeviceId,
                          String action,
                          LocalTime scheduledTime,
                          String days) {

        this.id = id;
        this.name = name;
        this.triggerType = "time";
        this.actionDeviceId = actionDeviceId;
        this.action = action;
        this.scheduledTime = scheduledTime;
        this.days = days;
        this.enabled = true;
        this.triggerDeviceId = null;
    }

    public boolean checkTrigger(SmartDevice device, LocalTime currentTime) {
        if (!enabled) return false;

        switch (triggerType.toLowerCase()) {
            case "motion":
            case "motion_detected":
                if (device instanceof MotionSensor) {
                    return ((MotionSensor) device).isMotionDetected();
                }
                break;
                
            case "time":
                if (scheduledTime != null && currentTime != null) {
                    return currentTime.getHour() == scheduledTime.getHour()
                        && currentTime.getMinute() == scheduledTime.getMinute();
                }
                break;
        }
        
        return false;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTriggerDeviceId() {
        return triggerDeviceId;
    }

    public String getTriggerCondition() {
        return triggerType;
    }

    public String getActionDeviceId() {
        return actionDeviceId;
    }

    public String getAction() {
        return action;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        if (triggerType.equals("time")) {
            return String.format(
                    "Rule: %s | %s %s at %s on %s | %s",
                    name,
                    action.replace("_", " ").toUpperCase(),
                    actionDeviceId,
                    scheduledTime,
                    days,
                    enabled ? "ENABLED" : "DISABLED"
            );
        } else {
            return String.format(
                    "Rule: %s | IF %s on %s THEN %s on %s | %s",
                    name,
                    triggerType,
                    triggerDeviceId,
                    action,
                    actionDeviceId,
                    enabled ? "ENABLED" : "DISABLED"
            );
        }
    }
}