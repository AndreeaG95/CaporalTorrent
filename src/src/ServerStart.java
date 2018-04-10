package src;

import java.rmi.Naming;

public class ServerStart {
	public static void main(String[] args) {
		
		if(args.length != 1){
			System.out.println("Incorrect usage: ServerStart <file_directory>");
			System.exit(0);
		}
		
		try{
			CentralServer cs = new CentralServer();
			cs.setFileLocation(args[0]);
			
			Naming.rebind("rmi://localhost/CentralServer", cs);
			
			System.out.println("File Server is Ready");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
