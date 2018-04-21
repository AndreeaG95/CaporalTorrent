package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import common.Location;

public class LocationTest {

	@Test
	public void test1() {
		Location loc1 = new Location(-35.84, 21.35);
		Location loc2 = new Location(45.89, 21.28);

		double distance = loc1.getDistance(loc2);
		System.out.println(distance);
		
		assertTrue(9000 < distance);

	}

}
