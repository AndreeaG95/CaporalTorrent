package client;

import java.io.File;
import java.io.FileOutputStream;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import local_server.LocalServerInterface;

public class Client {

	public static void main(String[] args) {
		String download = "download";
		String dir = "list";
		String shutdown= "shutdown";
		
		String clientpath;
		String serverpath;
		
		
		try{
			LocalServerInterface server = (LocalServerInterface) Naming.lookup("rmi://localhost/LocalServer1");
			
			//to download a file
			if(download.equals(args[0]))
			{
				serverpath = args[1];
				clientpath= args[2];

				byte [] mydata = server.downloadFile(serverpath);
				System.out.println("downloading...");
				
				File clientpathfile = new File(clientpath);
				FileOutputStream out = new FileOutputStream(clientpathfile);				
	    		
				System.out.println("Finished downloading !");
				
				out.write(mydata);
				out.flush();
		    	out.close();
			}
			
			//to list all the files in a directory
			if(dir.equals(args[0]))
			{
				serverpath = args[1];
				String[] filelist = server.listFiles(serverpath);
				for (String i: filelist)
				{
					System.out.println(i);
				}
			}
			
			//to shutdown the client
			if(shutdown.equals(args[0]))
			{
				System.exit(0);
				System.out.println("Client has shutdown. Close the console");
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("error with connection or command. Check your hostname or command");
		}
	}
}
