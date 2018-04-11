package central_server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import local_server.LocalServerInterface;

public class CentralServer extends UnicastRemoteObject implements CentralServerInterface {
	/**
	 * needed for serialization
	 */
	private static final long serialVersionUID = 2L;
	
	public ArrayList<LocalServerInterface> servers;
	
	public CentralServer() throws RemoteException {
		super();
		servers = new ArrayList<>();
	}

	@Override
	public void registerLocalServer(String lsName) {
		try {
			LocalServerInterface newLocalServer = (LocalServerInterface)Naming.lookup("rmi://localhost/" + lsName);
			servers.add(newLocalServer);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

}
