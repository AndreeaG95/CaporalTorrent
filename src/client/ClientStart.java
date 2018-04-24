package client;

import java.io.File;
import java.io.IOException;

public class ClientStart {
	
	public static void main(String[] args) throws InterruptedException {
		Client c = new Client("client1");
		
		//c.downloadFile("ana.txt", "maria.txt");
		try {
			//c.uploadFile(new File("ana.txt"), new File("upload.txt"));
			c.downloadFile(new File("maria.txt"), new File("download.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
