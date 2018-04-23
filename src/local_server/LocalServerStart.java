package local_server;

import java.rmi.Naming;

import central_server.CentralServerInterface;
import common.Constants;
import common.Location;

public class LocalServerStart {
	public static void main(String[] args) {
		
		if(args.length != 1){
			System.out.println("Incorrect usage: ServerStart <file_directory>");
			System.exit(0);
		}
		
		try{
			LocalServer ls = new LocalServer("LocalServer1", new Location(53.2734, -7.778320310000026));
			LocalServer ls2 = new LocalServer("LocalServer2");
			String localServerName = ls.getLocalServerName();
			String localServerName2 = ls.getLocalServerName();
			
			Naming.rebind("rmi://localhost/" + localServerName, ls);
			Naming.rebind("rmi://localhost/" + localServerName2, ls2);
			
			//register the LS to the central server
			CentralServerInterface csi = (CentralServerInterface)Naming.lookup("rmi://" + Constants.CS_IP + "/" + Constants.CS_NAME );
			csi.registerLocalServer(localServerName);
			csi.registerLocalServer(localServerName2);
			
			System.out.println("Local Server <" + localServerName + "> has started!");
			System.out.println("Local Server <" + localServerName2 + "> has started!");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
