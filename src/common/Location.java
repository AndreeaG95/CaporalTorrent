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

	// formula for computing the distance: https://www.geodatasource.com/developers/java
	/**
	 * 
	 * @param other
	 * @return distance in km 
	 */
	public double getDistance(Location other) {
		double theta = this.longitude - other.longitude;
		double dist = Math.sin(deg2rad(this.latitude)) * Math.sin(deg2rad(other.latitude)) + Math.cos(deg2rad(this.latitude)) * Math.cos(deg2rad(other.latitude)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;

		// we are using kilometers.
		dist = dist * 1.609344;

		return (dist);
	}
	
	/********************************************************************/
	/*	This function converts decimal degrees to radians				*/
	/********************************************************************/
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/********************************************************************/
	/*	This function converts radians to decimal degrees				*/
	/********************************************************************/
	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
}
