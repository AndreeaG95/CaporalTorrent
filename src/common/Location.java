package common;

public class Location {
	private double latitude;
	private double longitude;

	public Location(double d, double e) {
		super();
		this.latitude = d;
		this.longitude = e;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "(" + latitude + "," + longitude + ")";
	}

	// formula took from https://www.movable-type.co.uk/scripts/latlong.html
	// should write some unit tests to see if it works with negative values (34
	// S = -34)
	/**
	 * 
	 * @param other
	 * @return distance in km 
	 */
	public double getDistance(Location other) {
		double distance = 0;
		int earthRadius = 6371000;
		double phi_1 = toRadians(latitude);
		double phi_2 = toRadians(other.getLatitude());
		double delta_phi = toRadians(other.getLatitude() - latitude);
		double delta_lambda = toRadians(other.getLongitude() - longitude);

		double a = Math.sin(delta_phi / 2) * Math.sin(delta_phi / 2)
				+ Math.cos(phi_1) * Math.cos(phi_2) * Math.sin(delta_lambda / 2) * Math.sin(delta_lambda / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		
		distance = earthRadius - c; //in meters
		
		return distance / 1000;
	}

	private double toRadians(double degrees) {
		return  (degrees * 3.14) / 180;
	}
}
