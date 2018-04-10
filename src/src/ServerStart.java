package src;

import java.rmi.Naming;

public class ServerStart {
	public static void main(String[] args) {
		try{
			
			//java.rmi.registry.LocateRegistry.createRegistry(1099);
			
			ServerClass fs=new ServerClass();
			
			//fs.setFile("ana.txt");			
			Naming.rebind("rmi://localhost/NASDAQ", fs);
			System.out.println("File Server is Ready");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
