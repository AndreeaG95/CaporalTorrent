package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import common.Constants;
import common.FindLocationTask;
import common.Location;

public class TestLocation {
	public static void main(String[] args) throws IOException {
		ExecutorService es = Executors.newSingleThreadExecutor();
		FindLocationTask task = new FindLocationTask();
		Future<Location> future = es.submit(task);
		Location location = null;

		try {
			location = future.get();
		} catch (InterruptedException e) {
			System.err.println("Couldn't get the location for server " + Constants.CS_NAME);
			e.printStackTrace();
		} catch (ExecutionException e) {
			System.err.println("Couldn't get the location for server " + Constants.CS_NAME);
			e.printStackTrace();
		}

		System.out.println("Location: " + location);

		es.shutdown();
	}
}
