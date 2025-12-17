package smarthome;

public interface EnergyConsumer {
    double getEnergyConsumption();  // watts
    double getDailyUsage();         // kWh per day (approx)
}
