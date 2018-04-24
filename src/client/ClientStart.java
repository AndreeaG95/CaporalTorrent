package client;

import java.io.File;
import java.io.IOException;

public class ClientStart {

	private static void printUsage(){
		System.out.println("Usage:\n "
				+ "    upload <filename> \n"
				+ "    download <filename> \n"
				+ "    list\n");
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		if (args.length == 0){
			printUsage();
			System.exit(0);
		}
		
		String command = args[0];
		Client c = new Client("client1");
		
		switch(command){
		case "upload":
			try {
				if (args.length != 2){
					printUsage();
					System.exit(0);
				}
				String file = args[1];
				
				String outfile = "out_"+file;
				System.out.println("Output file:" + outfile);
				c.uploadFile(new File(file), new File(outfile));
			} catch (IOException e) {
				System.err.println("Couldn't upload file");
				e.printStackTrace();
			}
			break;
		case "download":
			try {
				if (args.length != 2){
					printUsage();
					System.exit(0);
				}
				String file = args[1];
				
				String outfile = "out_"+file;
				System.out.println("Output file:" + outfile);
				c.downloadFile(new File(file), new File(outfile));
			} catch (IOException e) {
				System.err.println("Couldn't download file");
				e.printStackTrace();
			}
			break;
		case "list":
			c.listFiles("./");
			break;
		}
	}

}
