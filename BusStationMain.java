import java.util.regex.Pattern;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;
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

	int userStart, userEnd;
	String[] start = new String[3];
	String[] end = new String[3];
	String[] travelPlans = new String[3];
	String chosen = "";
	boolean isStart = false;
	boolean isEnd = false;
	String selectStart, selectEnd;
	JTextArea userSelectionStart = new JTextArea("");
	JTextArea userSelectionEnd = new JTextArea("");
	
	
	
	
	JButton finalize = new JButton("Finalize Travel"); // button to finalize the user's selection.
	public BusStationMain() throws IOException{
		setLayout(new BorderLayout());
		
		  try {
	            setIconImage(ImageIO.read(getClass().getResource("busIcon.jpg")));
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		
		this.finalize.setEnabled(false);
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
		
		//textConverter(data);
		
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
			String text = Pattern.quote(searchBox.getText());
			sorter.setRowFilter(RowFilter.regexFilter(text));
		});
		
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
			String text = Pattern.quote(searchBox2.getText());
			sorter2.setRowFilter(RowFilter.regexFilter(text));
		});
		
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
		
		userSelectionStart.setEditable(false);
		userSelectionStart.setBorder(BorderFactory.createLineBorder(Color.black));
		
		arrowRightStart.addActionListener(e ->{	
			//arrows to select values
			//JOptionPane.showMessageDialog(null, "No Selection Made!", "Alert!", getDefaultCloseOperation());
			
			int row = table.getSelectedRow(); //get user selected row
			String value;
			try {
			for(int i = 0; i <=2; i++) {
				
			value = table.getModel().getValueAt(table.convertRowIndexToModel(row), i).toString();
			this.start[i]=value; //assign to global variable
			//JOptionPane.showMessageDialog(this, arr[i]);
			
			}
			this.selectStart = convertToString(this.start);
			userSelectionStart.setText(this.selectStart);
			this.isStart = true;
			
			changeFinal(this.isStart,this.isEnd);
			}catch(IndexOutOfBoundsException g) {
				JOptionPane.showMessageDialog(null, "No departure location selected!", "Alert!", getDefaultCloseOperation());
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
		  
		  
		  
		
		userSelectionEnd.setEditable(false);
		userSelectionEnd.setBorder(BorderFactory.createLineBorder(Color.black));
		
	

		arrowRightEnd.addActionListener(e ->{
			int row = table2.getSelectedRow();
			String value;
			//String[] arr = new String[3];
			try {
			for(int i = 0; i <= 2; i++) {
			
			value = table2.getModel().getValueAt(table2.convertRowIndexToModel(row), i).toString();
			this.end[i]=value;
			//JOptionPane.showMessageDialog(this, arr[i]);
			this.selectEnd = convertToString(this.end);

			userSelectionEnd.setText(this.selectEnd);
			}
			this.isEnd = true;
			if(this.userSelectionStart.equals("")) {
				this.isStart = false;
			}
			changeFinal(this.isStart,this.isEnd);
			}catch(IndexOutOfBoundsException g) {
				JOptionPane.showMessageDialog(null, "No destination selected!", "Alert!", getDefaultCloseOperation());
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
		
		//JButton finalize = new JButton("Finalize Travel"); // button to finalize the user's selection.
		finalize.addActionListener(e ->{
			//String[] options = new String[]{"View A", "View B", "View C", "Cancel"};
			//JOptionPane.showOptionDialog(null, "Message", "Choose Preferred Route", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			

				try {
					this.userStart = findStationNumber(this.start[0]);
					this.userEnd = findStationNumber(this.end[0]);
				} catch (IOException e5) {

					JOptionPane.showMessageDialog(null, "One of the selections are null or empty", "Alert!", getDefaultCloseOperation());
				} 																	//retrieve the position of the station for start location
				
				
			
			
			try {
				this.travelPlans = CityDefaultInitialization.implementTravel(this.userStart, this.userEnd); //returns an array of strings that will be used to input into JLabels
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		
			
			
			JFrame showOptions = new JFrame();
			showOptions.setLayout(new BorderLayout());
			showOptions.setTitle("Choose Preferred Route");
			showOptions.setSize(500,260);
			showOptions.setLocationRelativeTo(null);
			showOptions.setVisible(true);
			
			JPanel holdOps = new JPanel(new FlowLayout());
			JPanel first = new JPanel(new BorderLayout());
			JPanel wrapA = new JPanel();//used to keep button from fill main panel
			JPanel wrapB = new JPanel();
			JPanel wrapC = new JPanel();
			JPanel second = new JPanel(new BorderLayout());
			JPanel three = new JPanel(new BorderLayout());
			
			ButtonGroup optionGroup = new ButtonGroup();
			
			JRadioButton optionA = new JRadioButton("Option A: ");
			optionA.setSelected(true);
			JRadioButton optionB = new JRadioButton("Option B: ");
			JRadioButton optionC = new JRadioButton("Option C: ");
			
			optionGroup.add(optionA);
			optionGroup.add(optionB);
			optionGroup.add(optionC);
			
			JButton viewA = new JButton("View A");
			wrapA.add(viewA);
			first.add(optionA, BorderLayout.NORTH);
			first.add(wrapA,BorderLayout.SOUTH);
			viewA.addActionListener(e2->   {

				JTextArea bestRoute = new JTextArea(this.travelPlans[0]);
				
				JScrollPane paneOne = new JScrollPane(bestRoute);
				paneOne.setPreferredSize(new Dimension(400,600));
				JOptionPane.showMessageDialog(null,paneOne,"Option A",JOptionPane.PLAIN_MESSAGE);
				
			});
			holdOps.add(first);
			
			JButton viewB  = new JButton("View B");
			wrapB.add(viewB);
			second.add(optionB,BorderLayout.NORTH);
			second.add(wrapB,BorderLayout.SOUTH);
			viewB.addActionListener(e3->{
				JTextArea secondRoute = new JTextArea(this.travelPlans[1]);

				JScrollPane paneTwo = new JScrollPane(secondRoute);
				paneTwo.setPreferredSize(new Dimension(400,600));
				JOptionPane.showMessageDialog(null,paneTwo,"Option B",JOptionPane.PLAIN_MESSAGE);
				});

			holdOps.add(second);
			
			JButton viewC = new JButton("View C");
			wrapC.add(viewC);
			three.add(optionC,BorderLayout.NORTH);
			three.add(wrapC,BorderLayout.SOUTH);
			viewC.addActionListener(e4->{
				
				JTextArea thirdRoute = new JTextArea(this.travelPlans[2]);

				JScrollPane paneThree = new JScrollPane(thirdRoute);
				paneThree.setPreferredSize(new Dimension(400,600));
				JOptionPane.showMessageDialog(null,paneThree,"Option C",JOptionPane.PLAIN_MESSAGE);
			});
			holdOps.add(three);
			showOptions.add(holdOps,BorderLayout.CENTER);
			
			
			JPanel holdButtons = new JPanel(new FlowLayout());
			JPanel approveWrap = new JPanel();
			JPanel cancelWrap = new JPanel();
			JButton approve = new JButton("OK");
			approveWrap.add(approve);
			holdButtons.add(approveWrap);
			approve.addActionListener(c->{
				if(optionA.isSelected())
			{	
				this.chosen = this.travelPlans[0];
			}else if(optionB.isSelected()) {
				this.chosen = this.travelPlans[1];
			}else if(optionC.isSelected()) {
				this.chosen = this.travelPlans[2];
			}
				
				JComponent comp = (JComponent) c.getSource();
				  Window win = SwingUtilities.getWindowAncestor(comp);
				  win.dispose();
			});
			JButton cancel = new JButton("Cancel");
			cancelWrap.add(cancel);
			holdButtons.add(cancelWrap);
			showOptions.add(holdButtons,BorderLayout.SOUTH);
			cancel.addActionListener(f1->{
				this.chosen = "";
				JComponent comp = (JComponent) f1.getSource();
				  Window win = SwingUtilities.getWindowAncestor(comp);
				  win.dispose();
			});
			
			});
			
		JButton addBusSt= new JButton("+ Bus Station");
		
		addBusSt.addActionListener(t->{
			JOptionPane.showMessageDialog(null, "Coming Soon!", "Alert!", getDefaultCloseOperation());

		});
		
		
		JButton addBus = new JButton("+ Bus");
		addBus.addActionListener(t->{
			JOptionPane.showMessageDialog(null, "Coming Soon!", "Alert!", getDefaultCloseOperation());

		});
		
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
		
		add(master,BorderLayout.CENTER);	
		
		
		JPanel result = new JPanel(new FlowLayout());
		JLabel showResult = new JLabel("Selected: ");
		JButton view = new JButton("View Selection");
		JButton clear = new JButton("Clear Selection!");
		result.add(showResult);
		result.add(view);
		result.add(clear);
		add(result,BorderLayout.SOUTH);
		
		clear.addActionListener(j->{

			if(!this.chosen.equals("") || !userSelectionStart.equals("") || !userSelectionStart.equals("")){
				this.chosen="";
				userSelectionStart.setText("");;
				userSelectionEnd.setText("");
				this.isStart = false;
				this.isEnd = false;
				JOptionPane.showMessageDialog(null, "All selections cleared.", "Success!", getDefaultCloseOperation());
				changeFinal(this.isStart,this.isEnd);
			}else {
				JOptionPane.showMessageDialog(null, "Selection is already empty!", "Alert!", getDefaultCloseOperation());
				
			}
			
		});
		
		view.addActionListener(b->{ //used to get view for selected items.
	
			if(this.chosen.equals("")) {
				JOptionPane.showMessageDialog(null, "No Selection Made!", "Alert!", getDefaultCloseOperation());
				
			}else {

				
				JTextArea finalSelection = new JTextArea(this.chosen);
				JScrollPane scl = new JScrollPane(finalSelection);
				scl.setPreferredSize(new Dimension(400,600));
				JOptionPane.showMessageDialog(null,scl,"Final Selection",JOptionPane.PLAIN_MESSAGE);
			
				
			}
		});
		
	}
	
	public String convertToString(String[] arr) {
		return "Bus Station: " + arr[0]  + "\n Longitude: " + arr[1] + "\n Latitude: " + arr[2];
	}
	
	public String[][] stations() throws IOException{
		
	  //File file = new File("C:\\Users\\JRT12\\Desktop\\Bus Station\\CityStationsText.txt"); 	//Find the file with stations 
	  //File file = new File( "E:\\USCA\\Spring 2019\\CSCI 240\\Project\\BusPlanningSystem\\"
	  //		+ "BusPlanningSystem\\src\\CityStationsText.txt" );
		File file = new File( "C:\\Users\\JRT12\\Desktop\\Bus Station\\CityStationsText.txt" );
		
		BufferedReader cr = new BufferedReader(new FileReader(file));  						 	//user bufferedReader on the file
		
		int n = 0; 																				//initialize n which will determine the space required for array
		while( !cr.readLine().isEmpty() )															//count number of lines for reference to dynamically add more later.
			n++;
		
		String [][] array = new String[n][3];														//Create two dimensional array with n being the number lines 
		String temp,name,lat,longit;																// create variables for name, longitude, latitude, and temp
		
		cr.close();
		cr = new BufferedReader(new FileReader(file)); 											//Have to create another BufferedReader because can't use twice
		  
		String st; 																				//attribute to hold line
		int x = 1;																				//Used to determine array position		
		while (!(st = cr.readLine()).isEmpty()) 														//Used to retrieve the information from file.
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
		cr.close();
		return array;																				//return newly create array of stations
	}
	
	
	public int findStationNumber(String station) throws IOException { 								//Method to return a number or position to get route started.
		
		String[][] temp = stations();
		String arrStat = "";
		int position = 0;
		for(int i = 0; i<temp.length; i++) {
			arrStat = temp[i][0];
		//System.out.println(temp[i][0]);
			if(arrStat.equals(station)) {
				position = i+1;
				break;
			}
			
			
		}
		return position;

	}
	
	public String Routes() {
		String a = "";
		
		return a;
	}
		
	public void changeFinal(boolean a, boolean b) {
	if(a == true && b == true) {
			this.finalize.setEnabled(true);
		}else
			this.finalize.setEnabled(false);
	}
	
	
	public static void main(String[] args) throws IOException {
		
		JOptionPane.showMessageDialog(null, "SOFTWARE IS NOT TO BE USED FOR ROUTE PLANNING PURPOSE.");
		
		BusStationMain frame = new BusStationMain();
		frame.setTitle("Bus Route System");
		frame.setSize(1000,550);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		
	}

}
