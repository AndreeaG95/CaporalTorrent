package central_server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import local_server.LocalServerInterface;

import common.Constants;
import common.Location;

public class CentralServer extends UnicastRemoteObject implements CentralServerInterface {
	/**
	 * needed for serialization
	 */
	private static final long serialVersionUID = 2L;
	
	private HashMap<LocalServerInterface, ArrayList<File>> serverFiles;
	
	public CentralServer() throws RemoteException {
		super();
		System.out.println("Initializing " + Constants.CS_NAME + " ...");
		
		// Count how many clients are connected to a local server.
		serverFiles = new HashMap<>();
	}

	@Override
	public void registerLocalServer(String lsName) throws RemoteException {
		try {
			LocalServerInterface newLocalServer = (LocalServerInterface) Naming
					.lookup("rmi://" + Constants.CS_IP + "/" + lsName);
			
			ArrayList<File> files = new ArrayList<File>();
			serverFiles.put(newLocalServer,files);
		
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public LocalServerInterface getLocalServer(Location c) throws RemoteException {
		LocalServerInterface nearestLS = null;
		Location clientLocation = c;
		double minDistance = Double.MAX_VALUE;

		// Detect which LS is closest to client.
		for (Entry<LocalServerInterface, ArrayList<File>> e : serverFiles.entrySet()) {
			LocalServerInterface currLS = e.getKey();
			Location currentLsLoc = currLS.getLS_Location();
			double currentDistance = clientLocation.getDistance(currentLsLoc);
			if (currentDistance < minDistance){
				minDistance = currentDistance;
				nearestLS = currLS;
			}
		}
		
		//serversToClients.get(nearestLS).add(c);
		return nearestLS;
	}
	
	public void update(){
		System.out.println("Uploading files to the rest of the LS");
		
		for (Entry<LocalServerInterface, ArrayList<File>> e : serverFiles.entrySet()) {
			LocalServerInterface currLS = e.getKey();
			
			
		}
	}

	final public static int BUF_SIZE = 1024 * 64;
	public void copy(InputStream in, OutputStream out) throws IOException {
        
    	System.out.println("using byte[] read/write");
        byte[] b = new byte[BUF_SIZE];
        int len;
        while ((len = in.read(b)) >= 0) {
            out.write(b, 0, len);
        }
        in.close();
        out.close();
    }

	
	
	public void updateFiles(LocalServerInterface lSserver, File dest) {
		/*ArrayList<File> files = serverFiles.get(lSserver);
		files.add(dest);
		serverFiles.put(lSserver, files);
		
		for (Entry<LocalServerInterface, ArrayList<File>> e : serverFiles.entrySet()) {
			LocalServerInterface currLS = e.getKey();
			
			if(lSserver != currLS){
				try {
					copy(lSserver.getInputStream(dest), currLS.getOutputStream(dest));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}*/
		
	}

}
