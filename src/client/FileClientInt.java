package client;

/*
 * FileClientInt.java
 * Copyright (C) 2018 marius <marius@DESKTOP-1QRGQGP>
 *
 * Distributed under terms of the MIT license.
 */

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileClientInt extends Remote {

	public boolean sendData(String filename, byte[] data, int len) throws RemoteException;

	public String getName() throws RemoteException;
}
