package src;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote{

	void sendData(String name, byte[] mydata, int mylen);
	
	public String getName() throws RemoteException;

}
