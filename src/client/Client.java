package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
	private ClientId cId;
	private CentralServerInterface centralServer;

	public Client(String cName) {
		System.out.println("Initializing client : <<" + cName + " >>");

		try {
			centralServer = (CentralServerInterface) Naming.lookup("rmi://localhost/" + Constants.CS_NAME);
					.lookup("rmi://localhost/" + Constants.CS_NAME);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void configureClient(String cName) {
		ExecutorService es = Executors.newSingleThreadExecutor();
		FindLocationTask task = new FindLocationTask(this);

		// a Future object can be used to fetch the result of the task when it
		// is available.
		Future<Location> future = es.submit(task);

		try {
			Location location = future.get();
			this.cId = new ClientId(location, cName, "test", "test");
		} catch (InterruptedException e) {
			System.err
					.println("Couldn't get the location for client " + "test");
			e.printStackTrace();
		} catch (ExecutionException e) {
			System.err
					.println("Couldn't get the location for client " + "test");
			e.printStackTrace();
		}

		// TODO when location is set notify that the central server can be used
		es.shutdown();
	}

	// TODO: Error checks.
	// We ask each time for a new local server in case the one we used before
	// crashed or is busy.

	// FIXME If we ask each time the CS to give us a LS then each time we should
	// recompute Client location(which takes time)
	// maybe it would be better to ask only after a timer expires or after a
	// number of downloads performed
	public void downloadFile(String serverpath, String clientpath) {
		try {
			LocalServerInterface LSserver = (LocalServerInterface) CSserver.getLocalServer(location);
					.getLocalServer(cId);

			if ((LSserver == null) || (location == null)) {
				System.out
						.println("Server unavailibe. Please try again later.");
				System.exit(0);
			}
			System.out.println("\nDownloading from: " + LSserver.getLocalServerName());

			byte[] mydata = LSserver.downloadFile(serverpath);
			System.out.println("downloading...");

			File clientpathfile = new File(clientpath);
			FileOutputStream out = new FileOutputStream(clientpathfile);


			out.write(mydata);
			out.flush();
			out.close();
			System.out.println("Finished downloading !");
		}
		catch(FileNotFoundException e){
			System.err.println("Cannot create file on client side");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void listFiles(String serverpath) {
		try {
			LocalServerInterface LSserver = (LocalServerInterface) centralServer.getLocalServer(location);
					.getLocalServer(cId);

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

	public void shutdown() {
		System.exit(0);
		System.out.println("Client has shutdown.");
	}

	/*
	 * public static void main(String[] args) { String download = "download";
	 * String dir = "list"; String shutdown= "shutdown";
	 * 
	 * String clientpath; String serverpath;
	 * 
	 * 
	 * try{
	 * 
	 * 
	 * 
	 * //to download a file if(download.equals(args[0])) { serverpath = args[1];
	 * clientpath= args[2];
	 * 
	 * byte [] mydata = server.downloadFile(serverpath);
	 * System.out.println("downloading...");
	 * 
	 * File clientpathfile = new File(clientpath); FileOutputStream out = new
	 * FileOutputStream(clientpathfile);
	 * 
	 * System.out.println("Finished downloading !");
	 * 
	 * out.write(mydata); out.flush(); out.close(); }
	 * 
	 * //to list all the files in a directory if(dir.equals(args[0])) {
	 * serverpath = args[1]; String[] filelist = server.listFiles(serverpath);
	 * for (String i: filelist) { System.out.println(i); } }
	 * 
	 * //to shutdown the client if(shutdown.equals(args[0])) { System.exit(0);
	 * System.out.println("Client has shutdown. Close the console"); }
	 * }catch(Exception e) { e.printStackTrace(); System.out.println(
	 * "error with connection or command. Check your hostname or command"); } }
	 */

	@Override
	public void locationDetected() {
		System.out.println("Location was detected. Client ready!");
	}

	public Location getLocation() {
		return cId.getLocation();
	}

}
