package local_server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import common.Constants;
import common.FindLocationTask;
import common.Location;
import common.LocationDetectedListener;

public class LocalServer extends UnicastRemoteObject implements LocalServerInterface, LocationDetectedListener {
	private String lsName;
	private Location location;

	private static final long serialVersionUID = 1L;

	private String dir = "";

	public LocalServer(String lsName) throws RemoteException {
		System.out.println("Initializing <<" + lsName + ">> ...");
		this.lsName = lsName;
		setServerLocation();
	}

	/**
	 * Used for simulating the server being in a different location than the
	 * current one
	 * 
	 * @param lsName
	 * @param hardcodedLocation
	 * @throws RemoteException
	 */
	public LocalServer(String lsName, Location hardcodedLocation) throws RemoteException {
		System.out.println("Initializing <<" + lsName + ">> ...");
		this.lsName = lsName;
		this.location = hardcodedLocation;
		System.out.println(lsName + " is ready to use!");
	}

	private void setServerLocation() {
		ExecutorService es = Executors.newSingleThreadExecutor();
		FindLocationTask task = new FindLocationTask(this);

		// a Future object can be used to fetch the result of the task when it
		// is available.
		Future<Location> future = es.submit(task);

		try {
			location = future.get();
		} catch (InterruptedException e) {
			System.err.println("Couldn't get the location for server " + Constants.CS_NAME);
			e.printStackTrace();
		} catch (ExecutionException e) {
			System.err.println("Couldn't get the location for server " + Constants.CS_NAME);
			e.printStackTrace();
		}

		// TODO when location is set notify that the central server can be used
		es.shutdown();
	}

	public void setFileLocation(String d) {
		dir = d;
	}

	@Override
	public byte[] downloadFile(String file) throws RemoteException {
		byte[] mydata;

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

	@Override
	public void locationDetected() {
		System.out.println(lsName + " is ready to use!");
	}

}
