package smarthome;

public interface Controllable {
    void control(String command, Object value);
    String[] getAvailableCommands();
}
