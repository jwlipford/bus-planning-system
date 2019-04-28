/* LongDistStationsDatabase:
 * 
 * An interface to the file LongDistStationsText.txt, which stores a list of
 * all long-distance stations (BusStations and GasStations) and the connections
 * between them. This class reads from and writes to the file, and the file
 * should only be accessed through this class. This class contains a variety
 * of methods for creation and deletion of stations and connections, conversion
 * of them to Strings or arrays of Strings, and other needs.
 * 
 * Especially important is the update() method. Changes to the database are not
 * automatically saved in the text file, and update() must be called to do this.
 * 
 * Only one instance of this class should be created. Otherwise, pairs of
 * stations connected within one instance will not be connected across the two
 * instances, leading to failures in the system.
 */

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;

public class LongDistStationsDatabase
{
    private static final File DATA_FILE = new File( "LongDistStationsText.txt" );
    // A File object representing the text file that stores the data. This class
    // is an interface to this file.

    private ArrayList<BusStation> lDStations;
    // A list of all the stations (both BusStations and GasStations, since
    // GasStation extends BusStation) in the database.

    public void createConnection( int i0, int i1 )
    // Connects the (i0)th station and the (i1)th station (in both directions
    // -- the (i0)th to the (i1)th and the (i1)th to the (i0)th)
    {
        BusStation bs0 = this.lDStations.get( i0 );
        BusStation bs1 = this.lDStations.get( i1 );
        bs0.connect( bs1 );
        bs1.connect( bs0 );
    }
    
    public void deleteConnection( int i0, int i1 )
    // Disconnects the (i0)th station and the (i1)th station (in both
    // directions -- the (i0)th from the (i1)th and the (i1)th from the (i0)th)
    {
        BusStation bs0 = this.lDStations.get( i0 );
        BusStation bs1 = this.lDStations.get( i1 );
        bs0.disconnect( bs1 );
        bs1.disconnect( bs0 );
    }
    
    public boolean connected( int i0, int i1 )
    // Returns whether the (i0)th station and the (i1)th station are connected
    {
        return this.lDStations.get( i0 ).connectedTo( this.lDStations.get( i1 ) );
    }

    public LongDistStationsDatabase() throws Exception
    // Reads from the text file to create the list of stations, lDStations,
    // and connect the stations as indicated
    {
        BufferedReader br = new BufferedReader( new FileReader( DATA_FILE ) );
        String line = br.readLine();
        this.lDStations = new ArrayList<BusStation>();
        
        // First part of file, stations:
        while( !line.isEmpty() ) // until line.Equals("")
        {
            String[] splitLine = line.split( ", " );

            double lat = Double.parseDouble( splitLine[0] );
            double lng = Double.parseDouble( splitLine[1] );
            String name = splitLine[3];

            boolean isGasStation;
            switch( splitLine[2] )
            {
                case "City":
                    isGasStation = false;
                    break;
                case "Gas":
                    isGasStation = true;
                    break;
                default:
                    throw new Exception( "Error reading LongDistStationsText.txt" );
            }

            BusStation lDStation = isGasStation ? new GasStation( lat, lng, name )
                    : new BusStation( lat, lng, name );
            this.lDStations.add( lDStation );
            line = br.readLine();
        }
        line = br.readLine();
        
        // Second part of file, connections:
        while( line != null && !line.isEmpty() )
        {
            String[] splitLine = line.split( " " );
            // In LongDistStationsText.txt (as opposed to CityStationsText.txt),
            // each of these lines has only 1 connection, making this part of
            // the code fairly simple.
            this.createConnection( Integer.parseInt( splitLine[0] ),
                                   Integer.parseInt( splitLine[1] ) );
            line = br.readLine();
        }
        
        br.close();
    }

    private String[][] arrayListTo2dStringArray(ArrayList<BusStation> arrayList)
    // Returns a 2D String array in which each row represents a station in the
    // arrayList and each of the four columns represents one of the four fields
    // of the BusStation class.
    {
        String[][] array = new String[arrayList.size()][4];
        for( int i = 0; i < arrayList.size(); ++i )
        {
            BusStation bs = arrayList.get( i );
            array[i][0] = bs.getName();
            array[i][1] = bs.getClass() == BusStation.class ? "City" : "Gas";
            array[i][2] = String.valueOf( bs.getLatitude() );
            array[i][3] = String.valueOf( bs.getLongitude() );
        }
        return array;
    }

    private ArrayList<BusStation> busStationsArrayList()
    // Returns a list of the BusStations in the database that are not GasStations.
    {
        ArrayList<BusStation> busStations = new ArrayList<BusStation>( lDStations.size() );
        for( BusStation bs : lDStations )
            if( bs.getClass() == BusStation.class )
                busStations.add( bs );
        return busStations;
    }

    private ArrayList<BusStation> gasStationsArrayList()
    // Returns a list of the GasStations in the database, but not the BusStations.
    {
        ArrayList<BusStation> gasStations = new ArrayList<BusStation>( lDStations.size() );
        for( BusStation bs : lDStations )
            if( bs.getClass() == GasStation.class )
                gasStations.add( bs );
        return gasStations;
    }
    
    public ArrayList<BusStation> toArrayList()
    // Returns a list of all the stations in the database -- namely, the
    // lDStations ArrayList itself. This method is only used in LongDistTravel
    // .implementTravel.
    {
        return this.lDStations;
    }

    public String[][] busStations()
    // Returns a 2D String array in which each row represents a BusStation in
    // the database. Only BusStations, not GasStations, are included.
    {
        return arrayListTo2dStringArray( this.busStationsArrayList() );
    }

    public String[][] gasStations()
    // Returns a 2D String array in which each row represents a GasStation in
    // the database. Only GasStations, not BusStations, are included.
    {
        return arrayListTo2dStringArray( this.gasStationsArrayList() );
    }

    public String[][] allStations()
    // Returns a 2D String array in which each row represents a station in
    // the database. Both BusStations and GasStations are included.
    {
        return arrayListTo2dStringArray( this.lDStations );
    }
    
    public String connectionsString()
    // Returns a single String listing all the connections between all the
    // stations. One line might look like, for example,
    //         "Washington DC <==> New York NY"
    {
        String s = "";
        int indexLim = this.lDStations.size(); // Exclusive max station index
        
        // Loops through all stations, checking for each one whether it is
        // connected to any of those with higher indices than it
        for( int i = 0; i < indexLim; ++i )
        {
            BusStation ithStation = lDStations.get(i);
            for( int j = i + 1; j < indexLim; ++j )
            {
                BusStation jthStation = lDStations.get(j);
                if( ithStation.connectedTo( jthStation ) )
                    s += ithStation.getName() + " <==> " + jthStation.getName() + "\n";
            }
        }
        return s;
    }

    public void update() throws Exception
    // Overwrites the text file with information from lDStations. This method
    // is the only way to save changes made using other methods into the actual
    // text file; without using this method, changes will be lost upon closing
    // the program.
    {
        FileWriter fw = new FileWriter( DATA_FILE );

        // Write first part of file:
        for( BusStation bs : this.lDStations )
            fw.write( bs.getLatitude() + ", " + bs.getLongitude() + ", "
                    + (bs.getClass() == GasStation.class ? "Gas, " : "City, ") +
                    bs.getName() + "\n" );
        fw.write( "\n" );

        // Write second part of file:
        int indexLim = this.lDStations.size(); // Exclusive max station index
        for( int i = 0; i < indexLim; ++i )
        {
            BusStation ithStation = lDStations.get(i);
            for( int j = i + 1; j < indexLim; ++j )
            {
                BusStation jthStation = lDStations.get(j);
                if( ithStation.connectedTo( jthStation ) )
                    fw.write( i + " " + j + "\n" );
            }
        }

        fw.close();
    }

    public void addLDStation(BusStation lDStation) throws Exception
    // Adds a BusStation to lDStations, but does not update the text file (call
    // update() to do so)
    {
        if( lDStation.getName().contains( "," ) )
            throw new Exception( "Long-distance bus station's name cannot contain ','" );
        this.lDStations.add( lDStation );
    }

    public void addNewLDStation(double lat, double lng, boolean isGasStation, String name)
        throws Exception
    // Creates a new BusStation from the arguments and adds it to lDStations,
    // but does not update the text file (call update() to do so)
    {
        if( name.contains( "," ) )
            throw new Exception( "Long-distance bus station's name cannot contain ','" );
        BusStation lDStation = isGasStation ? new GasStation( lat, lng, name )
                : new BusStation( lat, lng, name );
        this.lDStations.add( lDStation );
    }

    public void delete(int index)
    // Deletes the BusStation at the index from lDStations, but does not update
    // the text file (call update() to do so)
    {
        String name = this.lDStations.get( index ).getName();
        this.lDStations.remove( index );
        for( BusStation bs : this.lDStations )
        {
            for( int i = 0; i < bs.connectedStations.size(); ++i )
            {
                if( bs.connectedStations.get( i ).getName().equals( name ) )
                    bs.connectedStations.remove( i );
            }
        }
    }
    
    public BusStation stationAtLocation( double lat, double lng )
    // If a station exists in the database at (lat, lng), returns that station.
    // If no such station exists, returns null. This method can be and is used
    // to check whether a station exists at the location by checking whether
    // this method returns null.
    {
        for( BusStation b : lDStations )
        {
            if( b.getLatitude() == lat && b.getLongitude() == lng )
                return b;
        }
        return null;
    }
}
