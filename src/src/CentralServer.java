package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CentralServer extends UnicastRemoteObject implements ServerInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String dir="";
	
	public CentralServer() throws RemoteException {}

	public void setFileLocation(String d){
		dir = d;
	}
	

	@Override
	public float get_price(String Company) {
		float price=12345;
        System.out.println("get_price method executing");
        return price;
	}

	@Override
	// This will actually redirect the download to the local server that is closer to the client.
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

}
