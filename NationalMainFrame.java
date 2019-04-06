import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.StyledDocument;

public class NationalMainFrame extends JFrame{
	
	
	
	String[] addBusArr = new String[4];      //Used to get bus from addBusPanel
	String[] addStationArr = new String[4];  //Used to get Station from addStationPanel
	int userStart, userEnd;
	String[] start = new String[4];
	String[] travelPlans = new String[4];
	String[] end = new String[4];
	String[] planTotals  = new String[4];
	String chosen = "";
	boolean isStart = false;
	boolean isEnd = false;
	String selectStart =" ";
	String selectEnd = " ";
	JTextArea userSelectionStart = new JTextArea("");
	JTextArea userSelectionEnd = new JTextArea("");
	String[] selectedBus = new String[5];
	JTextPane finalPane = new JTextPane();
	JTextPane chosenBusPane = new JTextPane();
	StyledDocument docum = chosenBusPane.getStyledDocument();
	StyledDocument doc = finalPane.getStyledDocument();
	String[] delBus = new String[5];
	JButton finalize = new JButton("Finalize Travel"); 		// button to finalize the user's selection.
	
	public NationalMainFrame() throws Exception {
		finalize.setEnabled(false);
		setLayout(new BorderLayout()); 						// set layout of frame to BorderLayout.
		JPanel titlePan = new JPanel(new GridBagLayout());
		JLabel title = new JLabel("National Bus Routing");
		Font font = new Font("SansSerif",Font.BOLD, 34);
		title.setFont(font);
		titlePan.add(title);
		add(titlePan,BorderLayout.NORTH);
		JPanel master = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel leftPanel = new JPanel(new BorderLayout());
		JPanel leftConsole = new JPanel(new GridLayout(6,1));
		JPanel showFinalSelection = new JPanel(new BorderLayout());
		JPanel holdFinal = new JPanel();
		
		userSelectionStart.setEditable(false);
		userSelectionStart.setBorder(BorderFactory.createLineBorder(Color.black));
		JButton finalSelectedDepart = new JButton("Select Departure");
		JPanel holdButton= new JPanel();
		JPanel holdArea = new JPanel(new BorderLayout());
		holdArea.setPreferredSize(new Dimension(300,170));
		holdArea.add(userSelectionStart);
		holdButton.add(finalSelectedDepart);
		
		JPanel leftTop = new JPanel(new BorderLayout());
		
		leftTop.setBorder(BorderFactory.createLineBorder(Color.black));
		
		leftTop.add(holdArea,BorderLayout.NORTH);
		leftTop.add(holdButton,BorderLayout.SOUTH);
		
		finalSelectedDepart.addActionListener(e->{
			setLocations sl = new setLocations();
			int var = JOptionPane.showConfirmDialog(null, sl,
                    "Select a Departure", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			
			if(var == JOptionPane.OK_OPTION) {
				try {
					int row = setLocations.table.getSelectedRow();
				String value;

				for(int i = 0; i <= 3; i++) {
				
				value = setLocations.table.getModel().getValueAt(setLocations.table.convertRowIndexToModel(row), i).toString();
				NationalMainFrame.this.start[i]=value;
				
				}
				this.selectStart = convertToString(this.start);
				//System.out.print(this.selectStart);
				
				userSelectionStart.setText(this.selectStart);
				this.isStart = true;
				
				changeFinal(this.isStart,this.isEnd);
				for(int i = 0; i<this.start.length; i++) {
					//System.out.println(this.start[i]);
				}
				}catch(Exception e2) {
					JOptionPane.showMessageDialog(this, "No value was selected! ");
				}
			}
		});
		
		leftPanel.add(leftTop,BorderLayout.NORTH);
		
		
		userSelectionEnd.setEditable(false);
		userSelectionEnd.setBorder(BorderFactory.createLineBorder(Color.black));
		JButton finalSelectedDest = new JButton("Select Destination");
		JPanel Button= new JPanel();
		JPanel Area = new JPanel(new BorderLayout());
		Area.setPreferredSize(new Dimension(300,170));
		Area.add(userSelectionEnd);
		Button.add(finalSelectedDest);
		
		JPanel leftBot = new JPanel(new BorderLayout());
		
		leftBot.setBorder(BorderFactory.createLineBorder(Color.black));
		
		leftBot.add(Area,BorderLayout.NORTH);
		leftBot.add(Button,BorderLayout.SOUTH);

		leftPanel.add(leftTop,BorderLayout.NORTH);
		leftPanel.add(leftBot,BorderLayout.SOUTH);
		
		JPanel blah = new JPanel();
		
		finalSelectedDest.addActionListener(e->{
			setLocations loc = new setLocations();
			int var = JOptionPane.showConfirmDialog(null, loc,
                    "Select a Destination", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			
			if(var == JOptionPane.OK_OPTION) {
				try {
					int row = setLocations.table.getSelectedRow();
				String value;

				for(int i = 0; i <= 3; i++) {
				
				value = setLocations.table.getModel().getValueAt(setLocations.table.convertRowIndexToModel(row), i).toString();
				NationalMainFrame.this.end[i]=value;
				
				}
				this.selectEnd = convertToString(this.end);
				//System.out.print(this.selectEnd);
				
				userSelectionEnd.setText(this.selectEnd);
				this.isEnd = true;
				
				changeFinal(this.isStart,this.isEnd);
				//System.out.print("Test" + this.end[0]);
				}catch(Exception e2) {
					JOptionPane.showMessageDialog(this, "No value was selected! ");
				}
			}
		});
		
		
		
		leftPanel.add(blah,BorderLayout.CENTER);

		
		
		
		
		//leftPanel.add(startPanel,BorderLayout.NORTH);					//add startpanel to leftpanel
		
		/*
		DefaultTableModel model2 = new DefaultTableModel(data, header); 	//add data and header to table model
		
		TableRowSorter<TableModel> sorter2 = new TableRowSorter<>(model2); //create method for sorting
		
		JTable table2 = new JTable(model2);				 				//Create JTable using model
		table2.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); 		//Resize to fit columns
		table2.setPreferredScrollableViewportSize(new Dimension(300,100)); //set dimensions of scrollable
		
		table2.setRowSorter(sorter2);										//add sorter to table
		
		JScrollPane scroll2 = new JScrollPane(table2);					//set scrollpane for JTable
		scroll2.setVisible(true);										//make scroll appear
		
		table2.setRowSelectionAllowed(true);								//Able to select the specific row
		
		JTextField searchBox2 = new JTextField(20);						//create text box for user input
			
		searchBox2.addActionListener(e -> {								//event to sort table
			String text2 = "(?i)" + Pattern.quote(searchBox2.getText()); // (?i) = case-insensitive
			sorter.setRowFilter(RowFilter.regexFilter(text2));		   //sort table according to text
		});
		
		JLabel endHeader = new JLabel("Select Destination");
		
		endPanel.setLayout(new BorderLayout());							//set layout for endPanel
		endPanel.add(endHeader,BorderLayout.NORTH);						//add endHeader to startPanel make it top
		endPanel.add(scroll2,BorderLayout.CENTER);						//add scroll2 (Jtable) to centered endpanel
		endPanel.add(searchBox2,BorderLayout.SOUTH);					//add searchbox2 to bottom of endpanel
		leftPanel.add(endPanel,BorderLayout.SOUTH);						//add endpanel to leftpanel
		
		master.add(leftPanel);
	*/
//*********************************CenterPanel********************************//
		JPanel centerPanel = new JPanel(new BorderLayout());
	
		/*
		arrowRightStart.addActionListener(e ->{	
			
			int row = table.getSelectedRow(); //get user selected row
			String value;
			try {
			for(int i = 0; i <=3; i++) {
				
			value = table.getModel().getValueAt(table.convertRowIndexToModel(row), i).toString();
			
			this.start[i]=value; //assign to global variable
			System.out.print(this.start[i]);
			}
			this.selectStart = convertToString(this.start);
			System.out.print(this.selectStart);
			
			userSelectionStart.setText(this.selectStart);
			this.isStart = true;
			
			changeFinal(this.isStart,this.isEnd);
			}catch(IndexOutOfBoundsException g) {
				JOptionPane.showMessageDialog(null, "No departure location selected!", "Alert!", getDefaultCloseOperation());
			}
		});*/
		

		
		//Center bottom arrow button
		
		
	
/*
		arrowRightEnd.addActionListener(e ->{
			int row = table2.getSelectedRow();
			String value;
			try {
			for(int i = 0; i <= 3; i++) {
			
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
		//butPan1.setBorder(BorderFactory.createLineBorder(Color.black));
		butPan1.setPreferredSize(new Dimension(100,170));

		butPan1.add(arrowRightEnd,gbc1);
		centerPanel.add(butPan1,BorderLayout.SOUTH);

		master.add(centerPanel);
		*/
	//*****************Right Panel ***************//	
		
		//JPanel rightPanel = new JPanel(new BorderLayout());
		//rightPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		//rightPanel.setPreferredSize(new Dimension(300,340));

		
		//JPanel rightBottom = new JPanel(new GridLayout());
		
		//rightBottom.setPreferredSize(new Dimension(300,170));
		//rightBottom.setBorder(BorderFactory.createLineBorder(Color.black));
		//JLabel finalSelectDest = new JLabel("Selected Destination: ");
		
		//rightBottom.add(finalSelectDest);
		//rightBottom.add(userSelectionEnd);
		//rightPanel.add(rightBottom, BorderLayout.SOUTH);
		//master.add(rightPanel);
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.setRows(9);
		gridLayout.setColumns(1);
		gridLayout.setHgap(10);
		gridLayout.setVgap(10);    
		
		JPanel console = new JPanel(gridLayout);
		JButton city = new JButton("City Routing");
		
		JButton userBus = new JButton("Select Bus");
		userBus.addActionListener(e->{
			selectBus sb = new selectBus();
			int var = JOptionPane.showConfirmDialog(null, sb,
                    "Select a Bus", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			
			if(var == JOptionPane.OK_OPTION) {
				try {
					int row = selectBus.busTable.getSelectedRow();
				String value;

				for(int i = 0; i <= 4; i++) {
				
				value = selectBus.busTable.getModel().getValueAt(selectBus.busTable.convertRowIndexToModel(row), i).toString();
				NationalMainFrame.this.selectedBus[i]=value;
				//JOptionPane.showMessageDialog(this, arr[i]);
				
				}
				String userChosenBus = "Bus Name: " + this.selectedBus[0] + "\nTank Size: " + this.selectedBus[2] + "\nCruise Consumption: " + this.selectedBus[3] + "\nCruise Speed: " + this.selectedBus[4];
				chosenBusPane.setText("");//resets pre-existing text
				docum.insertString(0, userChosenBus, null );
				/*used to see if bus array worked for(int i = 0; i<this.selectedBus.length; i++) {
					System.out.println(this.selectedBus[i]);
				}*/
				}catch(Exception e2) {
					JOptionPane.showMessageDialog(this, "No value was selected! ");
				}
				
			}
		});
		
		
		city.addActionListener(e->{
		int warn = JOptionPane.showConfirmDialog(null, "Are you sure you want to leave?", "Leave National Bus Routing",JOptionPane.YES_NO_OPTION);
		if(warn == JOptionPane.YES_OPTION) {
			try {
				//BusStationMain busMain = new BusStationMain();
				BusStationMain frame = new BusStationMain();
				frame.setTitle("City Bus Routing System");
				frame.setSize(1000,550);
				frame.setLocationRelativeTo(null);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);		
				this.dispose();
			} catch (IOException e1) {
			
				e1.printStackTrace();
			}			

		}
		});
		
		JButton addBus = new JButton("Add Bus");
		addBus.addActionListener(e->{
			boolean empty = false;
			addBusPanel addBusPanel = new addBusPanel();
			int var = JOptionPane.showConfirmDialog(null, addBusPanel,
	                     "Create Bus", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if(var==JOptionPane.OK_OPTION) {
				addBusArr[0] = addBusPanel.mAndm.getText();
				addBusArr[1] = addBusPanel.tank.getText();
				addBusArr[2] = addBusPanel.cruiseTxt.getText();
				addBusArr[3] = addBusPanel.CruiseSpd.getText();
				for(int i = 0; i<addBusArr.length; i++) {
					if(addBusArr[i].equals("")) {
						empty = true;
						break;
					}
				}
				if(empty == true) {
					
					JOptionPane.showMessageDialog(this, "One or more of your inputs were blank. Bus was not saved!");
	
				}

				String busName = addBusArr[0];
				Double tankSz = Double.parseDouble(addBusArr[1]);
				Double CruiseCon = Double.parseDouble(addBusArr[2]);
				Double CruiseSpd = Double.parseDouble(addBusArr[3]);
				//String LongDist = "LD";
				BusesDatabase bd;
				try {
					bd = new BusesDatabase();
					final Bus UserBus = new Bus(busName, BusType.longDistance, //constructor that takes makeandmodel, type, tanksize, cruisingconsumption, cruising speed
				                         tankSz, CruiseCon, CruiseSpd );

					bd.addBus(UserBus);
					bd.update();

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		
		
	
		
		JButton addStation = new JButton("Add Station");
		addStation.addActionListener(e->{
			
			boolean empty = false;
			String position = "";
			String typeChose = "";
			double longit;
			double lat;
			String bsName = "";
			boolean isGasSt = false;
			addStationPanel addStationPanel = new addStationPanel();
			int var = JOptionPane.showConfirmDialog(null, addStationPanel,
	                     "Create Station", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if(var == JOptionPane.OK_OPTION) {
				addStationArr[0] = addStationPanel.stName.getText();
				if(addStationPanel.city.isSelected()) {
					typeChose = "city";
				}else if(addStationPanel.gas.isSelected()){
					typeChose = "gas";
					isGasSt = true;
				}
				
				addStationArr[1] = typeChose;
				
				try {
					bsName = addStationArr[0];
					
				}catch(Exception e5) {
					System.out.print("Issue while trying to get name of station!");
				}
				
				
				try{
				
					addStationArr[2] = addStationPanel.longitTxt.getText();
					
				}catch(Exception e1) {
					JOptionPane.showMessageDialog(this, "Longitude must be a number. Station not saved!");
				}
				
				try{
				
					addStationArr[3] = addStationPanel.latTxt.getText();
				}catch(Exception e1) {
					JOptionPane.showMessageDialog(this, "Latitude must be a number. Station not saved!");
				}
				 
				
				
				
				for(int i = 0; i<addStationArr.length; i++) {
					//System.out.println(addStationArr[i]);
					if(addStationArr[i].equals("")) {
						empty = true;
						break;
					}
				}
				if(empty == true) { //Check to see if array is empty proving user didn't fill out completely
					
					JOptionPane.showMessageDialog(this, "One or more of your inputs were blank. Station was not saved!");
	
				}else {
					
					try {
						
						longit = Double.parseDouble(addStationPanel.longitTxt.getText());
						lat = Double.parseDouble(addStationPanel.latTxt.getText());
						LongDistStationsDatabase ldsd = new LongDistStationsDatabase();
						ldsd.addNewLDStation(lat,longit,isGasSt,bsName);
						ldsd.update();
						
						
						
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
		});
		
		
		JButton deleteBus = new JButton("Delete Bus");
		
		deleteBus.addActionListener(e->{
		
			selectBus sb = new selectBus();
			int var = JOptionPane.showConfirmDialog(null, sb,
                    "Delete a Bus", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			
			if(var == JOptionPane.OK_OPTION) {
				try {
					int row = selectBus.busTable.getSelectedRow();
				String value;

				for(int i = 0; i <= 4; i++) {
				
				value = selectBus.busTable.getModel().getValueAt(selectBus.busTable.convertRowIndexToModel(row), i).toString();
				delBus[i]=value;
				//JOptionPane.showMessageDialog(this, arr[i]);
				
				}
				
				String name = delBus[0];
				String type = delBus[1];
				Double tank = Double.parseDouble(delBus[2]);
				Double CC = Double.parseDouble(delBus[3]);
				Double CS = Double.parseDouble(delBus[4]);
				BusesDatabase bd = new BusesDatabase();
				int index = 0;
			for(int i = 0; i <= bd.longDistBuses().length; i++) {
				
				String bus = bd.longDistBuses()[i][0];
				 if(bus.equals(name)) {
					 break;
				 }
				 index++;
				 
			}
				bd.delete(index+1);
				bd.update();
				
				}catch(Exception e2) {
					JOptionPane.showMessageDialog(this, "No value was selected! ");
				}
				
			}
			
		});
		console.add(deleteBus);
		
		JButton deleteStation = new JButton("Delete Station");
		deleteStation.addActionListener(e->{

			deleteStations sl = new deleteStations();
			String []deleteST = new String[4];
			int var = JOptionPane.showConfirmDialog(null, sl,
                    "Delete a Station", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			
			if(var == JOptionPane.OK_OPTION) {
				try {
					int row = sl.table.getSelectedRow();
				String value;

				for(int i = 0; i <= 3; i++) {
				
				value = sl.table.getModel().getValueAt(sl.table.convertRowIndexToModel(row), i).toString();
				deleteST[i]=value;

				
				}
				
				String name = deleteST[0];

				LongDistStationsDatabase LD = new LongDistStationsDatabase();
				int index = 0;
			for(int i = 0; i <= LD.allStations().length; i++) {
				
				String station = LD.allStations()[i][0];
				 if(station.equals(name)) {
					 break;
				 }
				 index++;
				 
			}

				LD.delete(index);
				LD.update();
				
				}catch(Exception e2) {
					JOptionPane.showMessageDialog(this, "No value was selected! ");
				}
				
			}
		});
		
		
		
		
		finalize.addActionListener(e ->{
			//String[] options = new String[]{"View A", "View B", "View C", "Cancel"};
			//JOptionPane.showOptionDialog(null, "Message", "Choose Preferred Route", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			
			
				try {
					this.userStart = findStationNumber(this.start[0]);
					this.userEnd = findStationNumber(this.end[0]);
		
				} catch (IOException e5) {

					JOptionPane.showMessageDialog(null, "One of the selections are null or empty", "Alert!", getDefaultCloseOperation());
				} 																	//retrieve the position of the station for start location
				
				
			
			
			
			try
			{
				Route[] routes = LongDistTravel.implementTravel(new LongDistStationsDatabase(),this.userStart, this.userEnd);
				for( int i = 0; i < 3; ++i )
				{
					if( routes[i] == null )
					{
						this.travelPlans[i] = "No alternative found";
						this.planTotals[i]  = this.travelPlans[i];
					}
					else
					{
						this.travelPlans[i] = routes[i].display( Bus.DEFAULT_BUS );
						this.planTotals[i]  = routes[i].totals();
					}
				}
			}
			catch (Exception e1)
			{
				// TODO Auto-generated catch block
				System.out.print(this.userStart + " " + this.userEnd);
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
				//finalPane.setText(this.chosen);
				try {
				finalPane.setText("");
				doc.insertString(0, this.chosen, null );
				}catch(Exception e6) {
					System.out.print("Something went wrong in finalizing JTextPane.");
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
			
		});//end of finalize
		
		leftConsole.add(userBus);
		leftConsole.add(addBus);
		leftConsole.add(deleteBus);
		leftConsole.add(deleteStation);
		leftConsole.add(addStation);
		JPanel holdViews = new JPanel(new BorderLayout());
		JPanel holdFinalPane = new JPanel(new BorderLayout());
		JPanel holdBuses = new JPanel(new BorderLayout());
		JLabel routes = new JLabel("Your Routes: ");
		JLabel buses = new JLabel("Chosen Bus: ");
		finalPane.setEditable(false);
		JScrollPane scrollRoute = new JScrollPane(finalPane);
		finalPane.setPreferredSize(new Dimension(300,250));
		holdFinalPane.add(routes, BorderLayout.NORTH);
		holdFinalPane.add(scrollRoute,BorderLayout.CENTER);
		chosenBusPane.setEditable(false);
		chosenBusPane.setPreferredSize(new Dimension(200,150));
		holdBuses.add(buses,BorderLayout.NORTH);
		holdBuses.add(chosenBusPane,BorderLayout.CENTER);
		
		

		showFinalSelection.add(holdFinalPane);
		holdViews.add(holdBuses,BorderLayout.NORTH);
		holdViews.add(showFinalSelection,BorderLayout.CENTER);
		
		
	//console.add(userBus);
		console.add(finalize);
		console.add(city);
		master.add(leftConsole);
		master.add(leftPanel);
		master.add(console);
		master.add(holdViews);
		
		
		add(master,BorderLayout.CENTER);
		//add(bottomPanel,BorderLayout.SOUTH);
		
		
		
	}
		
	public String convertToString(String[] arr) {
		return "Bus Station: " + arr[0]  +"\nType: " + arr[1] +"\n Longitude: " + arr[2] + "\n Latitude: " + arr[3];
	}
	
	public String[][] stations() throws IOException {
		
		  //File file = new File("C:\\Users\\JRT12\\Desktop\\Bus Station\\CityStationsText.txt"); 	//Find the file with stations 
		  //File file = new File( "E:\\USCA\\Spring 2019\\CSCI 240\\Project\\BusPlanningSystem\\"
		  //		+ "BusPlanningSystem\\src\\CityStationsText.txt" );
		    File file = new File( "C:\\Users\\JRT12\\Desktop\\BusStation\\BusStation\\src\\LongDistStationsText.txt" );
			
			BufferedReader cr = new BufferedReader(new FileReader(file));  						 	//user bufferedReader on the file
			
			int n = 0; 																				//initialize n which will determine the space required for array
			while( !cr.readLine().isEmpty() )															//count number of lines for reference to dynamically add more later.
				
			
				n++;
			
			String [][] array = new String[n][4];														//Create two dimensional array with n being the number lines to count number of positions
			String temp,name,lat,longit,type;																// create variables for name, longitude, latitude, and temp
			
			cr.close();
			cr = new BufferedReader(new FileReader(file)); 											//Have to create another BufferedReader because can't use twice
			  
			String st; 																				//attribute to hold line
			int x = 1;																				//Used to determine array position		
			while (!(st = cr.readLine()).isEmpty()) 														//Used to retrieve the information from file.
			{
				lat = st.split(",")[0];																//assign latitude
				longit = st.split(",")[1];															//assign longit
				type = st.split(",")[2];
				name = st.split(",")[3];														//Assign non-Quotation value to name.

				//temp = st.split(",")[3];
				//The information is seperate by comma to get a temporary name value;
				
				array[x-1][0] = name;																	//position x-1 in array create an object at 0,1,2
				array[x-1][1] = type;
				array[x-1][2] = lat;																	// assign lat to position in array
				array[x-1][3] = longit;																// assign longit in array
				x++;  																					// increase x to position next object
			}
			cr.close();
			return array;																				//return newly create array of stations
		}
		
	
	public void changeFinal(boolean a, boolean b) {
	if(a == true && b == true) {
			this.finalize.setEnabled(true);
		}else
			this.finalize.setEnabled(false);
	}
	
public int findStationNumber(String station) throws IOException { 								//Method to return a number or position to get route started.
	LongDistStationsDatabase stations;
	int position = 0;
	try{
		stations = new LongDistStationsDatabase();
		String[][] temp = stations.allStations();
		String arrStat = "";
		position = 0;
		for(int i = 0; i<temp.length; i++) {
			arrStat = temp[i][0];
		//System.out.println(temp[i][0]);
			if(arrStat.equals(station)) {
				position = i+1;
				break;
			}	
		}
			
	}catch(Exception e) {
		
	}
	//System.out.print("this might be broken" + position);
		return position;
	

	}
	
	class addBusPanel extends JPanel {
		JTextField mAndm;
		JTextField tank;
		JTextField cruiseTxt;
		JTextField CruiseSpd;
		
		addBusPanel(){
			setLayout(new GridLayout(4,2));
			JLabel MakeandModel = new JLabel("Make and Model: ");
			 mAndm = new JTextField();
			
			JLabel tankSize = new JLabel("Tank Size: ");
			tank = new JTextField();

		
			JLabel cruisCon = new JLabel("Cruising Consumption: ");
			cruiseTxt = new JTextField();

			JLabel CruiseSpeed = new JLabel("Cruising Speed: ");
			CruiseSpd = new JTextField();
			
			add(MakeandModel);
			add(mAndm);
			add(tankSize);
			add(tank);
			add(cruisCon);
			add(cruiseTxt);
			add(CruiseSpeed);
			add(CruiseSpd);
			
		
			//new Bus( MakeAndModel, BusType.longDistance, )
			
			
			
		}
	}
	
	class addStationPanel extends JPanel {
		JTextField stName;
		JTextField stType;
		JTextField longitTxt;
		JTextField latTxt;
		JRadioButton city = new JRadioButton("City");
		JRadioButton gas = new JRadioButton("Gas");
		ButtonGroup group = new ButtonGroup();
		addStationPanel(){
			JPanel radio = new JPanel();
			
			setLayout(new GridLayout(4,2));
			JLabel station= new JLabel("Station Name: ");
			stName = new JTextField();
			
			JLabel type = new JLabel("Type: ");
			group.add(city);
			group.add(gas);
			radio.add(city);
			radio.add(gas);
			
			JLabel longit = new JLabel("Longitude: ");
			longitTxt = new JTextField();
			
			JLabel lat = new JLabel("Latitude: ");
			latTxt = new JTextField();
			
			add(station);
			add(stName);
			add(type);
			add(radio);
			add(longit);
			add(longitTxt);
			add(lat);
			add(latTxt);
		
			//new Bus( MakeAndModel, BusType.longDistance, )
			
			
			
		}
	}
	

	
	static class selectBus extends JPanel {
		
		
															//create 2-dim array to hold data
		String[] busHeader= {"Name","Type","Tank Size","Cruise Consumption", "Cruise Speed"}; 
		static JTable busTable;
		selectBus(){
	
			try {
			BusesDatabase bsd = new BusesDatabase();
			JLabel selectBus = new JLabel("Select Bus");
			String [][]busData = bsd.longDistBuses();	
			DefaultTableModel busModel = new DefaultTableModel(busData, busHeader); 	//add data and header to table model
			
			TableRowSorter<TableModel> busSorter = new TableRowSorter<>(busModel); //create method for sorting
			
			busTable = new JTable(busModel);				 				//Create JTable using model
			busTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); 		//Resize to fit columns
			busTable.setPreferredScrollableViewportSize(new Dimension(300,100)); //set dimensions of scrollable
			
			busTable.setRowSorter(busSorter);										//add sorter to table
			
			JScrollPane busScroll = new JScrollPane(busTable);					//set scrollpane for JTable
			busScroll.setVisible(true);										//make scroll appear
			
			busTable.setRowSelectionAllowed(true);								//Able to select the specific row
			
			JTextField searchBoxBus = new JTextField(20);						//create text box for user input
				
			searchBoxBus.addActionListener(e -> {								//event to sort table
				String text = "(?i)" + Pattern.quote(searchBoxBus.getText()); // (?i) = case-insensitive
				busSorter.setRowFilter(RowFilter.regexFilter(text));		   //sort table according to text
			});
			JPanel holdBus = new JPanel();
			JPanel extraPanel = new JPanel();

			holdBus.setLayout(new BorderLayout());						//set layout for startpanel
			holdBus.add(selectBus,BorderLayout.NORTH);					//add startHeader to startPanel make it top
			holdBus.add(busScroll,BorderLayout.CENTER);						//add scroll (Jtable) to centered startpanel
			holdBus.add(searchBoxBus,BorderLayout.SOUTH);					//add searchbox to bottom of startpanel
			extraPanel.setLayout(new BorderLayout());
			extraPanel.add(holdBus,BorderLayout.WEST);
			add(extraPanel);
			
			
			
			
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			
	
			
		}
	}
	
	static class setLocations extends JPanel{
		LongDistStationsDatabase stations; 
		NationalMainFrame nmf;
		static JTable table;
		setLocations(){

			try {
				stations = new LongDistStationsDatabase();
				nmf = new NationalMainFrame();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				 
			nmf.finalize.setEnabled(false); 							  //disable finalize button
			JPanel master = new JPanel(new FlowLayout(FlowLayout.LEFT)); //Create panel inside Frame 
			
			JPanel leftPanel = new JPanel(new BorderLayout()); 				//set left Panel in master
			JPanel startPanel = new JPanel(); 				   				//set start panel to hold JTable departure
			JPanel endPanel = new JPanel();					   				//set end panel to hold JTable Destination
			

			
			JLabel startHeader = new JLabel("Select Departure Location");
			String [][]data = stations.busStations();									//create 2-dim array to hold data
			String[] header= {"Station","Type","Longitude","Latitude"}; 			//header for table
			
			
			DefaultTableModel model = new DefaultTableModel(data, header); 	//add data and header to table model
			
			TableRowSorter<TableModel> sorter = new TableRowSorter<>(model); //create method for sorting
			
			table = new JTable(model);				 				//Create JTable using model
			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); 		//Resize to fit columns
			table.setPreferredScrollableViewportSize(new Dimension(300,100)); //set dimensions of scrollable
			
			table.setRowSorter(sorter);										//add sorter to table
			
			JScrollPane scroll = new JScrollPane(table);					//set scrollpane for JTable
			scroll.setVisible(true);										//make scroll appear
			
			table.setRowSelectionAllowed(true);								//Able to select the specific row
			
			JTextField searchBox = new JTextField(20);						//create text box for user input
				
			searchBox.addActionListener(e -> {								//event to sort table
				String text = "(?i)" + Pattern.quote(searchBox.getText()); // (?i) = case-insensitive
				sorter.setRowFilter(RowFilter.regexFilter(text));		   //sort table according to text
			});
			
			startPanel.setLayout(new BorderLayout());						//set layout for startpanel
			startPanel.add(startHeader,BorderLayout.NORTH);					//add startHeader to startPanel make it top
			startPanel.add(scroll,BorderLayout.CENTER);						//add scroll (Jtable) to centered startpanel
			startPanel.add(searchBox,BorderLayout.SOUTH);					//add searchbox to bottom of startpanel
			add(startPanel);
		}
	}
	
	static class deleteStations extends JPanel{
		LongDistStationsDatabase stations; 
		NationalMainFrame nmf;
		static JTable table;
		deleteStations(){

			try {
				stations = new LongDistStationsDatabase();
				nmf = new NationalMainFrame();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				 
			nmf.finalize.setEnabled(false); 							  //disable finalize button
			JPanel master = new JPanel(new FlowLayout(FlowLayout.LEFT)); //Create panel inside Frame 
			
			JPanel leftPanel = new JPanel(new BorderLayout()); 				//set left Panel in master
			JPanel startPanel = new JPanel(); 				   				//set start panel to hold JTable departure
			JPanel endPanel = new JPanel();					   				//set end panel to hold JTable Destination
			

			
			JLabel startHeader = new JLabel("Select Departure Location");
			String [][]data = stations.allStations();									//create 2-dim array to hold data
			String[] header= {"Station","Type","Longitude","Latitude"}; 			//header for table
			
			
			DefaultTableModel model = new DefaultTableModel(data, header); 	//add data and header to table model
			
			TableRowSorter<TableModel> sorter = new TableRowSorter<>(model); //create method for sorting
			
			table = new JTable(model);				 				//Create JTable using model
			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); 		//Resize to fit columns
			table.setPreferredScrollableViewportSize(new Dimension(300,100)); //set dimensions of scrollable
			
			table.setRowSorter(sorter);										//add sorter to table
			
			JScrollPane scroll = new JScrollPane(table);					//set scrollpane for JTable
			scroll.setVisible(true);										//make scroll appear
			
			table.setRowSelectionAllowed(true);								//Able to select the specific row
			
			JTextField searchBox = new JTextField(20);						//create text box for user input
				
			searchBox.addActionListener(e -> {								//event to sort table
				String text = "(?i)" + Pattern.quote(searchBox.getText()); // (?i) = case-insensitive
				sorter.setRowFilter(RowFilter.regexFilter(text));		   //sort table according to text
			});
			
			startPanel.setLayout(new BorderLayout());						//set layout for startpanel
			startPanel.add(startHeader,BorderLayout.NORTH);					//add startHeader to startPanel make it top
			startPanel.add(scroll,BorderLayout.CENTER);						//add scroll (Jtable) to centered startpanel
			startPanel.add(searchBox,BorderLayout.SOUTH);					//add searchbox to bottom of startpanel
			add(startPanel);
		}
	}
	
	
	
	
}



