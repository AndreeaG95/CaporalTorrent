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

import common.FindLocationTask;
import common.Location;
import common.LocationDetectedListener;

public class LocalServer extends UnicastRemoteObject implements LocalServerInterface, LocationDetectedListener {
	private String lsName;
	private Location location;
	
	//the folder in which the files available for download are stored
	private String storage_folder="";

	private static final long serialVersionUID = 1L;


	public LocalServer(String lsName, String storage_folder) throws RemoteException {
		System.out.println("\nInitializing <<" + lsName + ">> ...");
		this.lsName = lsName;
		this.storage_folder = storage_folder;
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
	public LocalServer(String lsName, Location hardcodedLocation, String storage_folder) throws RemoteException {
		System.out.println("\nInitializing <<" + lsName + ">> ...");
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
			System.err.println("Couldn't get the location for server " + lsName);
			e.printStackTrace();
		} catch (ExecutionException e) {
			System.err.println("Couldn't get the location for server " + lsName);
			e.printStackTrace();
		}

		es.shutdown();
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

	public String[] listFiles() throws RemoteException {
		System.out.println("Listing files...");
		
		File serverpathdir = new File(storage_folder);
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
	
	/*
	public void sendFile(RemoteInputStream ristream) throws IOException {
	      InputStream istream = RemoteInputStreamClient.wrap(ristream);
	      FileOutputStream ostream = null;
	      try {

	        File tempFile = File.createTempFile("sentFile_", ".dat");
	        ostream = new FileOutputStream(tempFile);
	        System.out.println("Writing file " + tempFile);

	        byte[] buf = new byte[1024];

	        int bytesRead = 0;
	        while((bytesRead = istream.read(buf)) >= 0) {
	          ostream.write(buf, 0, bytesRead);
	        }
	        ostream.flush();

	        System.out.println("Finished writing file " + tempFile);
	        
	      } finally {
	        try {
	          if(istream != null) {
	            istream.close();
	          }
	        } finally {
	          if(ostream != null) {
	            ostream.close();
	          }
	        }
	      }
	    }
	    */

}
