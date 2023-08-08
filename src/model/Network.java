package model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Network {
	
	private final InetAddress address;
	private int max;
	private List<Packet> packets;
	
	private boolean overflowed;
	
	private double averagePing;
	private double lost;
	
	private Packet firstLost;
	
	public Network(final String addressField, int max) throws UnknownHostException {
		address = InetAddress.getByName(addressField);
		this.max = max;
		packets = Arrays.asList(new Packet[max]);
	}
	
	public Packet ping(int index) {
		final Packet packet = new Packet();
		try {
			if(address.isReachable(1000)) {
				packet.calcualteDifference();
			} else {
				packet.timedOut();
				if(firstLost == null) {
					firstLost = packet;
				}
			}
		} catch(final IOException e) {
			e.printStackTrace();
		} finally {
			packets.set(index % max, packet);
		}
		return packet;
	}
	
	public int getSize() {
		if(overflowed) {
			return packets.size();
		}
		int count = 0;
		for(final Packet packet : packets) {
			if(packet != null) {
				count++;
			} else {
				return count;
			}
		}
		overflowed = true;
		return max;
	}
	
	private final List<Packet> getPackets() {
		if(overflowed) {
			return packets;
		} else {
			final List<Packet> temp = new ArrayList<Packet>();
			for(final Packet packet : packets) {
				if(packet != null) {
					temp.add(packet);
				} else {
					break;
				}
			}
			return temp;
		}
	}
	
	public final void recalculate() {
		calculateAverage();
		calculateLoss();
	}
	
	private final void calculateAverage() {
		int count = getSize();
		long sum = 0;
		for(final Packet packet : getPackets()) {
			sum += packet.getDifference();
		}
		averagePing = (double)sum/(double)count;
	}
	
	private final void calculateLoss() {
		int count = getSize();
		int loss = 0;
		for(final Packet packet : getPackets()) {
			if(packet.wasTimedOut()) {
				loss++;
			}
		}
		lost = (double)loss/(double)count;
	}
	
	public double getAverage() {
		return averagePing;
	}
	
	public double getLossPercent() {
		return lost*100;
	}
	
	public Packet getFirstLost() {
		return firstLost;
	}
}
