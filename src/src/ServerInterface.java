package src;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote{
	
	/* Method for sending a file from a local server to the connected client */
	public byte[] downloadFile(String file) throws java.rmi.RemoteException;
	
	public String[] listFiles(String serverpath) throws RemoteException;
	
	float get_price (String Company) throws java.rmi.RemoteException;
	
    /** Returns a stub for the storage server hosting a file.
    <p>
    If the client intends to perform calls only to <code>read</code> or
    <code>size</code> after obtaining the storage server stub, it should
    lock the file for shared access before making this call. If it intends
    to perform calls to <code>write</code>, it should lock the file for
    exclusive access.
    @param file Path to the file.
    @return A stub for communicating with the storage server.
    @throws FileNotFoundException If the file does not exist.
    @throws RMIException If the call cannot be completed due to a network
                         error.
 */
	public Storage getStorage(Path file) throws java.rmi.RemoteException, FileNotFoundException;

}
