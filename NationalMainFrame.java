import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.StyledDocument;


public class NationalMainFrame extends JFrame{
	
	String[] addBusArr = new String[4];      											//Used to get bus from addBusPanel
	String[] addStationArr = new String[4];  											//Used to get Station from addStationPanel
	int userStart, userEnd;																//Find the index of both departure and destination
	String[] start = new String[4];														//Select the array for departure
	String[] travelPlans = new String[4];												//Used when user selects their final travel plan
	String[] end = new String[4];														//Used when user selects their destination
	String[] planTotals  = new String[4];												//Used to determine the totals for final travel plans
	String chosen = "";																	//Used to hold the route plan as a string
	boolean isStart = false;															//Determine if there is a departure to enable Finalize button
	boolean isEnd = false;																//Determine if there is a destination to enable Finalize button
	boolean isBus = false;																//Determine if bus is selected
	String selectStart =" ";															//Used to find start location from within JTable
	String selectEnd = " ";																//Used to find end location from within JTable 															
	JTextArea userSelectionStart = new JTextArea("");									//Hold the departure within user interface
	JTextArea userSelectionEnd = new JTextArea("");										//Used to hold destination within user interface
	String[] selectedBus = new String[5];												//Hold selected bus information
	JTextPane finalPane = new JTextPane();												//Used to hold final route information within user interface
	JTextPane chosenBusPane = new JTextPane();											//Used to hold chosen bus information within user interface
	StyledDocument docum = chosenBusPane.getStyledDocument();							//Used to help format/style the pane
	String[] delBus = new String[5];													//Holds the chosen bus for deletion
	JButton finalize = new JButton("Finalize Travel"); 									//button to finalize the user's selection.
	String userChosenBus = "";															//Holds selection of chosen bus as a string
	Bus chosenBus = null;																//Create instance of bus
	final LongDistStationsDatabase LDSDB;												//Create instance of LongDistStationsDatabase
	Route[] routes;																		//Create instance of Routes
	
	public NationalMainFrame() throws Exception {
	    LDSDB = new LongDistStationsDatabase();
		finalize.setEnabled(false);														//Disable button until all chosen fields are true
		setLayout(new BorderLayout()); 													// set layout of frame to BorderLayout.
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
		
		userSelectionStart.setEditable(false);											//Ensure area is not editable	
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
		
		finalSelectedDepart.addActionListener(e->{																				//Set action for select departure
			setLocations sl = new setLocations( this );
			int var = JOptionPane.showConfirmDialog(null, sl,																	//create a JOptionPane window to ensure frame is disabled
                    "Select a Departure", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);								//use the setLocations as an object in JOptionPane 
			
			if(var == JOptionPane.OK_OPTION) {																					//if user selects "ok", set destination
				try {
					int row = setLocations.table.getSelectedRow();																//get row number
				String value;																									//Use value to get information from table
				for(int i = 0; i <= 3; i++) {																
				
				value = setLocations.table.getModel().getValueAt(setLocations.table.convertRowIndexToModel(row), i).toString(); //Get information from each column and set it within an array
				NationalMainFrame.this.start[i]=value;
				
				}
				this.selectStart = convertToString(this.start);																	//set info to another string
				
				userSelectionStart.setText(this.selectStart);																	//put info into JTextArea for viewing
				this.isStart = true;																							//set boolean to true for enabling finalize button
				changeFinal();																									//check to see if required info is complete to enable finalize button
				
				}catch(Exception e2) {
					JOptionPane.showMessageDialog(this, "No value was selected! ");												//if an error show message
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
		
		finalSelectedDest.addActionListener(e->{																				//set action for selecting destination button
			setLocations loc = new setLocations( this );																		//instance of setLocation
			int var = JOptionPane.showConfirmDialog(null, loc,																	//use setLocation as an object inside JOptionPane
                    "Select a Destination", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			
			if(var == JOptionPane.OK_OPTION) {																					//if selects ok, save destination information
				try {
					int row = setLocations.table.getSelectedRow();																//retrieve row number
				String value;																									//instance of value to get info from table columns

				for(int i = 0; i <= 3; i++) {
				
				value = setLocations.table.getModel().getValueAt(setLocations.table.convertRowIndexToModel(row), i).toString(); //set columns to value and input into an array
				NationalMainFrame.this.end[i]=value;
				
				}
				this.selectEnd = convertToString(this.end);																		//set info to string
				//System.out.print(this.selectEnd);
				
				userSelectionEnd.setText(this.selectEnd);																		//set departure info to Departure JTextArea for viewing
				this.isEnd = true;																								//set isEnd to true to show info was inputted
				changeFinal();																									//check to see if finalize should be enabled
				
				//System.out.print("Test" + this.end[0]);
				}catch(Exception e2) {
					JOptionPane.showMessageDialog(this, "No value was selected! ");
				}
			}
		});
		
		
		
		leftPanel.add(blah,BorderLayout.CENTER);

		
		GridLayout gridLayout = new GridLayout();
		gridLayout.setRows(9);
		gridLayout.setColumns(1);
		gridLayout.setHgap(10);
		gridLayout.setVgap(10);    
		
		JPanel console = new JPanel(gridLayout);
		JButton city = new JButton("City Routing");
		
		JButton userBus = new JButton("Select Bus");														
		userBus.addActionListener(e->{																			//Set action for selecting a bus
			selectBus sb = new selectBus();																		//Create instance of selectbus Jpanel
			int var = JOptionPane.showConfirmDialog(null, sb,													//set sb as object for JOptionPane
                    "Select a Bus", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			
			if(var == JOptionPane.OK_OPTION) {																	//if ok, set bus info from user
				try {
					int row = selectBus.busTable.getSelectedRow();												//get row number
					
					BusesDatabase bd = new BusesDatabase();														//instance of BusesDatabase
					this.chosenBus = bd.toArray()[ row ];														//set chosen bus 
    
    				for(int i = 0; i <= 4; i++) {																//set information to a value
    				
        				String value = selectBus.busTable.getModel()
        				    .getValueAt(selectBus.busTable.convertRowIndexToModel(row), i)
        				    .toString();
        				NationalMainFrame.this.selectedBus[i]=value;
        				//JOptionPane.showMessageDialog(this, arr[i]);
        				
    				}																							//Set a string according to chosen bus information
    				this.userChosenBus =
    				    "Bus Name: " + this.selectedBus[0] +
    				    "\nTank Size: " + this.selectedBus[2] +
    				    "\nCruise Consumption: " + this.selectedBus[3] +
    				    "\nCruise Speed: " + this.selectedBus[4];
    				chosenBusPane.setText("");//resets pre-existing text
    				docum.insertString(0, userChosenBus, null );												//input the information into choseBusPane								
    				
    				this.isBus = true;																			//set bus to true indicating info was selected
    				this.changeFinal();																			//check to see if finalize should be enabled
    				
				}catch(Exception e2) {
					JOptionPane.showMessageDialog(this, "No value was selected! ");
				}
				
			}
		});
		
																																					//create action to leave national bus to city bus
		city.addActionListener(e->{
		int warn = JOptionPane.showConfirmDialog(null, "Are you sure you want to leave?", "Leave National Bus Routing",JOptionPane.YES_NO_OPTION); //confirm to leave
		if(warn == JOptionPane.YES_OPTION) {																									   //if yes, close current frame and open city routing
			try {
				//BusStationMain busMain = new BusStationMain();
				BusStationMain frame = new BusStationMain();
				frame.setTitle("City Bus Routing System");
				frame.setSize(1000,550);
				frame.setLocationRelativeTo(null);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);		
				this.dispose();
			} catch (Exception e1) {
			
				e1.printStackTrace();
			}			

		}
		});
		
		JButton addBus = new JButton("Add Bus");
		addBus.addActionListener(e->{																							//create an action to add bus to database
			boolean empty = false;																								//check to see if empty after selecting ok
			boolean isChar = false;																								//check to see if a comma is used
			addBusPanel addBusPanel = new addBusPanel();																		//instance of addBusPanel
			int var = JOptionPane.showConfirmDialog(null, addBusPanel,															//input object addBusPanel into JOptionPane
	                     "Create Bus", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if(var==JOptionPane.OK_OPTION) {																					//Get info From user and input into an array
				addBusArr[0] = addBusPanel.mAndm.getText();
				addBusArr[1] = addBusPanel.tank.getText();
				addBusArr[2] = addBusPanel.cruiseTxt.getText();
				addBusArr[3] = addBusPanel.CruiseSpd.getText();
				for(int i = 0; i<addBusArr.length; i++) {																		//check to see if any parts are empty to determine if bus is saved
					if(addBusArr[i].equals("")) {
						empty = true;																							//if empty, set empty true, end search
						break;
					}
					
				}
				if(empty == true) {																								//if emtpy, warn user
					
					JOptionPane.showMessageDialog(this, "One or more of your inputs were blank. Bus was not saved!");
	
				}
				
				String busName = addBusArr[0];																					//set bus name
				
				if (busName.indexOf(',') != -1) {																				//check for comma and if so warn user
					isChar = true;
					JOptionPane.showMessageDialog(this, "Name cannot contain a comma.");
				}
				
			if(empty == false && isChar==false) {																				//if no character and no space continue
				try {																										
				Double tankSz = Double.parseDouble(addBusArr[1]);																//set tanksz, cruisecon, cruisespd to double
				Double CruiseCon = Double.parseDouble(addBusArr[2]);
				Double CruiseSpd = Double.parseDouble(addBusArr[3]);
				//String LongDist = "LD";
				 BusesDatabase bd = new BusesDatabase();																		//instance of BusesDatabase 
				 String[][] data = bd.allBuses();																				//Create array data
				 boolean same = false;																							//create boolean same
				 
				 for(int i = 0; i < data.length; i++) {																			//determine if there is already a name in the database+
					 if(busName.equalsIgnoreCase(data[i][0])) {
						 same = true;
						 break;
					 }
				 }
			if(same == true) {																									//if there is a duplicate warn user
				JOptionPane.showMessageDialog(this, "Bus is already listed in database. Bus not saved!");

			}else if(tankSz<=0 || CruiseCon<=0 || CruiseSpd<=0) {
				
					JOptionPane.showMessageDialog(this, "Tank size, Cruise Consumption and Cruise Speed must all "				//Ensure all double values are not less than or equal to 0;
							+ "be a number greater than or equal to 0.");
					
					}else {
							try {
								
									final Bus UserBus = new Bus(busName, BusType.longDistance, 									//constructor that takes makeandmodel, type, tanksize, cruisingconsumption, cruising speed
									                         tankSz, CruiseCon, CruiseSpd );
										
									bd.addBus(UserBus);																			//add bus to database
									bd.update();																				//update the database
					
								} catch (Exception e1) {
										e1.printStackTrace();
								}
							}
							
						
						}catch(Exception a) {
							JOptionPane.showMessageDialog(this, "Tank size, Cruise Consumption and Cruise Speed must "			//If less than or equal to zero, warn user
									+ "all be a number greater than or equal to 0.");
					}
				}																												//end of if
			}																													//end of ok statement
		});
		
	
		
		JButton addStation = new JButton("Add Station");
		addStation.addActionListener(e->{																						//action for adding a station
			boolean empty = false;																								//check if empty value
			String typeChose = "";																								//hold type of station gas or city
			double longit;																										//hold longitude
			double lat;																											//hold latitude
			String bsName = "";																									//hold bus station name
			boolean isGasSt = false;																							//determine if gas station
			boolean same = false;																								//determine if type and name is the same (duplicates)
			addStationPanel addStationPanel = new addStationPanel();															//instance of addStationPanel
			int var = JOptionPane.showConfirmDialog(null, addStationPanel,														//put instance of addStationPanel as object inside JOptionPane
	                     "Create Station", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if(var == JOptionPane.OK_OPTION) {																					//if ok, set station and add to database
				addStationArr[0] = addStationPanel.stName.getText();															//get info name
				if(addStationPanel.city.isSelected()) {																			//determine if city or gas
					typeChose = "city";																							//if city selected set typeChose to city
				}else if(addStationPanel.gas.isSelected()){																		//if gas than set typeChose to gas
					typeChose = "gas";
					isGasSt = true;
				}
				bsName = addStationArr[0];																						//set bus station name, type, longitude,latitude to an array
				addStationArr[1] = typeChose;																					
				addStationArr[2] = addStationPanel.longitTxt.getText();
				addStationArr[3] = addStationPanel.latTxt.getText();	

				for(int i = 0; i<addStationArr.length; i++) {																	//ensure there are not any blanks
					if(addStationArr[i].equals("")) {
						empty = true;
						break;
					}
				}
				
				
				
				//used to determine if the name and type are the same and if they are reject request.
				String[][] data = LDSDB.allStations();																			//retrieve all stations
				for(int i = 0; i < data.length; i++)																			//check to see if there are duplicates
				{
					if(data[i][0].equalsIgnoreCase(bsName) && data[i][1].equalsIgnoreCase(addStationArr[1]))
					{	
						same=true;
						break;
					}
				}
					
				
				
				longit = Double.parseDouble(addStationPanel.longitTxt.getText());												//get longitude
				lat = Double.parseDouble(addStationPanel.latTxt.getText());														//get latitude
				
				
																																//boundary conditions resulting in failure of request.
				if(same==true) {									
					JOptionPane.showMessageDialog(this, "Name is already listed in Station Database for Long Distance.");		//duplicates warn user
				}else if (bsName.indexOf(',') != -1) {
					JOptionPane.showMessageDialog(this, "Name cannot contain a comma.");										//if , warn user
				}else if(empty == true) { //Check to see if array is empty proving user didn't fill out completely
					
					JOptionPane.showMessageDialog(this, "One or more of your inputs were blank. Station was not saved!");       //if empty value, warn user
	
				}else if(longit<-180 || longit>180){
					JOptionPane.showMessageDialog(this, "Longitude must be greater than -180 and less than 180 inclusive. "     //if outside longitude values warn user
							+ "Station not saved!");  

				}else if(lat<-90 || lat>90){
					JOptionPane.showMessageDialog(this, "Latitude must be greater than -90 and less than 90 inclusive. "        //if outside latitude values warn user
							+ "Station not saved!");

				}else{
				    try {
    					LDSDB.addNewLDStation(lat,longit,isGasSt,bsName);														//add information to stations database
    					LDSDB.update();																							//update database
    				} catch( Exception e1 ){
    				    e1.printStackTrace();
				    }
				}
			}
			
		});
		
		JButton addConn = new JButton("Add Connection");
		addConn.addActionListener(e->{																							//add connection to another city
			if( this.start[0] == null || this.end[0] == null )																	//if either start of end is empty warn user
			{
			    JOptionPane.showMessageDialog( null, "Stations not selected" );
			    return;
			}

		    try {
				int userStartCon0based = findStationNumber(this.start[0]) - 1;													//find start point for connection
				int userEndCon0based   = findStationNumber(this.end[0]) - 1;													//find end point for connection
				if( LDSDB.connected( userStartCon0based, userEndCon0based ) )													//warn if there is already a connection
				    JOptionPane.showMessageDialog( null, "Already connected!" );
				else
				{
    				LDSDB.createConnection( userStartCon0based, userEndCon0based );												//create connection		
    				LDSDB.update();																								//update database
    				JOptionPane.showMessageDialog( null,																		//validate to user connection
    				    this.start[0] + " connected to " + this.end[0] );
				}	
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		});
		
		JButton deleteConn = new JButton( "Delete Connection" );
		deleteConn.addActionListener( e->{																						//delete connection action
		    if( this.start[0] == null || this.end[0] == null )																	//if start or end is null, warn user
            {
                JOptionPane.showMessageDialog( null, "Stations not selected" );
                return;
            }

            try {
                int userStartCon0based = findStationNumber(this.start[0]) - 1;													//find start location
                int userEndCon0based   = findStationNumber(this.end[0]) - 1;													//find end location
                if( !LDSDB.connected( userStartCon0based, userEndCon0based ) )													//determine if there is a connection
                    JOptionPane.showMessageDialog( null, "Already not connected!" );
                else
                {
                    LDSDB.deleteConnection( userStartCon0based, userEndCon0based );												//delete connection
                    LDSDB.update();																								//update database
                    JOptionPane.showMessageDialog( null,																		//validate to user connection deleted
                        this.start[0] + " disconnected from " + this.end[0] );
                }     
		    }
		    catch( Exception e3 )
		    {
		        e3.printStackTrace();
		    }
		});
		
		JButton deleteBus = new JButton("Delete Bus");
		deleteBus.addActionListener(e->{																						//delete bus from database
		
			selectBus sb = new selectBus();																						//instance of selectbus
			int var = JOptionPane.showConfirmDialog(null, sb,																	//add selectBus object to JOptionPane
                    "Delete a Bus", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			
			if(var == JOptionPane.OK_OPTION) {																					//when user selects ok, delete the bus
				try {
					int row = selectBus.busTable.getSelectedRow();
				String value;

				for(int i = 0; i <= 4; i++) {
				
				value = selectBus.busTable.getModel().getValueAt(selectBus.busTable.convertRowIndexToModel(row), i).toString();	//get value from table
				delBus[i]=value;																								//set value to array
				//JOptionPane.showMessageDialog(this, arr[i]);
				
				}
				
				String name = delBus[0];																						//get bus name
				BusesDatabase bd = new BusesDatabase();		
				int index = 0;																									//find index of bus in database
			for(int i = 0; i < bd.longDistBuses().length; i++) {																//look for the bus in database
				
				String bus = bd.longDistBuses()[i][0];
				 if(bus.equals(name)) {
					 break;
				 }
				 index++;																										//if bus not found increase index
				 
			}
				bd.delete(index);																								//if bus found, find bus index in database and delete
				bd.update();																									//update database
				
				}catch(Exception e2) {
					JOptionPane.showMessageDialog(this, "No value was selected! ");
				}
				
			}
			
		});
		console.add(deleteBus);
		
		JButton deleteStation = new JButton("Delete Station");
		deleteStation.addActionListener(e->{																					//delete station action

			deleteStations sl = new deleteStations( this );																		//create instance of deleteStations
			String []deleteST = new String[4];																					//create array for info of station
			int var = JOptionPane.showConfirmDialog(null, sl,																	//add instance of deleteStations to JOptionPane
                    "Delete a Station", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			
			if(var == JOptionPane.OK_OPTION) {
				try {
					int row = sl.table.getSelectedRow();																		//find row that was selected
				String value;																									//instance of string value 

				for(int i = 0; i <= 3; i++) {																					//for each column set to value and add to array
				
				value = sl.table.getModel().getValueAt(sl.table.convertRowIndexToModel(row), i).toString();
				deleteST[i]=value;

				
				}
				
				String name = deleteST[0];																						//get name of station

				int index = 0;																									//set index to zero
			for(int i = 0; i <= LDSDB.allStations().length; i++) {																//iterate through stations and find the name matching user selection	
				
				String station = LDSDB.allStations()[i][0];																
				 if(station.equals(name)) {																						//break if found name
					 break;
				 }
				 index++;																										//if name not found, increase index
				 
			}

				LDSDB.delete(index);																							//if name found, get index and delete
				LDSDB.update();																									//update database
				
				}catch(Exception e2) {
					JOptionPane.showMessageDialog(this, "No value was selected! ");
				}
				
			}
		});
		
		
		
		
		finalize.addActionListener(e ->{																						//finalize action

			
			if( this.start[0].equals( this.end[0] ) )																			//if start and end are equal warn user
			{
			    JOptionPane.showMessageDialog( null,
			        "Departure is same as destination.\nNo route needed." );
			    return;
			}
		    
			try {
				this.userStart = findStationNumber(this.start[0]);																//find station number and set to userStart
				this.userEnd = findStationNumber(this.end[0]);																	//find end station number and set to userEnd 
	
			} catch (Exception e5) {

				JOptionPane.showMessageDialog(null, "One of the selections are null or empty", "Alert!", getDefaultCloseOperation());
			} 																
			
			
			
			try
			{
				routes = LongDistTravel.implementTravel( LDSDB, this.userStart, this.userEnd);									//set the routes 
				for( int i = 0; i < 3; ++i )																					//check to see if alternatives
				{
					if( routes[i] == null )
					{
						this.travelPlans[i] = "No alternative found";
						this.planTotals[i]  = this.travelPlans[i];
					}
					else
					{
						this.travelPlans[i] = routes[i].display( this.chosenBus );
						this.planTotals[i]  = routes[i].totals( this.chosenBus );
					}
				}
			}
			catch (Exception e1)
			{
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
			viewA.addActionListener(e2->   {																								//action for viewing first option																		

				JTextArea bestRoute = new JTextArea(this.travelPlans[0]);																	//get first option and set it to pane
				
				JScrollPane paneOne = new JScrollPane(bestRoute);																
				paneOne.setPreferredSize(new Dimension(400,600));
				JOptionPane.showMessageDialog(null,paneOne,"Option A",JOptionPane.PLAIN_MESSAGE);											//show user there option
				
			});
			holdOps.add(first);
			
			JButton viewB  = new JButton("View B");
			wrapB.add(viewB);
			second.add(optionB,BorderLayout.NORTH);
			second.add(wrapB,BorderLayout.SOUTH);
			viewB.addActionListener(e3->{																									//show option B
				JTextArea secondRoute = new JTextArea(this.travelPlans[1]); 																//show second travel plan

				JScrollPane paneTwo = new JScrollPane(secondRoute);
				paneTwo.setPreferredSize(new Dimension(400,600));
				JOptionPane.showMessageDialog(null,paneTwo,"Option B",JOptionPane.PLAIN_MESSAGE); 											//put second option in JOptionPane
				});

			holdOps.add(second);
			
			JButton viewC = new JButton("View C");
			wrapC.add(viewC);
			three.add(optionC,BorderLayout.NORTH);
			three.add(wrapC,BorderLayout.SOUTH);
			viewC.addActionListener(e4->{																									//show option C
				
				JTextArea thirdRoute = new JTextArea(this.travelPlans[2]);																	//show third option

				JScrollPane paneThree = new JScrollPane(thirdRoute);
				paneThree.setPreferredSize(new Dimension(400,600));
				JOptionPane.showMessageDialog(null,paneThree,"Option C",JOptionPane.PLAIN_MESSAGE);											//show third option in JOptionPane
			});
			holdOps.add(three);
			showOptions.add(holdOps,BorderLayout.CENTER);
			
			
			JPanel holdButtons = new JPanel(new FlowLayout());
			JPanel approveWrap = new JPanel();
			JPanel cancelWrap = new JPanel();
			JButton approve = new JButton("OK");
			approveWrap.add(approve);
			holdButtons.add(approveWrap);
			approve.addActionListener(c->{																									//set action to ok button
			    try {
        			if(optionA.isSelected())																								//if A is selected, auto generate gas station
        			{
        			    LongDistTravel.insertGasStations( routes[0], this.chosenBus, LDSDB );													
        			    this.chosen = routes[0].display( this.chosenBus );																	//set chosen to option A's value
        			}
        			else if(optionB.isSelected())																							//if B is chosen autogenerate gas stations
        			{
        			    LongDistTravel.insertGasStations( routes[1], this.chosenBus, LDSDB );
        			    this.chosen = routes[1].display( this.chosenBus );																	//set chosen to option B values
        			}
        			else if(optionC.isSelected())																							//if C is chosen autogenerate gas stations
        			{
        			    LongDistTravel.insertGasStations( routes[2], this.chosenBus, LDSDB );
        			    this.chosen = routes[2].display( this.chosenBus );																	//set chosen to option C values
        			}
			    } catch( Exception e1 ) {
			        e1.printStackTrace();
			    }
    			
    			finalPane.setText( this.chosen );																							//set Pane to chosen route for user viewing
				JComponent comp = (JComponent) c.getSource();
				Window win = SwingUtilities.getWindowAncestor(comp);
				win.dispose();
			});
			JButton cancel = new JButton("Cancel");															
			cancelWrap.add(cancel);
			holdButtons.add(cancelWrap);
			showOptions.add(holdButtons,BorderLayout.SOUTH);
			cancel.addActionListener(f1->{																									//cancel action sets chosen to empty string
				this.chosen = "";
				JComponent comp = (JComponent) f1.getSource();
				  Window win = SwingUtilities.getWindowAncestor(comp);
				  win.dispose();																											//close JOptionPane
			});
			
		});//end of finalize
		
		JButton showConnections = new JButton("Show Connections");
		showConnections.addActionListener(e->{
			viewCon s1 = new viewCon(this);																									//instance of viewCon Jpanel
			JOptionPane.showMessageDialog(null,s1);																							//set instance as object inside JOptionPane

			
			
		});

		JButton showAllStations = new JButton("View Stations");
		showAllStations.addActionListener(e->{
			deleteStations s1 = new deleteStations(this);		    																		//Although not deleting stations, it will generate the stations for viewing.
			JOptionPane.showMessageDialog(null,s1);																							//Use JOptionPane to add the object and ensure main frame is unselectable.

			
		});
		
		
		JButton clear = new JButton("Clear All");
		clear.addActionListener(e->{																										//set clear action 
			if(!this.chosen.equals("") || !userSelectionStart.equals("") || !userSelectionStart.equals("") || !userChosenBus.equals("")){	//if anything does not equal empty, delete all info and set all strings to empty strings and Panes to empty
				this.chosen="";
				userSelectionStart.setText("");;
				userSelectionEnd.setText("");
				finalPane.setText("");
				chosenBusPane.setText("");
				userChosenBus = "";
				selectEnd = "";
				selectStart="";
				this.isStart = false;																										//reset all booleans to false for enabling finalize button
				this.isEnd = false;
				this.isBus = false;
				JOptionPane.showMessageDialog(null, "All selections cleared.", "Success!", getDefaultCloseOperation());						//validate clear to user
				changeFinal();
			}else {
				JOptionPane.showMessageDialog(null, "Selection is already empty!", "Alert!", getDefaultCloseOperation());					//warn user if already empty
				
			}
		});
		
		//leftConsole.add(userBus);
		leftConsole.add(addBus);
		leftConsole.add(deleteBus);
		leftConsole.add(addStation);
		leftConsole.add(deleteStation);
		leftConsole.add(showAllStations);
		
		JPanel holdBusBt = new JPanel();
		holdBusBt.add(userBus);
		
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
		holdBuses.add(holdBusBt,BorderLayout.SOUTH);
		holdBusBt.setBorder(BorderFactory.createLineBorder(Color.black));
		holdBuses.setBorder(BorderFactory.createLineBorder(Color.black));
		

		showFinalSelection.add(holdFinalPane);
		holdViews.add(holdBuses,BorderLayout.NORTH);
		holdViews.add(showFinalSelection,BorderLayout.CENTER);
		
		
	//console.add(userBus);
		console.add(finalize);
		console.add(showConnections);
		console.add(addConn);
		console.add(deleteConn);
		console.add(clear);
		console.add(city);
		master.add(leftConsole);
		master.add(leftPanel);
		master.add(console);
		master.add(holdViews);
		
		
		add(master,BorderLayout.CENTER);
		//add(bottomPanel,BorderLayout.SOUTH);
		
		
		
	}
		
	public String convertToString(String[] arr) {																			//converts array to appropriate string
		return "Bus Station: " + arr[0]  +"\nType: " + arr[1] +"\n Longitude: " + arr[2] + "\n Latitude: " + arr[3];
	}
	
	public void changeFinal() {																								//determine if all booleans are satisfied to enable/disable finalize button 
	    if( this.isStart && this.isEnd && this.isBus ) {
			this.finalize.setEnabled(true);
		}else
			this.finalize.setEnabled(false);
	}
	
    public int findStationNumber(String station) throws Exception															//find station number in database
    //Method to return a number or position to get route started.
    { 								
		String[][] temp = LDSDB.allStations();																				//create temp two dimensional array
		String arrStat = "";																								//set a station string
		for(int i = 0; i<temp.length; i++) {																				//cycle through array to find station
			arrStat = temp[i][0];
			if(arrStat.equals(station))
				return i+1;																									//return position of station inside database if found
		}
		throw new Exception( "Station not found" );																			//if station not found, warn user
    }
    	
	
	class addBusPanel extends JPanel {																						//create addBusPanel 
		JTextField mAndm;																									//holds user input for make and model
		JTextField tank;																									//hold user input for tank size
		JTextField cruiseTxt;																								//hold user input for cruise consumption
		JTextField CruiseSpd;																								//hold user input for cruise speed
		
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
	
	class addStationPanel extends JPanel {																				//Create JPanel for addStation
		JTextField stName;																								//User input for station name
		JTextField stType;																								//User input for station type
		JTextField longitTxt;																							//User input for longitude 
		JTextField latTxt;																								//User input for latitude
		JRadioButton city = new JRadioButton("City");																	//Create radio button for city
		JRadioButton gas = new JRadioButton("Gas");																		//Create radio button for gas
		ButtonGroup group = new ButtonGroup();																			//put buttons as a group
		addStationPanel(){
			JPanel radio = new JPanel();
			JPanel input = new JPanel();
			
			input.setLayout(new GridLayout(4,2));
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
			
			JPanel holdLatLong = new JPanel();
			
			String note = 																								//Create a note for user understanding of longitude and latitude
			    "<html><br/><b>Note:</b><br/>Longitudes are positive in the east " +
			    "but negative in the west.<br/>Latitudes are positive in the north " +
			    "but negative in the south.<br/>That means all locations in the " +
			    "continental USA will have positive<br/>" +
	            "latitude but negative longitude!</html>";
			
			JLabel latLongNote = new JLabel( note );
			
			input.add(station);
			input.add(stName);
			input.add(type);
			input.add(radio);
			input.add(longit);
			input.add(longitTxt);
			input.add(lat);
			input.add(latTxt);
		
			setLayout(new BorderLayout());
			
			add(input, BorderLayout.NORTH);
			add(latLongNote, BorderLayout.SOUTH);
			
			//new Bus( MakeAndModel, BusType.longDistance, )
			
			
			
		}
	}
	

	
	static class selectBus extends JPanel {
		
		
																				
		String[] busHeader =																												//Create string
		    { "Name",
		      "Type",
		      "<html>Tank Size<br/>(gallons)</html>",
		      "<html>Cruise<br/>Consumption<br/>(gallons/hour)</html>",
		      "<html>Cruise Speed<br/>(mph)</html>",
		      "<html>Range<br/>(miles)</html>" }; 
		static JTable busTable;
		
		selectBus(){
	
			try {
			BusesDatabase bsd = new BusesDatabase();
			JLabel selectBus = new JLabel("Select Bus");
			String [][]busData = bsd.longDistBuses();	
			DefaultTableModel busModel = new DefaultTableModel(busData, busHeader); 														//add data and header to table model
			
			TableRowSorter<TableModel> busSorter = new TableRowSorter<>(busModel); 															//create method for sorting
			
			busTable = new JTable(busModel);				 																				//Create JTable using model
			busTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); 																	//Resize to fit columns
			
			busTable.setPreferredScrollableViewportSize(new Dimension(550, 150)); 															//set dimensions of scrollable
			busTable.getTableHeader().setPreferredSize( new Dimension(550, 60));
			
			busTable.setRowSorter(busSorter);																								//add sorter to table
			
			JScrollPane busScroll = new JScrollPane(busTable);																				//set scrollpane for JTable
			busScroll.setVisible(true);																										//make scroll appear
			
			busTable.setRowSelectionAllowed(true);																							//Able to select the specific row
			
			JTextField searchBoxBus = new JTextField(20);																					//create text box for user input
				
			searchBoxBus.addActionListener(e -> {																							//event to sort table
				String text = "(?i)" + Pattern.quote(searchBoxBus.getText()); 																// (?i) = case-insensitive
				busSorter.setRowFilter(RowFilter.regexFilter(text));		   																//sort table according to text
			});
			JPanel holdBus = new JPanel();
			JPanel extraPanel = new JPanel();

			holdBus.setLayout(new BorderLayout());																							//set layout for startpanel
			holdBus.add(selectBus,BorderLayout.NORTH);																						//add startHeader to startPanel make it top
			holdBus.add(busScroll,BorderLayout.CENTER);																						//add scroll (Jtable) to centered startpanel
			holdBus.add(searchBoxBus,BorderLayout.SOUTH);																					//add searchbox to bottom of startpanel
			extraPanel.setLayout(new BorderLayout());
			extraPanel.add(holdBus,BorderLayout.WEST);
			add(extraPanel);
			
			
			
			
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			
	
			
		}
	}
	
	static class setLocations extends JPanel{
		
		static JTable table;
		
		setLocations( NationalMainFrame nmf ){

			String[][] data = nmf.LDSDB.busStations();
				 
			nmf.finalize.setEnabled(false); 							  																//disable finalize button
			JPanel master = new JPanel(new FlowLayout(FlowLayout.LEFT)); 																//Create panel inside Frame 
			
			JPanel leftPanel = new JPanel(new BorderLayout()); 																			//set left Panel in master
			JPanel startPanel = new JPanel(); 				   																			//set start panel to hold JTable departure
			JPanel endPanel = new JPanel();					   																			//set end panel to hold JTable Destination
			

			
			JLabel startHeader = new JLabel("Select Departure Location");																//create 2-dim array to hold data
			String[] header= {"Station","Type","Longitude","Latitude"}; 																//header for table
			
			
			DefaultTableModel model = new DefaultTableModel(data, header); 																//add data and header to table model
			
			TableRowSorter<TableModel> sorter = new TableRowSorter<>(model); 															//create method for sorting
			
			table = new JTable(model);				 																					//Create JTable using model
			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); 																	//Resize to fit columns
			table.setPreferredScrollableViewportSize(new Dimension(300,130)); 															//set dimensions of scrollable
			
			table.setRowSorter(sorter);																									//add sorter to table
			
			JScrollPane scroll = new JScrollPane(table);																				//set scrollpane for JTable
			scroll.setVisible(true);																									//make scroll appear
			
			table.setRowSelectionAllowed(true);																							//Able to select the specific row
			
			JTextField searchBox = new JTextField(20);																					//create text box for user input
				
			searchBox.addActionListener(e -> {																							//event to sort table
				String text = "(?i)" + Pattern.quote(searchBox.getText()); 																// (?i) = case-insensitive
				sorter.setRowFilter(RowFilter.regexFilter(text));		  															    //sort table according to text
			});
			
			startPanel.setLayout(new BorderLayout());																					//set layout for startpanel
			startPanel.add(startHeader,BorderLayout.NORTH);																				//add startHeader to startPanel make it top
			startPanel.add(scroll,BorderLayout.CENTER);																					//add scroll (Jtable) to centered startpanel
			startPanel.add(searchBox,BorderLayout.SOUTH);																				//add searchbox to bottom of startpanel
			add(startPanel);
		}
	}
	
	static class deleteStations extends JPanel{
	    
		static JTable table;
		
		deleteStations( NationalMainFrame nmf ){

			String[][] data = nmf.LDSDB.allStations();
				 
			//nmf.finalize.setEnabled(false); 							 																//disable finalize button
			JPanel master = new JPanel(new FlowLayout(FlowLayout.LEFT)); 																//Create panel inside Frame 
			
			JPanel leftPanel = new JPanel(new BorderLayout()); 																			 //set left Panel in master
			JPanel startPanel = new JPanel(); 				   																			 //set start panel to hold JTable departure
			JPanel endPanel = new JPanel();					   																			 //set end panel to hold JTable Destination
			

			
			JLabel startHeader = new JLabel("Select Departure Location");
			String[] header= {"Station","Type","Longitude","Latitude"}; 																//header for table
			
			
			DefaultTableModel model = new DefaultTableModel(data, header); 																//add data and header to table model
			
			TableRowSorter<TableModel> sorter = new TableRowSorter<>(model); 															//create method for sorting
			
			table = new JTable(model);																					 				//Create JTable using model
			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); 																	//Resize to fit columns
			table.setPreferredScrollableViewportSize(new Dimension(300,100)); 															//set dimensions of scrollable
			
			table.setRowSorter(sorter);																									//add sorter to table
			
			JScrollPane scroll = new JScrollPane(table);																				//set scrollpane for JTable
			scroll.setVisible(true);																									//make scroll appear
			
			table.setRowSelectionAllowed(true);																							//Able to select the specific row
			
			JTextField searchBox = new JTextField(20);																					//create text box for user input
				
			searchBox.addActionListener(e -> {																							//event to sort table
				String text = "(?i)" + Pattern.quote(searchBox.getText()); 																// (?i) = case-insensitive
				sorter.setRowFilter(RowFilter.regexFilter(text));		   																//sort table according to text
			});
			
			startPanel.setLayout(new BorderLayout());																					//set layout for startpanel
			startPanel.add(startHeader,BorderLayout.NORTH);																				//add startHeader to startPanel make it top
			startPanel.add(scroll,BorderLayout.CENTER);																					//add scroll (Jtable) to centered startpanel
			startPanel.add(searchBox,BorderLayout.SOUTH);																				//add searchbox to bottom of startpanel
			add(startPanel);
		}
	}
	
	static class viewCon extends JPanel{
		JTextArea holdCons = new JTextArea();																							//create a JTestArea to hold connections
		public viewCon(NationalMainFrame frame){
				
			holdCons.setText(frame.LDSDB.connectionsString());																			//set connections to string
			holdCons.setEditable(false);																								//disable ability to change strings inside JTextArea
			holdCons.setBorder(BorderFactory.createLineBorder(Color.black));															//create border
			add(holdCons);
			
		}
	}
	
}
