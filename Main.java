import java.util.*;
import java.awt.*;
import javax.swing.*;

/*The system needs: button for adding bus stations. User must be able modify existing bus station.
 * delete bus station. display information about a bus station
 * add a new bus for station.
 * modify exsiting bus
 * delete bus
 * display info about a bus
 * enter type of bus, starting and ending location with shortest route.
 * System must accept bus station input as a substring of the full name
 * */
public class Main extends JFrame{
	public Main(){
		
		JPanel masterPanel = new JPanel(new BorderLayout());
		
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(4,3));
		p1.add(new JButton("Just an example"));
		masterPanel.add(p1,BorderLayout.EAST);
		
		
		JPanel p2 = new JPanel();
		p2.add(new JLabel("Add a bus station:"));
		p2.add(new JTextField("Just an example"));
		p2.add(new JLabel("Start Location:"));
		p2.add(new JTextField());
		p2.add(new JLabel("End Location:"));
		p2.add(new JTextField());
		masterPanel.add(p2,BorderLayout.WEST);
		
		add(masterPanel);
		
		
	}
	
	
	public static void main(String[] args) {
		
		JOptionPane.showMessageDialog(null, "SOFTWARE IS NOT TO BE USED FOR ROUTE PLANNING PURPOSE.");
		
		Main frame = new Main();
		frame.setTitle("Bus Route System");
		frame.setSize(1000,550);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	
	
	}

}
