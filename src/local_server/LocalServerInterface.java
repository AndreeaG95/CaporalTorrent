package local_server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import common.Location;

public interface LocalServerInterface extends Remote{
	
	/* Method for sending a file from a local server to the connected client */
	public byte[] downloadFile(String file) throws RemoteException;
	 
	public String[] listFiles() throws RemoteException;
	
	String getLocalServerName() throws RemoteException;
	
	Location getLS_Location() throws RemoteException;
	
}
