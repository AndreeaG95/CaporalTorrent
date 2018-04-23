package client;

import common.Location;

public class ClientId {
	private Location location;
	
	//used as id
	private String cName; 
	
	//used to authenticate the client on CS
	private String cUsername;
	private String cPassword;
	
	public ClientId(Location location, String cName, String cUsername,
			String cPassword) {
		this.location = location;
		this.cName = cName;
		this.cUsername = cUsername;
		this.cPassword = cPassword;
	}
	
	/**
	 * used to update client location
	 * @param loc
	 */
	public void setLocation(Location loc){
		this.location = loc;
	}
	
	public Location getLocation(){
		return location;
	}
	
	public String getClientName(){
		return cName;
	}


}
