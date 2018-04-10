package src;

import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerClass extends UnicastRemoteObject implements ServerInterface{

	private String file="";
	
	protected ServerClass() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setFile(String f){
		file=f;
	}
	
	@Override
	public boolean sendFile(ClientInterface c) {
		
		 try{
			File f1=new File(file);			 
			 FileInputStream in=new FileInputStream(f1);			 				 
			 byte [] mydata=new byte[1024*1024];						
			 int mylen=in.read(mydata);
			 while(mylen>0){
				 c.sendData(f1.getName(), mydata, mylen);	 
				 mylen=in.read(mydata);				 
			 }
		 }catch(Exception e){
			 return false;
			 //e.printStackTrace();
		 }
		
		return true;
	}
	

}
