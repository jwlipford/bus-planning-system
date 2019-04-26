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
	boolean isBus = false;
	String selectStart =" ";
	String selectEnd = " ";
	JTextArea userSelectionStart = new JTextArea("");
	JTextArea userSelectionEnd = new JTextArea("");
	String[] selectedBus = new String[5];
	JTextPane finalPane = new JTextPane();
	JTextPane chosenBusPane = new JTextPane();
	StyledDocument docum = chosenBusPane.getStyledDocument();
	String[] delBus = new String[5];
	JButton finalize = new JButton("Finalize Travel"); 		// button to finalize the user's selection.
	String userChosenBus = "";
	Bus chosenBus = null;
	final LongDistStationsDatabase LDSDB;
	Route[] routes;
	
	public NationalMainFrame() throws Exception {
	    LDSDB = new LongDistStationsDatabase();
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
			setLocations sl = new setLocations( this );
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
				changeFinal();
				
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
			setLocations loc = new setLocations( this );
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
				changeFinal();
				
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
		userBus.addActionListener(e->{
			selectBus sb = new selectBus();
			int var = JOptionPane.showConfirmDialog(null, sb,
                    "Select a Bus", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			
			if(var == JOptionPane.OK_OPTION) {
				try {
					int row = selectBus.busTable.getSelectedRow();
					
					BusesDatabase bd = new BusesDatabase();
					this.chosenBus = bd.toArray()[ row ];
    
    				for(int i = 0; i <= 4; i++) {
    				
        				String value = selectBus.busTable.getModel()
        				    .getValueAt(selectBus.busTable.convertRowIndexToModel(row), i)
        				    .toString();
        				NationalMainFrame.this.selectedBus[i]=value;
        				//JOptionPane.showMessageDialog(this, arr[i]);
        				
    				}
    				this.userChosenBus =
    				    "Bus Name: " + this.selectedBus[0] +
    				    "\nTank Size: " + this.selectedBus[2] +
    				    "\nCruise Consumption: " + this.selectedBus[3] +
    				    "\nCruise Speed: " + this.selectedBus[4];
    				chosenBusPane.setText("");//resets pre-existing text
    				docum.insertString(0, userChosenBus, null );
    				
    				this.isBus = true;
    				this.changeFinal();
    				
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
			boolean isChar = false;
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
				
				if (busName.indexOf(',') != -1) {
					isChar = true;
					JOptionPane.showMessageDialog(this, "Name cannot contain a comma.");
				}
				
			if(empty == false && isChar==false) {	
				try {
				Double tankSz = Double.parseDouble(addBusArr[1]);
				Double CruiseCon = Double.parseDouble(addBusArr[2]);
				Double CruiseSpd = Double.parseDouble(addBusArr[3]);
				//String LongDist = "LD";
				 BusesDatabase bd = new BusesDatabase();
				 String[][] data = bd.allBuses();
				 boolean same = false;
				 
				 for(int i = 0; i < data.length; i++) {
					 if(busName.equalsIgnoreCase(data[i][0])) {
						 same = true;
						 break;
					 }
				 }
			if(same == true) {
				JOptionPane.showMessageDialog(this, "Bus is already listed in database. Bus not saved!");

			}else if(tankSz<=0 || CruiseCon<=0 || CruiseSpd<=0) {
				
					JOptionPane.showMessageDialog(this, "Tank size, Cruise Consumption and Cruise Speed must all be a number greater than or equal to 0.");
					
					}else {
							try {
								
									final Bus UserBus = new Bus(busName, BusType.longDistance, //constructor that takes makeandmodel, type, tanksize, cruisingconsumption, cruising speed
									                         tankSz, CruiseCon, CruiseSpd );
										
									bd.addBus(UserBus);
									bd.update();
					
								} catch (Exception e1) {
										e1.printStackTrace();
								}
							}
							
						
						}catch(Exception a) {
							JOptionPane.showMessageDialog(this, "Tank size, Cruise Consumption and Cruise Speed must all be a number greater than or equal to 0.");
					}
				}//end of if
			}//end of ok statement
		});
		
	
		
		JButton addStation = new JButton("Add Station");
		addStation.addActionListener(e->{
			boolean empty = false;
			String typeChose = "";
			double longit;
			double lat;
			String bsName = "";
			boolean isGasSt = false;
			boolean same = false;
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
				bsName = addStationArr[0];
				addStationArr[1] = typeChose;
				addStationArr[2] = addStationPanel.longitTxt.getText();
				addStationArr[3] = addStationPanel.latTxt.getText();	

				for(int i = 0; i<addStationArr.length; i++) {
					if(addStationArr[i].equals("")) {
						empty = true;
						break;
					}
				}
				
				
				
				//used to determine if the name and type are the same and if they are reject request.
				String[][] data = LDSDB.allStations();
				for(int i = 0; i < data.length; i++)
				{
					if(data[i][0].equalsIgnoreCase(bsName) && data[i][1].equalsIgnoreCase(addStationArr[1]))
					{	
						same=true;
						break;
					}
				}
					
				
				
				longit = Double.parseDouble(addStationPanel.longitTxt.getText());
				lat = Double.parseDouble(addStationPanel.latTxt.getText());
				
				
				//boundary conditions resulting in failure of request.
				if(same==true) {
					JOptionPane.showMessageDialog(this, "Name is already listed in Station Database for Long Distance.");
				}else if (bsName.indexOf(',') != -1) {
					JOptionPane.showMessageDialog(this, "Name cannot contain a comma.");
				}else if(empty == true) { //Check to see if array is empty proving user didn't fill out completely
					
					JOptionPane.showMessageDialog(this, "One or more of your inputs were blank. Station was not saved!");
	
				}else if(longit<-180 || longit>180){
					JOptionPane.showMessageDialog(this, "Longitude must be greater than -180 and less than 180 inclusive. Station not saved!");

				}else if(lat<-90 || lat>90){
					JOptionPane.showMessageDialog(this, "Latitude must be greater than -90 and less than 90 inclusive. Station not saved!");

				}else{
				    try {
    					LDSDB.addNewLDStation(lat,longit,isGasSt,bsName);
    					LDSDB.update();
    				} catch( Exception e1 ){
    				    e1.printStackTrace();
				    }
				}
			}
			
		});
		
		JButton addConn = new JButton("Add Connection");
		addConn.addActionListener(e->{
			if( this.start[0] == null || this.end[0] == null )
			{
			    JOptionPane.showMessageDialog( null, "Stations not selected" );
			    return;
			}

		    try {
				int userStartCon0based = findStationNumber(this.start[0]) - 1;
				int userEndCon0based   = findStationNumber(this.end[0]) - 1;
				if( LDSDB.connected( userStartCon0based, userEndCon0based ) )
				    JOptionPane.showMessageDialog( null, "Already connected!" );
				else
				{
    				LDSDB.createConnection( userStartCon0based, userEndCon0based );
    				LDSDB.update();
    				JOptionPane.showMessageDialog( null,
    				    this.start[0] + " connected to " + this.end[0] );
				}	
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		});
		
		JButton deleteConn = new JButton( "Delete Connection" );
		deleteConn.addActionListener( e->{
		    if( this.start[0] == null || this.end[0] == null )
            {
                JOptionPane.showMessageDialog( null, "Stations not selected" );
                return;
            }

            try {
                int userStartCon0based = findStationNumber(this.start[0]) - 1;
                int userEndCon0based   = findStationNumber(this.end[0]) - 1;
                if( !LDSDB.connected( userStartCon0based, userEndCon0based ) )
                    JOptionPane.showMessageDialog( null, "Already not connected!" );
                else
                {
                    LDSDB.deleteConnection( userStartCon0based, userEndCon0based );
                    LDSDB.update();
                    JOptionPane.showMessageDialog( null,
                        this.start[0] + " disconnected from " + this.end[0] );
                }     
		    }
		    catch( Exception e3 )
		    {
		        e3.printStackTrace();
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
				BusesDatabase bd = new BusesDatabase();
				int index = 0;
			for(int i = 0; i < bd.longDistBuses().length; i++) {
				
				String bus = bd.longDistBuses()[i][0];
				 if(bus.equals(name)) {
					 break;
				 }
				 index++;
				 
			}
				bd.delete(index);
				bd.update();
				
				}catch(Exception e2) {
					JOptionPane.showMessageDialog(this, "No value was selected! ");
				}
				
			}
			
		});
		console.add(deleteBus);
		
		JButton deleteStation = new JButton("Delete Station");
		deleteStation.addActionListener(e->{

			deleteStations sl = new deleteStations( this );
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

				int index = 0;
			for(int i = 0; i <= LDSDB.allStations().length; i++) {
				
				String station = LDSDB.allStations()[i][0];
				 if(station.equals(name)) {
					 break;
				 }
				 index++;
				 
			}

				LDSDB.delete(index);
				LDSDB.update();
				
				}catch(Exception e2) {
					JOptionPane.showMessageDialog(this, "No value was selected! ");
				}
				
			}
		});
		
		
		
		
		finalize.addActionListener(e ->{
			//String[] options = new String[]{"View A", "View B", "View C", "Cancel"};
			//JOptionPane.showOptionDialog(null, "Message", "Choose Preferred Route", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			
			if( this.start[0].equals( this.end[0] ) )
			{
			    JOptionPane.showMessageDialog( null,
			        "Departure is same as destination.\nNo route needed." );
			    return;
			}
		    
			try {
				this.userStart = findStationNumber(this.start[0]);
				this.userEnd = findStationNumber(this.end[0]);
	
			} catch (Exception e5) {

				JOptionPane.showMessageDialog(null, "One of the selections are null or empty", "Alert!", getDefaultCloseOperation());
			} 																	//retrieve the position of the station for start location
			
			
			
			try
			{
				routes = LongDistTravel.implementTravel( LDSDB, this.userStart, this.userEnd);
				for( int i = 0; i < 3; ++i )
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
			    try {
        			if(optionA.isSelected())
        			{
        			    LongDistTravel.insertGasStations( routes[0], this.chosenBus, LDSDB );
        			    this.chosen = routes[0].display( this.chosenBus );
        			}
        			else if(optionB.isSelected())
        			{
        			    LongDistTravel.insertGasStations( routes[1], this.chosenBus, LDSDB );
        			    this.chosen = routes[1].display( this.chosenBus );
        			}
        			else if(optionC.isSelected())
        			{
        			    LongDistTravel.insertGasStations( routes[2], this.chosenBus, LDSDB );
        			    this.chosen = routes[2].display( this.chosenBus );
        			}
			    } catch( Exception e1 ) {
			        e1.printStackTrace();
			    }
    			
    			finalPane.setText( this.chosen );
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
		
		JButton showConnections = new JButton("Show Connections");
		showConnections.addActionListener(e->{
			viewCon s1 = new viewCon(this);											//Although not deleting stations, it will generate the stations for viewing.
			JOptionPane.showMessageDialog(null,s1);									//Use JOptionPane to add the object and ensure main frame is unselectable.

			
			
		});

		JButton showAllStations = new JButton("View Stations");
		showAllStations.addActionListener(e->{
			deleteStations s1 = new deleteStations(this);				//Although not deleting stations, it will generate the stations for viewing.
			JOptionPane.showMessageDialog(null,s1);					//Use JOptionPane to add the object and ensure main frame is unselectable.

			
		});
		
		
		JButton clear = new JButton("Clear All");
		clear.addActionListener(e->{
			if(!this.chosen.equals("") || !userSelectionStart.equals("") || !userSelectionStart.equals("") || !userChosenBus.equals("")){
				this.chosen="";
				userSelectionStart.setText("");;
				userSelectionEnd.setText("");
				finalPane.setText("");
				chosenBusPane.setText("");
				userChosenBus = "";
				selectEnd = "";
				selectStart="";
				this.isStart = false;
				this.isEnd = false;
				this.isBus = false;
				JOptionPane.showMessageDialog(null, "All selections cleared.", "Success!", getDefaultCloseOperation());
				changeFinal();
			}else {
				JOptionPane.showMessageDialog(null, "Selection is already empty!", "Alert!", getDefaultCloseOperation());
				
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
		
	public String convertToString(String[] arr) {
		return "Bus Station: " + arr[0]  +"\nType: " + arr[1] +"\n Longitude: " + arr[2] + "\n Latitude: " + arr[3];
	}
	
	public void changeFinal() {
	    if( this.isStart && this.isEnd && this.isBus ) {
			this.finalize.setEnabled(true);
		}else
			this.finalize.setEnabled(false);
	}
	
    public int findStationNumber(String station) throws Exception
    //Method to return a number or position to get route started.
    { 								
		String[][] temp = LDSDB.allStations();
		String arrStat = "";
		for(int i = 0; i<temp.length; i++) {
			arrStat = temp[i][0];
			if(arrStat.equals(station))
				return i+1;
		}
		throw new Exception( "Station not found" );
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
			
			String note = 
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
		
		
															//create 2-dim array to hold data
		String[] busHeader =
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
			DefaultTableModel busModel = new DefaultTableModel(busData, busHeader); 	//add data and header to table model
			
			TableRowSorter<TableModel> busSorter = new TableRowSorter<>(busModel); //create method for sorting
			
			busTable = new JTable(busModel);				 				//Create JTable using model
			busTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); 		//Resize to fit columns
			
			busTable.setPreferredScrollableViewportSize(new Dimension(550, 150)); //set dimensions of scrollable
			busTable.getTableHeader().setPreferredSize( new Dimension(550, 60));
			
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
		
		static JTable table;
		
		setLocations( NationalMainFrame nmf ){

			String[][] data = nmf.LDSDB.busStations();
				 
			nmf.finalize.setEnabled(false); 							  //disable finalize button
			JPanel master = new JPanel(new FlowLayout(FlowLayout.LEFT)); //Create panel inside Frame 
			
			JPanel leftPanel = new JPanel(new BorderLayout()); 				//set left Panel in master
			JPanel startPanel = new JPanel(); 				   				//set start panel to hold JTable departure
			JPanel endPanel = new JPanel();					   				//set end panel to hold JTable Destination
			

			
			JLabel startHeader = new JLabel("Select Departure Location");							//create 2-dim array to hold data
			String[] header= {"Station","Type","Longitude","Latitude"}; 			//header for table
			
			
			DefaultTableModel model = new DefaultTableModel(data, header); 	//add data and header to table model
			
			TableRowSorter<TableModel> sorter = new TableRowSorter<>(model); //create method for sorting
			
			table = new JTable(model);				 				//Create JTable using model
			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); 		//Resize to fit columns
			table.setPreferredScrollableViewportSize(new Dimension(300,130)); //set dimensions of scrollable
			
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
	    
		static JTable table;
		
		deleteStations( NationalMainFrame nmf ){

			String[][] data = nmf.LDSDB.allStations();
				 
			nmf.finalize.setEnabled(false); 							  //disable finalize button
			JPanel master = new JPanel(new FlowLayout(FlowLayout.LEFT)); //Create panel inside Frame 
			
			JPanel leftPanel = new JPanel(new BorderLayout()); 				//set left Panel in master
			JPanel startPanel = new JPanel(); 				   				//set start panel to hold JTable departure
			JPanel endPanel = new JPanel();					   				//set end panel to hold JTable Destination
			

			
			JLabel startHeader = new JLabel("Select Departure Location");
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
	
	static class viewCon extends JPanel{
		JTextArea holdCons = new JTextArea();
		public viewCon(NationalMainFrame frame){
				
			holdCons.setText(frame.LDSDB.connectionsString());
			holdCons.setEditable(false);
			holdCons.setBorder(BorderFactory.createLineBorder(Color.black));
			add(holdCons);
			
		}
	}
	
}
