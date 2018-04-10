package src;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote{

	public boolean sendData(String name, byte[] mydata, int mylen) throws RemoteException;
	
	public String getName() throws RemoteException;

}
