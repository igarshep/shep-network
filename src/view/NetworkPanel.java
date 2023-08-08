package view;

import model.Network;
import model.Packet;

public interface NetworkPanel {
	public void clear();
	public void updateOnPacket(Packet packet);
	public void updateOnNetwork(Network network);
}
