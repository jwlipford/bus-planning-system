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
    
    public Bus( String mm, BusType t, double ts, double cc, double cs )
    {
    	makeAndModel = mm;
    	type = t;
    	tankSize = ts;
    	cruisingConsumption = cc;
    	cruisingSpeed = cs;
    }
    
    public static final Bus DEFAULT_BUS =
    		new Bus( "The Magic School Bus", BusType.city, 60, 4.5, 30 );
    
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