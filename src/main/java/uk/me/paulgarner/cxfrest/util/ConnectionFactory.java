package uk.me.paulgarner.cxfrest.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {
	public static Connection getConnection() {
		Connection connection = null;

		// local
		// String host = "localhost";
		// String username = "root";
		// String password = "admin";
		// String schema = "lepus";
		
		// remote
		String host = "10.22.99.181:80";
		String username = "admin";
		String password = "qwerty11";
		String schema = "TFCC_PORTAL";	
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(
					String.format("jdbc:mysql://%s/%s?user=%s&password=%s", 
							host, schema, username, password));
		} catch (Exception e) {
			System.out.println("Exception: " + e.getClass().toString() + " - " + e.getMessage());
		}
		
		return connection;
	}
}
