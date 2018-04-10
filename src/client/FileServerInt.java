package client;

/*
 * FileServerInt.java
 * Copyright (C) 2018 marius <marius@DESKTOP-1QRGQGP>
 *
 * Distributed under terms of the MIT license.
 */
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileServerInt extends Remote {

	public boolean login(FileClientInt c) throws RemoteException;
}
