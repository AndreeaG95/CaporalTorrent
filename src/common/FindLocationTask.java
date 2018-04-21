package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

public class FindLocationTask implements Callable<Location> {
	private LocationDetectedListener listener;

	
	public FindLocationTask(LocationDetectedListener listener) {
		this.listener = listener;
	}


	@Override
	public Location call() throws Exception {
		URL whatismyip;
		Location location = null;
		try {
			long starttime = System.currentTimeMillis();
			whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			
			String internetIp = in.readLine(); // you get the IP as a String
			System.out.println(internetIp);
			
			URL ipToLocationUrl = new URL("https://ipinfo.io/" + internetIp);
			HttpURLConnection connection = (HttpURLConnection) ipToLocationUrl.openConnection();
			connection.setRequestMethod("GET");
			
			//TODO maybe delete these lines, see if the location detection is faster
			//connection.setReadTimeout(2000);
			//connection.setConnectTimeout(2000);
			
			// execute the request
			int status = connection.getResponseCode();
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			
			while (((inputLine = bufferedReader.readLine()) != null) && (!inputLine.contains("Longitude"))) {
				//System.out.println(inputLine);
			}
			inputLine = bufferedReader.readLine();
			String[] latLongArr= inputLine.trim().substring(14, 29).split(",");
			
			Float latitude = Float.valueOf(latLongArr[0]);
			Float longitude = Float.valueOf(latLongArr[1]);
			
			//System.out.println("Latitude: " + latitude + " , Longitude:" + longitude);
			
			//long endTime = System.currentTimeMillis();
			//System.out.println("Total time:" + (endTime- starttime) / 1000 + " sec");
			
			bufferedReader.close();
			
			location = new Location(latitude, longitude);

			listener.locationDetected();
			
			return location;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Failed to handle http response when trying to find local server location");
			e.printStackTrace();
		}		return null;
		
	}

}
