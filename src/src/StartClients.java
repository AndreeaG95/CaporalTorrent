package src;

import java.rmi.Naming;
import java.util.Scanner;

public class StartClients {
	
	public static void main(String[] args) {
		try{
			
			FileClient c = new FileClient("imed");			
			ServerInterface server = (ServerInterface)Naming.lookup("rmi://localhost/CentralServer");
		
			System.out.println("Listening.....");			
			
			Scanner s=new Scanner(System.in);
			
			server.sendFile(c, "ana.txt");
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
}
