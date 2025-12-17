package smarthome;

import java.util.Scanner;
import java.time.LocalTime;

public class SmartHomeSimulator {

    private static int deviceCounter = 5;
    private static int roomCounter = 3;
    private static int ruleCounter = 2;

    public static void main(String[] args) {
        Home home = new Home("CasaSecure", "Rue Mohamed 5");
        DeviceController controller = new DeviceController(home);

        // Create initial rooms
        Room livingRoom = new Room("room1", "Living Room");
        Room bedroom = new Room("room2", "Bedroom");
        home.addRoom(livingRoom);
        home.addRoom(bedroom);

        // Add some initial devices
        Light light1 = new Light("d1", "Living Room Light", "room1");
        SmartTV tv1 = new SmartTV("d2", "Living Room TV", "room1");
        Thermostat thermostat = new Thermostat("d3", "Main Thermostat", "room2");
        MotionSensor motionSensor = new MotionSensor("d4", "Hallway Motion Sensor", "room2");

        livingRoom.addDevice(light1);
        livingRoom.addDevice(tv1);
        bedroom.addDevice(thermostat);
        bedroom.addDevice(motionSensor);

        // Example automation
        AutomationRule rule = new AutomationRule(
                "r1",
                "Hall Motion turns on Living Room Light",
                "d4",
                "motion_detected",
                "d1",
                "turn_on"
        );
        controller.addRule(rule);

        runMenu(controller);
    }

    private static void runMenu(DeviceController controller) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n==== SMART HOME MENU ====");
            System.out.println("1. Show home status");
            System.out.println("2. List all devices");
            System.out.println("3. Add room");
            System.out.println("4. Add device");
            System.out.println("5. Turn device ON");
            System.out.println("6. Turn device OFF");
            System.out.println("7. Control device");
            System.out.println("8. Simulate motion detection");
            System.out.println("9. Show automation rules");
            System.out.println("10. Add automation rule");
            System.out.println("11. Show energy consumption");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        controller.getHome().showFullStatus();
                        break;
                        
                    case "2":
                        controller.listAllDevices();
                        break;
                        
                    case "3":
                        addRoom(controller, scanner);
                        break;
                        
                    case "4":
                        addDeviceToRoom(controller, scanner);
                        break;
                        
                    case "5":
                        turnDeviceOn(controller, scanner);
                        break;
                        
                    case "6":
                        turnDeviceOff(controller, scanner);
                        break;
                        
                    case "7":
                        controlDevice(controller, scanner);
                        break;
                        
                    case "8":
                        simulateMotion(controller, scanner);
                        break;
                        
                    case "9":
                        System.out.println("\n--- Automation Rules ---");
                        if (controller.getRules().isEmpty()) {
                            System.out.println("No automation rules configured.");
                        } else {
                            for (AutomationRule r : controller.getRules()) {
                                System.out.println(r);
                            }
                        }
                        break;
                        
                    case "10":
                        addAutomationRule(controller, scanner);
                        break;
                        
                    case "11":
                        showEnergyConsumption(controller);
                        break;
                        
                    case "0":
                        running = false;
                        break;
                        
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (DeviceNotFoundException e) {
                System.out.println("✗ Error: " + e.getMessage());
            } catch (DeviceOfflineException e) {
                System.out.println("✗ Error: " + e.getMessage());
            } catch (InvalidOperationException e) {
                System.out.println("✗ Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("✗ Unexpected error: " + e.getMessage());
            }
        }

        scanner.close();
        System.out.println("Exiting Smart Home Simulator.");
    }

    private static void addRoom(DeviceController controller, Scanner scanner) {
        System.out.print("Enter room name: ");
        String roomName = scanner.nextLine().trim();
        
        // Check if room already exists (case-insensitive)
        for (Room existingRoom : controller.getHome().getAllRooms()) {
            if (existingRoom.getName().equalsIgnoreCase(roomName)) {
                System.out.println("✗ Room '" + roomName + "' already exists!");
                return;
            }
        }
        
        String roomId = "room" + roomCounter++;
        Room newRoom = new Room(roomId, roomName);
        controller.getHome().addRoom(newRoom);
        System.out.println("✓ Room '" + roomName + "' created with ID: " + roomId);
        
        // Ask if user wants to add devices
        System.out.print("\nWould you like to add devices to this room now? (y/n): ");
        String addDevices = scanner.nextLine();
        
        if (addDevices.equalsIgnoreCase("y")) {
            boolean addingDevices = true;
            while (addingDevices) {
                try {
                    addDeviceToRoomDirect(controller, newRoom, scanner);
                    
                    System.out.print("\nAdd another device to " + roomName + "? (y/n): ");
                    String another = scanner.nextLine();
                    if (!another.equalsIgnoreCase("y")) {
                        addingDevices = false;
                    }
                } catch (DeviceNotFoundException e) {
                    System.out.println("✗ Error: " + e.getMessage());
                }
            }
        }
    }

    private static void addDeviceToRoom(DeviceController controller, Scanner scanner) 
            throws DeviceNotFoundException {
        
        // Show available rooms
        System.out.println("\n--- Available Rooms ---");
        for (Room room : controller.getHome().getAllRooms()) {
            System.out.println(room.getName());
        }
        
        System.out.print("\nEnter room name: ");
        String roomName = scanner.nextLine().trim();
        
        // Find room by name (case-insensitive)
        Room room = null;
        for (Room r : controller.getHome().getAllRooms()) {
            if (r.getName().equalsIgnoreCase(roomName)) {
                room = r;
                break;
            }
        }
        
        if (room == null) {
            System.out.println("✗ Room '" + roomName + "' not found!");
            return;
        }
        
        addDeviceToRoomDirect(controller, room, scanner);
    }

    private static void addDeviceToRoomDirect(DeviceController controller, Room room, Scanner scanner) 
            throws DeviceNotFoundException {
        
        // Check if room is Front Door
        boolean isFrontDoor = room.getName().equalsIgnoreCase("Front Door") || 
                              room.getName().equalsIgnoreCase("FrontDoor") ||
                              room.getName().equalsIgnoreCase("Entrance");
        
        System.out.println("\nAvailable device types:");
        
        if (isFrontDoor) {
            // Front Door ONLY has Security Camera and Smart Lock
            System.out.println("5. Security Camera");
            System.out.println("6. Smart Lock");
        } else {
            // Other rooms have all devices listed, but 5 & 6 will throw exception
            System.out.println("1. Light");
            System.out.println("2. Smart TV");
            System.out.println("3. Thermostat");
            System.out.println("4. Motion Sensor");
            System.out.println("5. Security Camera");
            System.out.println("6. Smart Lock");
        }
        
        System.out.print("Choose device type: ");
        String typeChoice = scanner.nextLine().trim();
        
        // Front Door restrictions - check BEFORE asking for name
        if (isFrontDoor) {
            if (!typeChoice.equals("5") && !typeChoice.equals("6")) {
                throw new DeviceNotFoundException(getDeviceTypeName(typeChoice) + 
                    " not found in " + room.getName() + " (only Security Camera and Smart Lock allowed)");
            }
        } else {
            if (typeChoice.equals("5") || typeChoice.equals("6")) {
                throw new DeviceNotFoundException(getDeviceTypeName(typeChoice) + 
                    " not found in " + room.getName() + " (only available for Front Door)");
            }
        }
        
        System.out.print("Enter device name: ");
        String deviceName = scanner.nextLine().trim();
        
        // Check for duplicate device name in the same room (case-insensitive)
        for (SmartDevice existingDevice : room.getAllDevices()) {
            if (existingDevice.getName().equalsIgnoreCase(deviceName)) {
                System.out.println("✗ Device '" + deviceName + "' already exists in " + room.getName() + "!");
                return;
            }
        }
        
        String deviceId = "d" + deviceCounter++;
        SmartDevice device = null;
        
        switch (typeChoice) {
            case "1":
                device = new Light(deviceId, deviceName, room.getId());
                break;
            case "2":
                device = new SmartTV(deviceId, deviceName, room.getId());
                break;
            case "3":
                device = new Thermostat(deviceId, deviceName, room.getId());
                break;
            case "4":
                device = new MotionSensor(deviceId, deviceName, room.getId());
                break;
            case "5":
                device = new SecurityCamera(deviceId, deviceName, room.getId());
                break;
            case "6":
                device = new SmartLock(deviceId, deviceName, room.getId());
                break;
            default:
                System.out.println("✗ Invalid device type!");
                return;
        }
        
        room.addDevice(device);
        System.out.println("✓ Device '" + deviceName + "' added to " + room.getName() + " [ID: " + deviceId + "]");
    }

    private static void turnDeviceOn(DeviceController controller, Scanner scanner)
            throws DeviceNotFoundException, DeviceOfflineException, InvalidOperationException {
        
        showDeviceSelectionMenu(controller);
        System.out.print("Enter device number, name, or ID: ");
        String input = scanner.nextLine().trim();
        
        SmartDevice device = findDevice(controller, input);
        controller.executeOnDeviceByName(device.getName(), "turn_on", null);
    }

    private static void turnDeviceOff(DeviceController controller, Scanner scanner)
            throws DeviceNotFoundException, DeviceOfflineException, InvalidOperationException {
        
        showDeviceSelectionMenu(controller);
        System.out.print("Enter device number, name, or ID: ");
        String input = scanner.nextLine().trim();
        
        SmartDevice device = findDevice(controller, input);
        controller.executeOnDeviceByName(device.getName(), "turn_off", null);
    }

    private static void controlDevice(DeviceController controller, Scanner scanner) 
            throws DeviceNotFoundException, DeviceOfflineException, InvalidOperationException {
        
        showDeviceSelectionMenu(controller);
        System.out.print("Enter device number, name, or ID: ");
        String input = scanner.nextLine().trim();
        
        SmartDevice device = findDevice(controller, input);
        
        System.out.print("Enter command (e.g., brightness, color, volume, temperature): ");
        String cmd = scanner.nextLine().trim();
        
        System.out.print("Enter value (string or number): ");
        String val = scanner.nextLine().trim();
        
        Object valueObj = parseValue(val);
        controller.executeOnDeviceByName(device.getName(), cmd, valueObj);
    }

    private static void simulateMotion(DeviceController controller, Scanner scanner) {
        showDeviceSelectionMenu(controller);
        System.out.print("Enter motion sensor number, name, or ID: ");
        String input = scanner.nextLine().trim();
        
        try {
            SmartDevice device = findDevice(controller, input);
            
            if (!(device instanceof MotionSensor)) {
                System.out.println("✗ Device is not a motion sensor!");
                return;
            }
            
            MotionSensor sensor = (MotionSensor) device;
            sensor.detectMotion();
            controller.handleEvent(sensor.getId(), "motion_detected");
            
        } catch (DeviceNotFoundException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private static void showDeviceSelectionMenu(DeviceController controller) {
        System.out.println("\n--- Available Devices ---");
        int index = 1;
        for (SmartDevice device : controller.getHome().getAllDevices()) {
            System.out.println(index + ". " + device.getName() + " [" + device.getId() + "]");
            index++;
        }
    }

    private static SmartDevice findDevice(DeviceController controller, String input) 
            throws DeviceNotFoundException {
        
        // Try as number first
        try {
            int deviceNum = Integer.parseInt(input);
            int index = 1;
            for (SmartDevice device : controller.getHome().getAllDevices()) {
                if (index == deviceNum) {
                    return device;
                }
                index++;
            }
        } catch (NumberFormatException e) {
            // Not a number, continue to name/ID search
        }
        
        // Try as ID or name (case-insensitive)
        return controller.findDeviceByName(input);
    }

    private static void addAutomationRule(DeviceController controller, Scanner scanner) {
        try {
            System.out.println("\n--- Create Automation Rule ---");
            
            showDeviceSelectionMenu(controller);
            
            System.out.print("\nEnter rule name: ");
            String ruleName = scanner.nextLine().trim();
            
            System.out.print("Enter action device (number, name, or ID): ");
            String deviceInput = scanner.nextLine().trim();
            SmartDevice actionDevice = findDevice(controller, deviceInput);
            
            System.out.print("Enter action (turn_on/turn_off): ");
            String action = scanner.nextLine().trim();
            
            System.out.print("Enter time (HH:MM format, e.g., 18:30): ");
            String time = scanner.nextLine().trim();
            
            System.out.println("\nSelect days (separate multiple days with commas):");
            System.out.println("1. Monday");
            System.out.println("2. Tuesday");
            System.out.println("3. Wednesday");
            System.out.println("4. Thursday");
            System.out.println("5. Friday");
            System.out.println("6. Saturday");
            System.out.println("7. Sunday");
            System.out.println("8. Weekdays (Mon-Fri)");
            System.out.println("9. Weekend (Sat-Sun)");
            System.out.println("10. Every day");
            System.out.print("Enter day numbers (e.g., 1,3,5 or 8 or 10): ");
            String dayInput = scanner.nextLine().trim();
            
            String days = parseDays(dayInput);
            
            String ruleId = "r" + ruleCounter++;
            
            // Parse time string to LocalTime
            String[] timeParts = time.split(":");
            LocalTime scheduledTime = LocalTime.of(
                Integer.parseInt(timeParts[0]), 
                Integer.parseInt(timeParts[1])
            );
            
            AutomationRule rule = new AutomationRule(
                ruleId,
                ruleName,
                actionDevice.getId(),
                action,
                scheduledTime,  // Pass LocalTime instead of String
                days
            );
            
            controller.addRule(rule);
            System.out.println("✓ Automation rule created successfully!");
            System.out.println("Rule: " + ruleName + " - " + action + " " + actionDevice.getName() + 
                             " at " + time + " on " + days);
            
        } catch (DeviceNotFoundException e) {
            System.out.println("✗ Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Error creating rule: " + e.getMessage());
        }
    }
    
    private static String parseDays(String input) {
        String[] choices = input.split(",");
        StringBuilder days = new StringBuilder();
        
        for (String choice : choices) {
            choice = choice.trim();
            switch (choice) {
                case "1":
                    days.append("Monday, ");
                    break;
                case "2":
                    days.append("Tuesday, ");
                    break;
                case "3":
                    days.append("Wednesday, ");
                    break;
                case "4":
                    days.append("Thursday, ");
                    break;
                case "5":
                    days.append("Friday, ");
                    break;
                case "6":
                    days.append("Saturday, ");
                    break;
                case "7":
                    days.append("Sunday, ");
                    break;
                case "8":
                    days.append("Weekdays (Mon-Fri), ");
                    break;
                case "9":
                    days.append("Weekend (Sat-Sun), ");
                    break;
                case "10":
                    days.append("Every day, ");
                    break;
            }
        }
        
        if (days.length() > 0) {
            days.setLength(days.length() - 2); // Remove last comma and space
        }
        
        return days.toString();
    }

    private static void showEnergyConsumption(DeviceController controller) {
        System.out.println("\n--- Energy Consumption Report ---");
        double total = controller.getHome().getTotalEnergyConsumption();
        System.out.printf("Total current consumption: %.2f W\n", total);
        
        System.out.println("\nPer device:");
        for (SmartDevice device : controller.getHome().getAllDevices()) {
            if (device instanceof EnergyConsumer) {
                EnergyConsumer ec = (EnergyConsumer) device;
                System.out.printf("  %s: %.2f W\n", 
                    device.getName(), ec.getEnergyConsumption());
            }
        }
    }

    private static String getDeviceTypeName(String typeChoice) {
        switch (typeChoice) {
            case "1": return "Light";
            case "2": return "Smart TV";
            case "3": return "Thermostat";
            case "4": return "Motion Sensor";
            case "5": return "Security Camera";
            case "6": return "Smart Lock";
            default: return "Device";
        }
    }

    private static Object parseValue(String input) {
        try {
            return Integer.valueOf(input);
        } catch (NumberFormatException ignored) {}

        try {
            return Double.valueOf(input);
        } catch (NumberFormatException ignored) {}

        return input;
    }
}