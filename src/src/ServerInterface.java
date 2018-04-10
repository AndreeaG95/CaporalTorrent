package src;

import java.rmi.Remote;
import java.util.ArrayList;

public interface ServerInterface extends Remote{
	
	/* Method for sending a file from a local server to the connected client */
	public boolean sendFile(ClientInterface c, String file) throws java.rmi.RemoteException;
	
	public ArrayList<String> getAvailibleFiles() throws java.rmi.RemoteException;
	
	float get_price (String Company) throws java.rmi.RemoteException;

}
