package src;

import java.rmi.Naming;

public class StockMarketClient {
	public static void main(String[] args) {
		try {
			ServerInterface market = (ServerInterface) Naming.lookup("rmi://192.168.0.101/NASDAQ");

			float price = market.get_price("ABC SRL");

			System.out.println("Price is " + price);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
