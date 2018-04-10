package src;

import java.rmi.Remote;

public interface ClientInterface extends Remote{

	void sendData(String name, byte[] mydata, int mylen);

}
