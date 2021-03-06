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
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import local_server.LocalServerInterface;
import central_server.CentralServerInterface;

import common.Constants;
import common.FindLocationTask;
import common.Location;
import common.LocationDetectedListener;

public class Client implements LocationDetectedListener {
	private CentralServerInterface centralServer;
	private ClientId cId;
	private List<String> filesAvailableForDownload;
	private final static int BUF_SIZE = 1024 * 64;
	// when more than UPTIME seconds have passed, client recomputes location and
	// asks for (new) LS
	private long UPTIME = 10;

	// commands
	private static final String EXIT = "exit";
	private static final String DOWNLOAD = "download";
	private static final String LIST_FILES = "listFiles";
	private static final String UPLOAD = "upload";

	public Client(String cName) {
		System.out.println("Initializing client : <<" + cName + " >>");

		cId = new ClientId(null, cName, "test", "test");

		setClientLocation();

		try {
			centralServer = (CentralServerInterface) Naming.lookup("rmi://"
					+ Constants.CS_IP + "/" + Constants.CS_NAME);

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
			System.err.println("Couldn't get the location for client "
					+ cId.getClientName());
			e.printStackTrace();
		} catch (ExecutionException e) {
			System.err.println("Couldn't get the location for client "
					+ cId.getClientName());
			e.printStackTrace();
		}

		es.shutdown();
	}

	public static void copy(InputStream in, OutputStream out)
			throws IOException {

		byte[] b = new byte[BUF_SIZE];
		int len;
		while ((len = in.read(b)) >= 0) {
			out.write(b, 0, len);
		}
		in.close();
		out.close();
	}

	public void downloadFile(File src, File dest) throws IOException {
		if (cId.getLocation() == null) {
			System.out
					.println("Client location unavailibe. Please try again later.");
			System.exit(0);
		}

		LocalServerInterface localServer = centralServer.getLocalServer(cId
				.getLocation());

		if (localServer == null) {
			System.out.println("Server unavailibe. Please try again later.");
			System.exit(0);
		}

		System.out.println("\nDownloading from: "
				+ localServer.getLocalServerName());
		copy(localServer.getInputStream(src), new FileOutputStream(dest));

		System.out.println("Finished downloading from: " + localServer.getLocalServerName());
				+ localServer.getLocalServerName());
	}

	public void uploadFile(File src, File dest) throws IOException {
		if (cId.getLocation() == null) {
			System.out
					.println("Client location unavailibe. Please try again later.");
			System.exit(0);
		}

		LocalServerInterface localServer = (LocalServerInterface) centralServer
				.getLocalServer(cId.getLocation());
		if (localServer == null) {
			System.out.println("Server unavailibe. Please try again later.");

			if (src.exists()) {
				File file2 = new File(dest + "_copy");
				dest.renameTo(file2);

				System.out.println("file is already there");
			} else {
				System.out.println("Not find file ");
			}

			// centralServer.updateFiles(localServer, dest);
		}

		System.out.println("\nUploading to: "
				+ localServer.getLocalServerName());
		copy(new FileInputStream(src), localServer.getOutputStream(dest));
	}

	public void printFiles() {
		try {
			LocalServerInterface localServer = centralServer.getLocalServer(cId
					.getLocation());

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

	/*
	 * public boolean update(){ try { centralServer.update(); } catch
	 * (RemoteException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } return true; }
	 */

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
		System.out
				.println("\t listFiles - prints the available files for downloading");
		System.out
				.println("\t download [file_name] - downloads the requested file");
		System.out
				.println("\t upload [file_name] - uploads the requested file");
		System.out.println("\t exit - closes the session\n");

	}

	private File[] listf(String directoryName, String destination) {

		// .............list file
		File directory = new File(directoryName);

		// get all the files from a directory
		File[] fList = directory.listFiles();

		for (File file : fList) {
			if (file.isDirectory()) {
				String outFileName = destination + "\\"+ "Out_" + file.getName();
				File dest = new File(outFileName);
				dest.mkdir();
				System.out.println("Directory copied ");
				listf(file.getAbsolutePath(), outFileName);
			} else {
				String outFileName = destination + "\\" + "Out_" + file.getName();
				File dest = new File(outFileName);

				try {
					downloadFile(file, dest);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		System.out.println(fList);
		return fList;
	}

	public void run() throws IOException {
		long startTime = System.currentTimeMillis() / 1000;
		long endTime;
		long elapsedTime;
		LocalServerInterface localServer;
		Scanner reader = new Scanner(System.in);

		printUsage();

		String[] userInput = reader.nextLine().split(" ");
		String command = userInput[0];
		localServer = centralServer.getLocalServer(cId.getLocation());

		while (!command.equals(EXIT)) {

			switch (command) {
			case LIST_FILES:
				printFiles();
				break;

			case DOWNLOAD:
				filesAvailableForDownload = Arrays.asList(localServer
						.listFiles());

				// file to be downloaded
				String fileToDownloadName = userInput[1];

				if (!filesAvailableForDownload.contains(fileToDownloadName)) {
					System.out.println("File << " + fileToDownloadName
							+ ">> is not on the server!");
					break;
				}
				String outFileName = "Out_" + fileToDownloadName;
				String inFileName = localServer.getStoragePath() + "\\"
						+ fileToDownloadName;
				File src = new File(inFileName);
				if (src.isFile()) {
					File dest = new File(outFileName);
					downloadFile(src, dest);
				} else {
					File dest = new File(outFileName);
					dest.mkdir();
					listf(inFileName, dest.getName());
				}

				break;

			case UPLOAD:
				String fileToUploadName = userInput[1];
				File srcUp = new File(fileToUploadName);

				if (!srcUp.exists()) {
					System.err.println("File <<" + fileToUploadName
							+ " >> doesn't exit on file system!");
					break;
				}

				File destUp = new File("In_" + fileToUploadName);
				uploadFile(srcUp, destUp);
				break;

			default:
				printUsage();
				System.err.println("Unrecognized command! Please retry!");
			}


			endTime = System.currentTimeMillis() / 1000;
			elapsedTime = endTime - startTime;
			
			if (elapsedTime > UPTIME){
				reconnect();
				reader.close();
			}
			
			userInput = reader.nextLine().split(" ");
			command = userInput[0];
		}

		// process exit command
		shutdown();

		reader.close();
	}

	private void reconnect() throws IOException {
		System.out.println("\nInitializing client : <<" + cId.getClientName() + " >>");
		setClientLocation();
		run();
	}

}