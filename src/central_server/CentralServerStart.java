package central_server;

import java.rmi.Naming;

import common.Constants;

public class CentralServerStart {

	public static void main(String[] args){
		try {
			CentralServer centralServer = new CentralServer();
			
			Naming.rebind("rmi://" + Constants.CS_IP + "/" + Constants.CS_NAME , centralServer);
			//Naming.rebind("rmi://localhost/NASDAQ", centralServer);
			System.out.println("Central server has started!");
			
		} catch (Exception e) {
			System.err.println("Central server could not start!");
			e.printStackTrace();
		}
	}
}
