package local_server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LocalServerInterface extends Remote{
	
	/* Method for sending a file from a local server to the connected client */
	public byte[] downloadFile(String file) throws RemoteException;
	
	public String[] listFiles(String serverpath) throws RemoteException;
	
	String getLocalServerName() throws RemoteException;
}
