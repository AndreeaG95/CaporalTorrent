package client;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Location;

public class TestLocation {
	public static void main(String[] args) {
		try {
			File dbFile = new File("D:\\proiecte\\NewEcWorkspace\\CaporalTorrent\\GeoLite.mmdb");
			DatabaseReader reader = new DatabaseReader.Builder(dbFile).build();
			InetAddress ipAddress = InetAddress.getByName("192.168.0.1");
			
			CityResponse cityResponse = reader.city(ipAddress);
			Location location = cityResponse.getLocation();
			
			//FIXME the current ip address is not found in the DB
			System.out.println(location.getLatitude());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GeoIp2Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
