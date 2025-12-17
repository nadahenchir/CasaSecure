package smarthome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home {

    private String name;
    private String address;
    private final Map<String, Room> rooms;

    public Home(String name, String address) {
        this.name = name;
        this.address = address;
        this.rooms = new HashMap<String, Room>();
    }

    public void addRoom(Room room) {
        rooms.put(room.getId(), room);
        System.out.println("Added room: " + room.getName());
    }

    public Room getRoom(String roomId) {
        return rooms.get(roomId);
    }

    public List<Room> getAllRooms() {
        return new ArrayList<Room>(rooms.values());
    }

    public SmartDevice findDevice(String deviceId) {
        for (Room room : rooms.values()) {
            SmartDevice d = room.getDevice(deviceId);
            if (d != null) {
                return d;
            }
        }
        return null;
    }

    public List<SmartDevice> getAllDevices() {
        List<SmartDevice> devices = new ArrayList<SmartDevice>();
        for (Room room : rooms.values()) {
            devices.addAll(room.getAllDevices());
        }
        return devices;
    }

    public void showFullStatus() {
        System.out.println("\n===============================");
        System.out.println(" HOME: " + name);
        System.out.println(" Address: " + address);
        System.out.println("===============================");
        for (Room room : rooms.values()) {
            room.showStatus();
        }
    }

    public double getTotalEnergyConsumption() {
        double total = 0.0;
        for (SmartDevice device : getAllDevices()) {
            if (device instanceof EnergyConsumer) {
                EnergyConsumer ec = (EnergyConsumer) device;
                total += ec.getEnergyConsumption();
            }
        }
        return total;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
