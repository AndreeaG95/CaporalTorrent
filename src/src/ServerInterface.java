package src;

import java.rmi.Remote;

public interface ServerInterface extends Remote{
	
	/* Method for sending file to the connected client */
	public boolean sendFile(ClientInterface c) throws java.rmi.RemoteException;
	
	float get_price (String Company) throws java.rmi.RemoteException;

}
