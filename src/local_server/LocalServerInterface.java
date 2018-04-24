package local_server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;

import common.Location;

public interface LocalServerInterface extends Remote{
	
	public OutputStream getOutputStream(File f) throws IOException;
	
	public InputStream getInputStream(File f) throws IOException;
	 
	public String[] listFiles() throws RemoteException;
	
	public String getLocalServerName() throws RemoteException;
	
	public Location getLS_Location() throws RemoteException;
	
	public String getStoragePath() throws RemoteException;
	
}
