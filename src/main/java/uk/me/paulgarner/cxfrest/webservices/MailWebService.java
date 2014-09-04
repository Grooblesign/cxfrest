package uk.me.paulgarner.cxfrest.webservices;

import java.util.ArrayList;
import java.util.List;

import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.activemq.ActiveMQConnectionFactory;

import uk.me.paulgarner.cxfrest.models.MailMessage;

@Path("/mail")
@Produces("application/xml")
public class MailWebService {

	@GET
	@Path("/")
	public Response getMessages() {

		List<MailMessage> messages = new ArrayList<MailMessage>();

		return Response.ok(messages).build();
	}

	@POST
	@Path("/")
	public Response postdMessage(MailMessage message) {

		String username = "admin";
		String password = "admin";
		String queueName = "Mail-Incoming";
		String queueUrl = "tcp://localhost:61616";

		final long messageTTL = 7776000000L; // 90 days TTL

		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(queueUrl);

		try {
			Connection connection = connectionFactory.createConnection(username, password);
			connection.start();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue(queueName);

			TextMessage textMessage = session.createTextMessage();

			MessageProducer messageProducer = session.createProducer(queue);
			messageProducer.setTimeToLive(messageTTL);
			messageProducer.send(textMessage);

			session.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println("Exception: " + exception.getClass().toString()
					+ " - " + exception.getMessage());
		}

		return Response.ok(message).build();
	}

}
