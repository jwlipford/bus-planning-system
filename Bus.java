public class Bus
{
    private String  makeAndModel;
    private BusType type;
    private double  tankSize;            // in gallons
    private double  cruisingConsumption; // gallons of fuel per hour
    private double  cruisingSpeed;       // miles per hour
    
    public String  getMakeAndModel()       { return makeAndModel;        }
    public BusType getType()               { return type;                }
    public double  getTankSize()           { return tankSize;            }
    public double  getCruisingConsumption(){ return cruisingConsumption; }
    public double  getCruisingSpeed()      { return cruisingSpeed;       }
    
    // Max number of hours cruising before fuel runs out
    public double maxTime()
    {
        return tankSize / cruisingConsumption;
        //     gallons  / (gallons/hour)
    }
    
    // Max number of miles cruising before fuel runs out
    public double maxRange()
    {
        return maxTime() * cruisingSpeed;
        //     hours     * (miles/hour)
    }
}