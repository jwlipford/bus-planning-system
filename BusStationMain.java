import java.util.*;
import java.util.regex.Pattern;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;
import javax.swing.table.*;
/*The system needs: button for adding bus stations. User must be able modify existing bus station.
 * delete bus station. display information about a bus station
 * add a new bus for station.
 * modify exsiting bus
 * delete bus
 * display info about a bus
 * enter type of bus, starting and ending location with shortest route.
 * System must accept bus station input as a substring of the full name
 * */
public class BusStationMain extends JFrame{
	public BusStationMain() throws IOException{
		setLayout(new BorderLayout());
		
		
		JPanel master = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		JPanel innerLeft = new JPanel(new GridBagLayout());
		
		innerLeft.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		innerLeft.setBorder(BorderFactory.createLineBorder(Color.black));
		
		JPanel lowInnerLeft = new JPanel(new GridBagLayout());
		
		lowInnerLeft.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		String[][] data = stations(); //get the array of stations that is read from a file and assign it.
		
		String[] header= {"Station","Longitude","Latitude"};
		
		JLabel startHeader = new JLabel("Select Departure Location");
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weightx = 1.0;
		gbc.weighty = 0.01;
		gbc.gridx = 0;
		gbc.gridy=0;
		innerLeft.add(startHeader, gbc);
		
		DefaultTableModel model = new DefaultTableModel(data, header);
		
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
		
		JTable table = new JTable(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setPreferredScrollableViewportSize(new Dimension(300,100));
		
		table.setRowSorter(sorter);
		
		JScrollPane scroll = new JScrollPane(table);
		scroll.setVisible(true);
		
		
		
		
		table.setRowSelectionAllowed(true);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridx = 0;
		gbc.gridy=1;
		
		innerLeft.add(scroll,gbc);
		
		JTextField searchBox = new JTextField(20);
		
		searchBox.addActionListener(e -> {
			if(sorter.getRowFilter() != null) {
				sorter.setRowFilter(null);
			}else {
				String text = Pattern.quote(searchBox.getText());
				String regex = String.format("^%s$", text);
				sorter.setRowFilter(RowFilter.regexFilter(regex));
				
			}
	});
		
		JLabel selectedStart = new JLabel();
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.gridx=0;
		gbc.gridy=5;
		
		innerLeft.add(searchBox,gbc);
		leftPanel.add(innerLeft,BorderLayout.NORTH);
		
		
//*********************************************lowInnerLeftPanel Start**********************		
		
		JLabel endHeader = new JLabel("Select Desitination");
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weightx = 1.0;
		gbc.weighty = 0.01;
		gbc.gridx = 0;
		gbc.gridy=0;
		lowInnerLeft.add(endHeader, gbc);
		
		DefaultTableModel model2 = new DefaultTableModel(data, header);
		
		TableRowSorter<TableModel> sorter2 = new TableRowSorter<>(model2);
		
		JTable table2 = new JTable(model2);
		table2.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table2.setPreferredScrollableViewportSize(new Dimension(300,100));
		
		table2.setRowSorter(sorter2);
		
		JScrollPane scroll2 = new JScrollPane(table2);
		scroll2.setVisible(true);
		
		
		
		
		table2.setRowSelectionAllowed(true);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridx = 0;
		gbc.gridy=1;
		
		lowInnerLeft.add(scroll2,gbc);
		
		JTextField searchBox2 = new JTextField(20);
		
		searchBox2.addActionListener(e -> {
			if(sorter2.getRowFilter() != null) {
				sorter2.setRowFilter(null);
			}else {
				String text2 = Pattern.quote(searchBox2.getText());
				String regex2 = String.format("^%s$", text2);
				sorter2.setRowFilter(RowFilter.regexFilter(regex2));
				
			}
	});
		
		JLabel selectedStart2 = new JLabel();
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.gridx=0;
		gbc.gridy=5;
		
		lowInnerLeft.add(searchBox2,gbc);
		leftPanel.add(lowInnerLeft,BorderLayout.SOUTH);
				
		
//*********************************************lowInnerLeftPanel End**********************		
		
		master.add(leftPanel,FlowLayout.LEFT);
		
		JPanel masterCenter = new JPanel(new BorderLayout());
		masterCenter.setPreferredSize(new Dimension(100,340));
		//masterCenter.setBorder(BorderFactory.createLineBorder(Color.black));
		
		JButton arrowRightStart = new JButton(">>>");
		
		JTextArea userSelectionStart = new JTextArea();	
		userSelectionStart.setEditable(false);
		userSelectionStart.setBorder(BorderFactory.createLineBorder(Color.black));
		arrowRightStart.addActionListener(e ->{
			int row = table.getSelectedRow();
			String actualValue;
			String value;
			String[] arr = new String[3];
			for(int i = 0; i <=2; i++) {
				
			value = table.getModel().getValueAt(table.convertRowIndexToModel(row), i).toString();
			arr[i]=value;
			//JOptionPane.showMessageDialog(this, arr[i]);
			actualValue = convertToString(arr);

			userSelectionStart.setText(actualValue);
			}
			
			
		});
		
		
		JPanel butPan= new JPanel(new GridBagLayout());
		//butPan.setBorder(BorderFactory.createLineBorder(Color.black));
		butPan.setPreferredSize(new Dimension(100,170));
		
		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.anchor = GridBagConstraints.NORTHWEST;

		butPan.add(arrowRightStart,gbc1);
		
		masterCenter.add(butPan, BorderLayout.NORTH);
		
		//second Button in masterCenter Panel  
		  JButton arrowRightEnd = new JButton(">>>");
		  
		  
		  
		JTextArea userSelectionEnd = new JTextArea();
		userSelectionEnd.setEditable(false);
		userSelectionEnd.setBorder(BorderFactory.createLineBorder(Color.black));
		
	

		arrowRightEnd.addActionListener(e ->{
			int row = table2.getSelectedRow();
			String actualValue;
			String value;
			String[] arr = new String[3];
			for(int i = 0; i <= 2; i++) {
				
			value = table2.getModel().getValueAt(table2.convertRowIndexToModel(row), i).toString();
			arr[i]=value;
			//JOptionPane.showMessageDialog(this, arr[i]);
			actualValue = convertToString(arr);

			userSelectionEnd.setText(actualValue);
			}
			
			
		});
		
		
		JPanel butPan1= new JPanel(new GridBagLayout());
		//butPan.setBorder(BorderFactory.createLineBorder(Color.black));
		butPan.setPreferredSize(new Dimension(100,170));

		gbc1.anchor = GridBagConstraints.NORTHWEST;
		butPan1.add(arrowRightEnd,gbc1);
		masterCenter.add(butPan1);

		master.add(masterCenter);
		
		
		
		JPanel rightPanel = new JPanel(new BorderLayout());
		rightPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		rightPanel.setPreferredSize(new Dimension(300,340));
		JPanel rightTop = new JPanel(new GridLayout());
		
		rightTop.setPreferredSize(new Dimension(300,170));
		rightTop.setBorder(BorderFactory.createLineBorder(Color.black));
		JLabel finalSelectedDepart = new JLabel("Selected Departure: ");
		
		rightTop.add(finalSelectedDepart);
		rightTop.add(userSelectionStart);
		rightPanel.add(rightTop, BorderLayout.NORTH);
		
		JPanel rightBottom = new JPanel(new GridLayout());
		
		rightBottom.setPreferredSize(new Dimension(300,170));
		rightBottom.setBorder(BorderFactory.createLineBorder(Color.black));
		JLabel finalSelectDest = new JLabel("Selected Destination: ");
		
		rightBottom.add(finalSelectDest);
		rightBottom.add(userSelectionEnd);
		rightPanel.add(rightBottom, BorderLayout.SOUTH);
		master.add(rightPanel);
		
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.setRows(9);
		gridLayout.setColumns(1);
		gridLayout.setHgap(10);
		gridLayout.setVgap(10);    
		
		JPanel console = new JPanel(gridLayout);
		
		JButton finalize = new JButton("Finalize Travel"); // button to finalize the user's selection.
		finalize.addActionListener(e ->{
			//String[] options = new String[]{"View A", "View B", "View C", "Cancel"};
			//JOptionPane.showOptionDialog(null, "Message", "Choose Preferred Route", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			JFrame showOptions = new JFrame();
			showOptions.setLayout(new FlowLayout());
			showOptions.setTitle("Choose Preferred Route");
			showOptions.setSize(500,260);
			showOptions.setLocationRelativeTo(null);
			showOptions.setVisible(true);

			JPanel first = new JPanel();
			JPanel second = new JPanel();
			JPanel three = new JPanel();
			JPanel bottom = new JPanel();
			
			JButton viewA = new JButton("View A");
			first.add(viewA);
			viewA.addActionListener(e2->   {
				
			});
			showOptions.add(first);
			
			JButton viewB  = new JButton("View B");
			second.add(viewB);
			viewB.addActionListener(e3->{
				
			});
			showOptions.add(second);
			
			JButton viewC = new JButton("View C");
			three.add(viewC);
			viewC.addActionListener(e4->{
				
			});
			showOptions.add(three);
			
			
			
			
			});
			
		JButton addBusSt= new JButton("+ Bus Station");
		JButton addBus = new JButton("+ Bus");
		console.add(finalize);
		console.add(addBusSt);
		console.add(addBus);
		master.add(console);
		
		JPanel titlePan = new JPanel(new GridBagLayout());
		JLabel title = new JLabel("Bus Routing System");
		Font font = new Font("Courier", Font.BOLD, 34);
		title.setFont(font);
		titlePan.add(title);
		add(titlePan,BorderLayout.NORTH);
		
		add(master);	
		
		
	
		
	}
	
	public String convertToString(String[] arr) {
		String concat = "";
		
		return concat = "Bus Station: " + arr[0]  + "\n Longitude: " + arr[1] + "\n Latitude: " + arr[2];	
	
	}
	
	public String[][] stations() throws IOException{
		int n = 0; 																					//initialize n which will determine the space required for array
		
		
		  	File file = new File("C:\\Users\\JRT12\\Desktop\\Bus Station\\CityStationsText.txt"); 	//Find the file with stations 
		  
		  BufferedReader cr = new BufferedReader(new FileReader(file));  						 	//user bufferedReader on the file
		  String counter;																			// used as a reference to the line
		  while((counter = cr.readLine()) != null) {												//count number of lines for reference to dynamically add more later.
			  n++;
		  }
		
		
		String [][] array = new String[n][3];														//Create two dimensional array with n being the number lines 
		String temp,name,lat,longit;																// create variables for name, longitude, latitude, and temp
		
	
	
		  BufferedReader br = new BufferedReader(new FileReader(file)); 							//Have to create another BufferedReader because can't use twice
		  
		  
		  String st; 																				//attribute to hold line
		  int x = 1;																				//Used to determine array position		
		  while ((st = br.readLine()) != null) 														//Used to retrieve the information from file.
		  {
			  temp = st.split(",")[2];																//The information is seperate by comma to get a temporary name value;
			  name = temp.replaceAll("\"", "");														//Assign non-Quotation value to name.
			  lat = st.split(",")[0];																//assign latitude
			  longit = st.split(",")[1];															//assign longit

			  array[x-1][0] = name;																	//position x-1 in array create an object at 0,1,2
			  array[x-1][1] = lat;																	// assign lat to position in array
			  array[x-1][2] = longit;																// assign longit in array
			x++;  																					// increase x to position next object
		  }
		return array;																				//return newly create array of stations
	}
	
	
	
	public static void BusStationMain(String[] args) throws IOException {
		
		JOptionPane.showMessageDialog(null, "SOFTWARE IS NOT TO BE USED FOR ROUTE PLANNING PURPOSE.");
		
		BusStationMain frame = new BusStationMain();
		frame.setTitle("Bus Route System");
		frame.setSize(1000,550);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}

}
