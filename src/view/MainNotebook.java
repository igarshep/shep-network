package view;

import javax.swing.JFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import controller.NetworkTester;
import model.Network;
import model.Packet;

import javax.swing.JTabbedPane;
import javax.swing.JButton;

public class MainNotebook implements NetworkPanel {

	private JFrame frame;
	private JTextField addressField;
	private JTabbedPane tabbedPane;
	private final List<NetworkPanel> panels;
	
	private final int max;
	private final NetworkTester tester;
	private Network network;

	/**
	 * Create the application.
	 */
	public MainNotebook(int max) {
		this.max = max;
		panels = new ArrayList<NetworkPanel>();
		tester = new NetworkTester();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 570);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Network Tester 3");
		
		PacketListView listTab = new PacketListView();
		panels.add(listTab);
		StatsView statsTab = new StatsView();
		panels.add(statsTab);
		
		final JLabel addressLabel = new JLabel("Address:");
		
		final JButton startButton = new JButton("Start");
		final JButton stopButton = new JButton("Stop");
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				startButton.setEnabled(true);
				addressField.setEditable(true);
				stopButton.setEnabled(false);
				tester.stopTest();
			}
		});
		
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				clear();
				startButton.setEnabled(false);
				addressField.setEditable(false);
				stopButton.setEnabled(true);
				try {
					network = new Network(addressField.getText(), max);
					tester.test(network, MainNotebook.this);
				} catch(final UnknownHostException uhe) {
					listTab.setFinalText("Can't resolve name");
					startButton.setEnabled(true);
					addressField.setEditable(true);
					stopButton.setEnabled(false);
				}
			}
		});
		
		addressField = new JTextField();
		addressField.setText("www.google.com");
		addressField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				changed();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				changed();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				changed();
			}
			
			private void changed() {
				startButton.setEnabled(!addressField.getText().isBlank() && !stopButton.isEnabled());
			}
		});
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		final JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		final GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(addressLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(addressField, GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(startButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(stopButton)
					.addPreferredGap(ComponentPlacement.RELATED, 141, Short.MAX_VALUE)
					.addComponent(closeButton)
					.addContainerGap())
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(addressLabel)
						.addComponent(addressField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(startButton)
						.addComponent(stopButton)
						.addComponent(closeButton))
					.addContainerGap())
		);
		
		tabbedPane.addTab("List", null, listTab, null);
		tabbedPane.addTab("Stats", null, statsTab, null);
		frame.getContentPane().setLayout(groupLayout);
		
		frame.setVisible(true);
	}
	
	@Override
	public final void clear() {
		for(final NetworkPanel panel : panels) {
			panel.clear();
		}
	}
	
	public final void updateOnPacket(Packet packet) {
		for(final NetworkPanel panel : panels) {
			panel.updateOnPacket(packet);
		}
	}
	
	public final void updateOnNetwork(Network network) {
		network.recalculate();
		for(final NetworkPanel panel : panels) {
			panel.updateOnNetwork(network);
		}
	}
}
