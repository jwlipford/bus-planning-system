/* Bus:
 * 
 * A bus with a certain make and model, city/long-distance type, fuel tank
 * size, fuel consumption rate (at cruising speed), and cruising speed. From
 * tank size, cruising consumption, and cruising speed, methods determine the
 * Bus's maximum travel time and distance.
 * 
 * In Long-Distance Mode, the user selects a Bus, and that Bus's attributes
 * determine the time between stations, which is displayed for each leg of the
 * route, and whether legs of that route are too long, in which case the system
 * automatically generates gas stations within legs.
 */

public class Bus
{
    private String  makeAndModel;        // Should not contain a comma
    private BusType type;                // City or Long Distance
    private double  tankSize;            // In gallons
    private double  cruisingConsumption; // Gallons of fuel per hour at cruising speed
    private double  cruisingSpeed;       // Speed in miles per hour, used as if the Bus
                                         // travels at this speed constantly
    
    // 5 accessors:
    public String  getMakeAndModel()       { return makeAndModel;        }
    public BusType getType()               { return type;                }
    public double  getTankSize()           { return tankSize;            }
    public double  getCruisingConsumption(){ return cruisingConsumption; }
    public double  getCruisingSpeed()      { return cruisingSpeed;       }
    
    public Bus( String mm, BusType t, double ts, double cc, double cs ) throws Exception
    // Makes sure mm contains no comma (throws an Exception if so) and assigns the five
    // arguments to the corresponding fields
    {
        if( mm.contains( "," ) )
            throw new Exception( "A Bus's name cannot contain ','" );
        makeAndModel = mm;
    	type = t;
    	tankSize = ts;
    	cruisingConsumption = cc;
    	cruisingSpeed = cs;
    }
    
    public double maxTime()
    // Max number of hours cruising before fuel runs out
    {
        return tankSize / cruisingConsumption;
        //     gallons  / (gallons/hour)
    }
    
    public double maxRange()
    // Max number of miles cruising before fuel runs out
    {
        return maxTime() * cruisingSpeed;
        //     hours     * (miles/hour)
    }
}