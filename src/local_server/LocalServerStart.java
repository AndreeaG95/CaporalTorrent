package local_server;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import central_server.CentralServerInterface;
import common.Constants;

public class LocalServerStart {
	public static void main(String[] args) {
		
		if(args.length != 1){
			System.out.println("Incorrect usage: ServerStart <file_directory>");
			System.exit(0);
		}
		
		try{
			LocalServer ls = new LocalServer("LocalServer1");
			String localServerName = ls.getLocalServerName();
			
			//register the LS to the central server
			CentralServerInterface csi = (CentralServerInterface)Naming.lookup("rmi://" + Constants.CS_IP + "/" + Constants.CS_NAME );
			csi.registerLocalServer(localServerName);
			
			Naming.rebind("rmi://localhost/" + localServerName, ls);
			
			System.out.println("Local Server <" + localServerName + "> has started!");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
