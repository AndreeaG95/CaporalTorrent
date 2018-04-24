package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
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
	private CentralServerInterface centralServer;
	private ClientId cId;
	private List<String> filesAvailableForDownload;

	// commands
	private static final String EXIT = "exit";
	private static final String DOWNLOAD = "download";
	private static final String LIST_FILES = "listFiles";

	public Client(String cName) {
		System.out.println("Initializing client : <<" + cName + " >>");

		cId = new ClientId(null, cName, "test", "test");
		
		setClientLocation();

		try {
			centralServer = (CentralServerInterface) Naming
					.lookup("rmi://" + Constants.CS_IP + "/" + Constants.CS_NAME);

		} catch (MalformedURLException | RemoteException | NotBoundException e) {
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
			cId.setLocation(future.get());
			
		} catch (InterruptedException e) {
			System.err.println("Couldn't get the location for client " + cId.getClientName());
			e.printStackTrace();
		} catch (ExecutionException e) {
			System.err.println("Couldn't get the location for client " + cId.getClientName());
			e.printStackTrace();
		}

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

			if (cId.getLocation() == null) {
				System.out.println("Client location unavailibe. Please try again later.");
				System.exit(0);
			}

			LocalServerInterface localServer = centralServer.getLocalServer(cId.getLocation());
			
			if (localServer == null) {
				System.out.println("Server unavailibe. Please try again later.");
				System.exit(0);
			}
			System.out.println("\nDownloading from: " + localServer.getLocalServerName());

			byte[] mydata = localServer.downloadFile(serverpath);
			System.out.println("downloading...");

			File clientpathfile = new File(clientpath);
			FileOutputStream out = new FileOutputStream(clientpathfile);

			out.write(mydata);
			out.flush();
			out.close();
			System.out.println("Finished downloading !");
		} catch (FileNotFoundException e) {
			System.err.println("Cannot create file on client side");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void printFiles() {
		try {

			LocalServerInterface localServer = centralServer.getLocalServer(cId.getLocation());

			String[] filelist;
			filelist = localServer.listFiles();
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
	
	@Override
	public void locationDetected() {
		System.out.println("Location was detected. Client ready!");
	}

	public Location getLocation() {
		return cId.getLocation();
	}

	public void printUsage() {
		System.out.println("\nAvailable commands:");
		System.out.println("\t listFiles - prints the available files for downloading");
		System.out.println("\t download [file_name] - downloads the requested file");
		System.out.println("\t exit - closes the session\n");

	}
	

	public void run() throws RemoteException {
		printUsage();

		LocalServerInterface localServer;

		Scanner reader = new Scanner(System.in);

		String[] userInput = reader.nextLine().split(" ");
		String command = userInput[0];
		localServer = centralServer.getLocalServer(cId.getLocation());
		
		while (!command.equals(EXIT)) {
			
			
			switch (command) {
			case LIST_FILES:
					printFiles();
				break;

			case DOWNLOAD:
					filesAvailableForDownload = Arrays.asList(localServer.listFiles());
					
				//new FileInputStream("test").rad
					//file to be downloaded
					String fileToDownload = userInput[1];
					
					if (!filesAvailableForDownload.contains(fileToDownload));{
						System.out.println("File << " + fileToDownload + ">> is not on the server!");
					}
					downloadFile("Storage_Folder", "downloadedFile");
					
				break;

			default:
				printUsage();
				System.err.println("Unrecognized command! Please retry!");
			}

			userInput = reader.nextLine().split(" ");
			command = userInput[0];
		}
		
		//process exit command
		shutdown();	

		reader.close();
	}

}