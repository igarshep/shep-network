package view;

import javax.swing.JPanel;

import model.Network;
import model.Packet;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JSeparator;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class StatsView extends JPanel implements NetworkPanel{
	
	private JTextField firstPacketLossField;
	private JTextField highestLossField;
	private JTextField highestAveragePingField;
	
	private boolean haveSeenLost;
	private double highestLoss;
	private double highestAvg;
	private JTextField lastPacketLossField;

	/**
	 * Create the application.
	 */
	public StatsView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		JLabel firstPacketLossLabel = new JLabel("First packet loss:");
		
		JLabel highestLossLabel = new JLabel("Highest loss:");
		
		JSeparator separator = new JSeparator();
		
		JLabel highestAvgPingLabel = new JLabel("Highest average ping:");
		
		firstPacketLossField = new JTextField();
		firstPacketLossField.setEditable(false);
		
		highestLossField = new JTextField();
		highestLossField.setEditable(false);
		
		highestAveragePingField = new JTextField();
		highestAveragePingField.setEditable(false);
		
		JLabel lastPacketLossLabel = new JLabel("Last packet loss:");
		
		lastPacketLossField = new JTextField();
		lastPacketLossField.setEditable(false);
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(highestLossLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(highestLossField, GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE))
				.addComponent(separator, GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(highestAvgPingLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(highestAveragePingField, GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(firstPacketLossLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(firstPacketLossField, GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(lastPacketLossLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lastPacketLossField, GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(firstPacketLossLabel)
						.addComponent(firstPacketLossField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lastPacketLossLabel)
						.addComponent(lastPacketLossField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(highestLossLabel)
						.addComponent(highestLossField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(highestAvgPingLabel)
						.addComponent(highestAveragePingField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(194, Short.MAX_VALUE))
		);
		setLayout(groupLayout);
	}

	@Override
	public void clear() {
		firstPacketLossField.setText("");
		lastPacketLossField.setText("");
		highestLossField.setText("");
		highestAveragePingField.setText("");
		haveSeenLost = false;
		highestLoss = 0;
		highestAvg = 0;
	}

	@Override
	public void updateOnPacket(final Packet packet) {
		if(packet.wasTimedOut()) {
			final String date = packet.getFinalDate();
			if(!haveSeenLost) {
				firstPacketLossField.setText(date);
				haveSeenLost = true;
			}
			lastPacketLossField.setText(date);
		}
	}

	@Override
	public void updateOnNetwork(Network network) {
		double loss = network.getLossPercent();
		double avgPing = network.getAverage();
		if(highestLoss < loss) {
			highestLoss = loss;
			highestLossField.setText(String.format("%.2f", highestLoss));
		}
		
		if(highestAvg < avgPing) {
			highestAvg = avgPing;
			highestAveragePingField.setText(String.format("%.2f", highestAvg));
		}
	}
}
