package client;

public class ClientStart {
	
	public static void main(String[] args) {
		Client c = new Client("client1");
		
		c.downloadFile("maria.txt", "ana.txt");
		
	}

}
