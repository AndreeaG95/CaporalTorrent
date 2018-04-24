package client;

import java.io.IOException;
import java.rmi.RemoteException;

public class ClientStart {

	public static void main(String[] args) throws InterruptedException {
		Client c = new Client("client1");

		try {
			c.run();
		} catch (RemoteException e) {
			System.err.println("Failed to start the client");
			e.printStackTrace();
		} catch (IOException e) {
			
			System.err.println("Could not create local file");
			e.printStackTrace();
		}
	}
}
