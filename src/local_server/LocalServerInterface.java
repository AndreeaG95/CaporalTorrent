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
	 
	public String[] listFiles(String serverpath) throws RemoteException;
	
	String getLocalServerName() throws RemoteException;
	
	Location getLS_Location() throws RemoteException;
	
}
