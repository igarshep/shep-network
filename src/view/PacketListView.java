package view;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;

import model.Network;
import model.Packet;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;

@SuppressWarnings("serial")
public class PacketListView extends JPanel implements NetworkPanel {
	
	private JTextArea consoleArea;
	private JLabel avgLabel, packetLossLabel;
	
	/**
	 * Create the application.
	 */
	public PacketListView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private final void initialize() {
		final JPanel panel = new JPanel();
		
		avgLabel = new JLabel();
		panel.add(avgLabel);
		
		packetLossLabel = new JLabel();
		panel.add(packetLossLabel);
		
		final JScrollPane scrollPane = new JScrollPane();
		final GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE))
		);
		
		consoleArea = new JTextArea();
		consoleArea.setEditable(false);
		scrollPane.setViewportView(consoleArea);
		setLayout(groupLayout);
	}
	
	private void drawLabel(final Network network) {
		final int size = network.getSize();
		avgLabel.setText("Avg of last " + size + ": " + String.format("%.2f", network.getAverage()) + "ms");
		
		final double lossPercent = network.getLossPercent();
		String color = "green";
		if(lossPercent != 0) {
			final int red = (int)(1.55*lossPercent) + 100;
			color = "rgb(" + red + ", 0, 0)";
		}
		packetLossLabel.setText("<html>Packet loss of last " + size + ": <a style=\"color:" + color + "\">" + String.format("%.1f", lossPercent) + "%</html>");
	}

	public void setFinalText(final String text) {
		consoleArea.setText(text);
	}
	
	@Override
	public void clear() {
		consoleArea.setText("");
	}

	@Override
	public void updateOnPacket(final Packet packet) {
		consoleArea.append(packet + "\n");
		consoleArea.setCaretPosition(consoleArea.getText().length());
	}

	@Override
	public void updateOnNetwork(final Network network) {
		drawLabel(network);
	}
}
