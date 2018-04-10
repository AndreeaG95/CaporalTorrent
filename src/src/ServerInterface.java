package src;

import java.rmi.Remote;

public interface ServerInterface extends Remote{
	
	/* Method for sending file to the connected client */
	public boolean sendFile(ClientInterface c);

}
