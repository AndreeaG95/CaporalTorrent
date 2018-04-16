package central_server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import client.Client;
import common.Constants;
import common.FindLocationTask;
import common.Location;
import local_server.LocalServerInterface;

public class CentralServer extends UnicastRemoteObject implements CentralServerInterface {
	/**
	 * needed for serialization
	 */
	private static final long serialVersionUID = 2L;
	private Location csLocation;
	public List<LocalServerInterface> servers;

	public CentralServer() throws RemoteException {
		super();
		System.out.println("Initializing " + Constants.CS_NAME + " ...");
		servers = new ArrayList<>();
		setServerLocation();
	}

	private void setServerLocation() {
		ExecutorService es = Executors.newSingleThreadExecutor();
		FindLocationTask task = new FindLocationTask();
		
		//a Future object can be used to fetch the result of the task when it is available.
		Future<Location> future = es.submit(task);
		
		try {
			csLocation = future.get();
		} catch (InterruptedException e) {
			System.err.println("Couldn't get the location for server " + Constants.CS_NAME);
			e.printStackTrace();
		} catch (ExecutionException e) {
			System.err.println("Couldn't get the location for server " + Constants.CS_NAME);
			e.printStackTrace();
		}
		
		//TODO when location is set notify that the central server can be used
		es.shutdown();
	}

	@Override
	public void registerLocalServer(String lsName) throws RemoteException {
		try {
			LocalServerInterface newLocalServer = (LocalServerInterface) Naming
					.lookup("rmi://" + Constants.CS_IP + "/" + lsName);
			servers.add(newLocalServer);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public LocalServerInterface connect(Client c) throws RemoteException {
		LocalServerInterface nearestLS = null;
		
		//
		nearestLS = servers.get(0);
		int serversSize = servers.size();
		
		//TODO detect which ls is closest to client 
		
		return nearestLS;
	}

}
