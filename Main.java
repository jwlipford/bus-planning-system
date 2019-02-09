import java.util.*;
import java.util.regex.Pattern;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class Main extends JFrame{
	public Main(){
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
		
		String[][] data = {
				{"Station 1","st1","2","3"},
				{"Station 2","st2", "4","5"},
				{"Station 3","st3","6","7"},
				{"Station 5","st4", "8","9"},
				{"Station 6","st1","2","3"},
				{"Station 7","st2", "4","5"},
				{"Station 8","st3","6","7"},
				{"Station 9","st4", "8","9"},
				{"Station 1","st1","2","3"},
				{"Station 2","st2", "4","5"},
				{"Station 3","st3","6","7"},
				{"Station 5","st4", "8","9"},
				{"Station 6","st1","2","3"},
				{"Station 7","st2", "4","5"},
				{"Station 8","st3","6","7"}
			};
		
		
		String[] header= {"Station","Acronym","Longitude","Latitude"};
		
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
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
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
		
		table2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
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
			String[] arr = new String[4];
			for(int i = 0; i < 5; i++) {
				
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
			String[] arr = new String[4];
			for(int i = 0; i < 5; i++) {
				
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
		
		
		
		add(master,BorderLayout.WEST);	
		
		
	
		
	}
	
	public String convertToString(String[] arr) {
		String concat = "";
		
		return concat = "Bus Station: " + arr[0] + "\n Acronym: " + arr[1] + "\n Longitude: " + arr[2] + "\n Latitude: " + arr[3];
	
		
	
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
