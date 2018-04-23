package client;

import java.util.concurrent.TimeUnit;

public class ClientStart {
	
	public static void main(String[] args) throws InterruptedException {
		Client c = new Client("client1");
		
		c.downloadFile("ana.txt", "maria.txt");
		
	}

}
