package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import controller.Controller;
import ds.DataTriplet;
import ds.Pair;
import model.Data;

public class MainWindow extends JFrame{

	private static final long serialVersionUID = 1L;
	private Pie pie;
	private Graph XYGraph = null;
	private JPanel control = new JPanel();
	private ArrayList<JTextField> filtersBox = new ArrayList<>();
	private Controller controller = new Controller(this);
	
	public ArrayList<JTextField> getFiltersBox(){
		return filtersBox;
	}
	
	private class readModeDialog extends JDialog{
		
		private static final long serialVersionUID = 1L;
		JButton goButton = new JButton("READ DATA");
		JTextField tf = new JTextField();
		
		public readModeDialog(JFrame owner) {
			super(owner);
			setTitle("Select read mode.");
			setBounds(400, 250, 400, 200);
			setResizable(false);
			setModalityType(ModalityType.APPLICATION_MODAL);
			JPanel content = new JPanel();
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(0,1,0,0));
			JRadioButton r1 = new JRadioButton("Read all data");   
			JRadioButton r2 = new JRadioButton("Read data for specified year");   
			ButtonGroup bg1 = new ButtonGroup();
			r1.setSelected(true);
			bg1.add(r1); bg1.add(r2);
			panel.add(new JLabel("Enter mode in which data should be read: "));
			panel.add(r1);
			panel.add(r2);
			panel.add(tf);
			panel.add(goButton);
			
			tf.setVisible(false);
			
			r2.addItemListener((ie)->{
				if(r2.isSelected()) tf.setVisible(true);
				else tf.setVisible(false);
			});
			
			goButton.addActionListener((ae)->{
				if(r1.isSelected()) MainWindow.this.controller.read(""); //sekundarni this
				else MainWindow.this.controller.read(tf.getText());
				readModeDialog.this.dispose();
			});
			
			content.add(panel);
			add(content, BorderLayout.CENTER);
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);//proveri sta je ovo
			setVisible(true);
		}
		
		
	}

	private void populateWindow() {
		
		control.setLayout(new GridLayout(0, 1, 0, 0));
		JRadioButton r1 = new JRadioButton("PieChart");   
		JRadioButton r2 = new JRadioButton("XYGraph"); 
		ButtonGroup bg1 = new ButtonGroup();  
		bg1.add(r1); bg1.add(r2);
		control.add(new JLabel("GRAPH SELECTION"));
		control.add(r1);
		control.add(r2);
		r1.setSelected(true);
		  
		JRadioButton rs2 = new JRadioButton("Number of disciplines");
		JRadioButton rs3 = new JRadioButton("Average height");
		JRadioButton rs4 = new JRadioButton("Average weight");
		ButtonGroup bg2 = new ButtonGroup();
		bg2.add(rs2); bg2.add(rs3); bg2.add(rs4);
		control.add(new JLabel("OPTIONS"));
		control.add(rs2);
		control.add(rs3);
		control.add(rs4);
		rs2.setSelected(true);
		rs2.setEnabled(false);
		rs3.setEnabled(false);
		rs4.setEnabled(false);
		
		JTextField rangeFrom = new JTextField();
		JTextField rangeTo = new JTextField();
		control.add(new JLabel("RANGE:"));
		control.add(new JLabel("From year:"));
		control.add(rangeFrom);
		control.add(new JLabel("To year:"));
		control.add(rangeTo);
		rangeFrom.setEnabled(false);
		rangeTo.setEnabled(false);
		
		
		control.add(new JLabel("FILTERS"));
		for(int i = 0; i<4; i++) {//i<4
			
			if(i == 0) control.add(new JLabel("Sport"));
			else if(i==1) control.add(new JLabel("Year"));
			else if(i==2) control.add(new JLabel("Event type"));
			else control.add(new JLabel("Medal"));
			
			JTextField tf = new JTextField();
			tf.setVisible(true);
			filtersBox.add(tf);
			control.add(tf);
		}
		
		JButton goButton = new JButton("GO!");
		goButton.addActionListener((ae)->{
			if(r1.isSelected()) {
				ArrayList<Pair> params = controller.performPie(
						filtersBox.get(0).getText(),
						filtersBox.get(1).getText(),
						filtersBox.get(2).getText(),
						filtersBox.get(3).getText());
				if(params == null) { //crta se default krug
					params = new ArrayList<Pair>();
					params.add(new Pair("", 360));
				}
				remove(pie);
				if(XYGraph!=null) remove(XYGraph);
				pie = new Pie(params);
				add(pie, BorderLayout.WEST);
				pie.setPreferredSize(new Dimension(800, 600));
			    pack();
			    repaint();
			}
			else {
				
				int option = 0;
				if(rs2.isSelected()) option = 0;
				else if(rs3.isSelected()) option = 1;
				else option = 2;
				
				ArrayList<DataTriplet> dps = controller.performXY(rangeFrom.getText(), rangeTo.getText(), option);
				if(dps == null) return;
				remove(pie);
				if(XYGraph!=null) remove(XYGraph);
				
				XYGraph = new Graph(dps);
				XYGraph.setPreferredSize(new Dimension(800, 600));
		        add(XYGraph);
		        pack();
			}
		});
		
		r1.addItemListener((ie)->{
			boolean val;
			if(r1.isSelected()) val = true;
			else val = false;
			for(int i = 0; i<4; i++) {
				filtersBox.get(i).setEnabled(val);
			}
			rs2.setEnabled(!val);
			rs3.setEnabled(!val);
			rs4.setEnabled(!val);
			rangeFrom.setEnabled(!val);
			rangeTo.setEnabled(!val);
		});
		
		control.add(goButton);
		add(control, BorderLayout.EAST);
		ArrayList<Pair> initPair = new ArrayList<Pair>();
		initPair.add(new Pair("", 360));
		pie = new Pie(initPair);//samo neki pocetni krug
		add(pie, BorderLayout.WEST);
		pie.setPreferredSize(new Dimension(800, 600));
	    pack();
	}
	
	public MainWindow(){
		
		readModeDialog rm = new readModeDialog(this);
		setTitle("Olympic games");
		setBounds(100, 50, 900, 600);
		setResizable(false);
		populateWindow();

		repaint();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int resp = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?",
	            "Exit?", JOptionPane.YES_NO_OPTION);

	            if (resp == JOptionPane.YES_OPTION) {
	            	Data.getInstance().deleteData();//obrisi sve podatke
	                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	            } else {
	               setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	            }
			}
		});
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		System.loadLibrary("noviDLL");
		new MainWindow();
	}
}
