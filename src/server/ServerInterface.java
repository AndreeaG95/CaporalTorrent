package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote{
	
	/* Method for sending a file from a local server to the connected client */
	public byte[] downloadFile(String file) throws java.rmi.RemoteException;
	
	public String[] listFiles(String serverpath) throws RemoteException;
	
	float get_price (String Company) throws java.rmi.RemoteException;

}
