package local_server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import common.Location;

public class LocalServer extends UnicastRemoteObject implements LocalServerInterface{
	private String lsName;
	private Location location;
	
	/**
	 * do we actually need this parameter?
	 */
	private static final long serialVersionUID = 1L;
	
	private String dir="";
	
	public LocalServer(String lsName) throws RemoteException {
		this.lsName = lsName;
		location = detectServerLocation();
	}
	
	/**
	 * Used for simulating the server being in a different location than the current one
	 * @param lsName
	 * @param hardcodedLocation
	 * @throws RemoteException
	 */
	public LocalServer(String lsName, Location hardcodedLocation) throws RemoteException {
		this.lsName = lsName;
		this.location = hardcodedLocation;
	}

	private Location detectServerLocation() {
		Location lsLoc = null;
		
		return lsLoc;
	}

	public void setFileLocation(String d){
		dir = d;
	}
	

	@Override
	public byte[] downloadFile(String file) throws RemoteException {
		byte [] mydata;	
		
		File serverpathfile = new File(file);			
		mydata = new byte[(int) serverpathfile.length()];
		FileInputStream in;
		try {
			in = new FileInputStream(serverpathfile);
			try {
				in.read(mydata, 0, mydata.length);
			} catch (IOException e) {
		
				e.printStackTrace();
			}						
			try {
				in.close();
			} catch (IOException e) {
			
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}		
		
		return mydata;
	}
	

	public String[] listFiles(String serverpath) throws RemoteException {
		File serverpathdir = new File(serverpath);
		return serverpathdir.list();
		
	}

	@Override
	public String getLocalServerName() {
		return lsName;
	}

	@Override
	public Location getLS_Location() throws RemoteException {
		return location;
	}

}
