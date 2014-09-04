package uk.me.paulgarner.cxfrest.webservices;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import uk.me.paulgarner.cxfrest.models.User;
import uk.me.paulgarner.cxfrest.util.ConnectionFactory;

@Path("/user")
@Produces("application/xml")
public class UserWebService {

	@GET
	@Path("/{id}")
	public User getUser(@PathParam("id") Integer id) {

		User user = null;
		
		try {
			Connection connection = ConnectionFactory.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM user WHERE Id=%s", id));
			
			if (resultSet.next()) {
				user = new User();
				user.setId(resultSet.getInt("id"));
				user.setName(resultSet.getString("Name"));
				user.setPassword(resultSet.getString("Password"));
				user.setWibble(resultSet.getString("Wibble"));
			}
			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println("Exception: " + exception.getClass().toString()
					+ " - " + exception.getMessage());
		}
			
		return user;
	}

	@GET
	@Path("/")
	public List<User> getUsers() {

		List<User> users = new ArrayList<User>();

		try {
			Connection connection = ConnectionFactory.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM user ORDER BY Id");
			
			while (resultSet.next()) {
				User user = new User();
				user.setId(resultSet.getInt("id"));
				user.setName(resultSet.getString("Name"));
				user.setPassword(resultSet.getString("Password"));
				user.setWibble(resultSet.getString("Wibble"));

				users.add(user);
			}

			statement.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println("Exception: " + exception.getClass().toString()
					+ " - " + exception.getMessage());
		}

		return users;
	}

}
