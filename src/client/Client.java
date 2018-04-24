package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import central_server.CentralServerInterface;
import common.Constants;
import common.FindLocationTask;
import common.Location;
import common.LocationDetectedListener;
import local_server.LocalServerInterface;

public class Client implements LocationDetectedListener {
	private String cName;
	private CentralServerInterface centralServer;
	private Location location;

	public Client(String cName) {
		System.out.println("Initializing client : <<" + cName + " >>");
		this.cName = cName;
		setClientLocation();

		try {
			centralServer = (CentralServerInterface) Naming
					.lookup("rmi://localhost/" + Constants.CS_NAME);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setClientLocation() {
		ExecutorService es = Executors.newSingleThreadExecutor();
		FindLocationTask task = new FindLocationTask(this);

		// a Future object can be used to fetch the result of the task when it
		// is available.
		Future<Location> future = es.submit(task);

		try {
			location = future.get();
		} catch (InterruptedException e) {
			System.err.println("Couldn't get the location for client " + cName);
			e.printStackTrace();
		} catch (ExecutionException e) {
			System.err.println("Couldn't get the location for client " + cName);
			e.printStackTrace();
		}

		// TODO when location is set notify that the central server can be used
		es.shutdown();
	}

	// TODO: Error checks.
	// We ask each time for a new local server in case the one we used before
	// crashed or is busy.

	
	final public static int BUF_SIZE = 1024 * 64;
	
    public static void copy(InputStream in, OutputStream out) throws IOException {
        
    	System.out.println("using byte[] read/write");
        byte[] b = new byte[BUF_SIZE];
        int len;
        while ((len = in.read(b)) >= 0) {
            out.write(b, 0, len);
        }
        in.close();
        out.close();
    }
    
    public void downloadFile(File src, File dest) throws IOException {
    	if (location == null) {
			System.out
					.println("Client location unavailibe. Please try again later.");
			System.exit(0);
		}
		
		LocalServerInterface LSserver = (LocalServerInterface) centralServer
				.getLocalServer(location);
		if (LSserver == null) {
			System.out
					.println("Server unavailibe. Please try again later.");
			System.exit(0);
		}
		
		System.out.println("\nDownloading from: "
				+ LSserver.getLocalServerName());
        copy (LSserver.getInputStream(src), new FileOutputStream(dest));
    }

	public void uploadFile(File src, File dest)throws IOException {
		if (location == null) {
			System.out
					.println("Client location unavailibe. Please try again later.");
			System.exit(0);
		}
		
		LocalServerInterface LSserver = (LocalServerInterface) centralServer
				.getLocalServer(location);
		if (LSserver == null) {
			System.out
					.println("Server unavailibe. Please try again later.");
			System.exit(0);
		}
		
		System.out.println("\nUploading to: "
				+ LSserver.getLocalServerName());
		
		 if(src.exists()){
			 File file2 = new File(dest+"_copy");
			 dest.renameTo(file2);
		     
			 System.out.println("file is already there");
		  }else{
		       System.out.println("Not find file ");
		  }
		copy(new FileInputStream(src), LSserver.getOutputStream(dest));
		
		centralServer.updateFiles(LSserver, dest);
	}


	public void listFiles(String serverpath) {
		try {
			LocalServerInterface LSserver = (LocalServerInterface) centralServer
					.getLocalServer(location);

			String[] filelist;
			filelist = LSserver.listFiles(serverpath);
			for (String i : filelist) {
				System.out.println(i);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public boolean update(){
		try {
			centralServer.update();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public void shutdown() {
		System.exit(0);
		System.out.println("Client has shutdown.");
	}


	@Override
	public void locationDetected() {
		System.out.println("Location was detected. Client ready!");
	}

	public Location getLocation() {
		return location;
	}

}
