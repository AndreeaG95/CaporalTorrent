package src;

import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

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
	public boolean sendFile(ClientInterface c, String file) throws RemoteException {
		try{		 
			 FileInputStream in = new FileInputStream("C:\\Users\\andreeagb\\Desktop\\Facultate\\AnulIV\\DP\\CaporalTorrent\\maria.txt");
			 
			 byte [] mydata=new byte[1024*1024];						
			 int mylen=in.read(mydata);
			 while(mylen>0){
				 c.sendData(file, mydata, mylen);	 
				 mylen = in.read(mydata);				 
			 }
			 
			 in.close();
		 }catch(Exception e){
			 return false;
			 
		 }
		return true;
	}

	@Override
	public ArrayList<String> getAvailibleFiles() throws RemoteException {
		ArrayList<String> obj = new ArrayList<String>();
		
		File folder = new File("C:\\Users\\andreeagb\\Desktop\\Facultate\\AnulIV\\DP\\CaporalTorrent\\files");
		File[] listOfFiles = folder.listFiles();

		System.out.println("Listing files...");
		
		for (File file : listOfFiles) {
			System.out.println(file.getName());
		    if (file.isFile() || file.isDirectory()) {
		        obj.add(file.getName());
		    }
		}
		
		return obj;
	}
	

}
