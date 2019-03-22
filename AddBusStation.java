import java.awt.*; // Needed for GridBagLayout and GridBagConstraints
import javax.swing.*;

public class AddBusStation extends JFrame
{
    public AddBusStation()
    {
        JPanel masterPanel = new JPanel( new GridBagLayout() );
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridx = 0;
        c.gridy = 0;
        masterPanel.add( new JLabel( "Name:" ), c );

        c.gridy = 1;
        masterPanel.add( new JLabel( "Latitude:" ), c );

        c.gridy = 2;
        masterPanel.add( new JLabel( "Longitude:" ), c );

        c.gridy = 3;
        masterPanel.add( new JButton( "Cancel" ), c );

        c.gridx = 1;
        for( c.gridy = 0; c.gridy < 3; ++c.gridy )
            masterPanel.add( new JTextField(), c );

        // Now c.gridy = 3
        masterPanel.add( new JButton( "Done" ), c );
        
        this.add( masterPanel );
        
        this.setTitle( "Add Bus Station" );
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        this.setSize( 256, 256 );
        this.setLocationRelativeTo( null ); // IDK what this does
    }
}

