package smarthome;

public interface Schedulable {
    void scheduleAction(String action, String time);
    void cancelSchedule(String schedule);
    String[] getScheduledActions();
}
