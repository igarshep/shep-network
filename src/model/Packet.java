package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Packet {
	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
	
	private boolean timedOut;
	private final long startTime;
	private long difference;
	
	private String finalDate = "";
	
	public Packet() {
		startTime = System.nanoTime();
	}
	
	public void timedOut() {
		timedOut = true;
		finalDate = dtf.format(LocalDateTime.now());
	}
	
	public void calcualteDifference() {
		difference = (System.nanoTime() - startTime)/1000000;
		finalDate = dtf.format(LocalDateTime.now());
	}
	
	public long getDifference() {
		return difference;
	}
	
	public boolean wasTimedOut() {
		return timedOut;
	}
	
	public String getFinalDate() {
		return finalDate;
	}
	
	@Override
	public String toString() {
		return timedOut ? "Request timeout" : "Round trip time: " + difference + " ms @ " + finalDate;
	}
}
