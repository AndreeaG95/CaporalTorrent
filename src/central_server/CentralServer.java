package central_server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	private List<LocalServerInterface> servers;
	private HashMap<LocalServerInterface, ArrayList<Client>> serversToClients;
	
	public CentralServer() throws RemoteException {
		super();
		System.out.println("Initializing " + Constants.CS_NAME + " ...");
		
		//count how many  clients are connected to a local server
		serversToClients = new HashMap<>();
	}

	@Override
	public void registerLocalServer(String lsName) throws RemoteException {
		try {
			LocalServerInterface newLocalServer = (LocalServerInterface) Naming
					.lookup("rmi://" + Constants.CS_IP + "/" + lsName);
			
			ArrayList<Client> clients = new ArrayList<Client>();
			serversToClients.put(newLocalServer,clients);
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
		Location clientLocation = c.getLocation();
		Location lsLocation;
		double minDistance = Double.MAX_VALUE;


		// detect which ls is closest to client
		for (Entry<LocalServerInterface, ArrayList<Client>> e : serversToClients.entrySet()) {
			LocalServerInterface currLS = e.getKey();
			Location currentLsLoc = currLS.getLS_Location();
			double currentDistance = clientLocation.getDistance(currentLsLoc);
			
			if (currentDistance < minDistance){
				minDistance = currentDistance;
				nearestLS = currLS;
			}
		}

		serversToClients.get(nearestLS).add(c);
		return nearestLS;
	}

}
