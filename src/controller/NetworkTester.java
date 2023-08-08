package controller;

import model.Network;
import view.MainNotebook;

public class NetworkTester {
	
	private boolean semaphore = true;
	private int index;
	
	public void test(final Network network, final MainNotebook view) {
		semaphore = true;
		index = 0;
		new Thread(new Runnable() {
			public void run() {
				while(semaphore) {
					view.updateOnPacket(network.ping(index++));
					if(index % 10 == 0) {
						view.updateOnNetwork(network);
					}
				}
			}
		}).start();
	}
	
	public void stopTest() {
		semaphore = false;
	}
}
