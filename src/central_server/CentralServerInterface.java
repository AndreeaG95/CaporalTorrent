package central_server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.ClientId;
import common.Location;
import local_server.LocalServerInterface;

public interface CentralServerInterface extends Remote {
	
	/**
	 * 
	 * @param lsName - name of the server that wants to provide services
	 * @throws RemoteException - each remote method must be capable of throwing RemoteException
	 */
	void registerLocalServer(String lsName) throws RemoteException;
	
	//String[] getLocalServersList() throws RemoteException;
	
	/**
	 * 
	 * @return -   the local server to which the client will connect
	 */
	LocalServerInterface getLocalServer(ClientId cId) throws RemoteException;
}
