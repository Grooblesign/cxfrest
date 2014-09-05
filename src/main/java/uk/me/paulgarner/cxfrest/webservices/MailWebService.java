package uk.me.paulgarner.cxfrest.webservices;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.jms.Connection;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.activemq.ActiveMQConnectionFactory;

import uk.me.paulgarner.cxfrest.models.MailMessage;

@Path("/mail")
public class MailWebService {

	private static final String username = "admin";
	private static final String password = "admin";
	private static final String incomingQueueName = "Mail-Incoming";
	private static final String processedQueueName = "Mail-Processed";
	private static final String queueUrl = "tcp://localhost:61616";
	
	@GET
	@Path("/")
	@Produces("application/xml")
	public List<MailMessage> getMessagesInXML() {

		System.out.println("getMessages in");

		List<MailMessage> messages = new ArrayList<MailMessage>();
		
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(queueUrl);

		try {
			Connection connection = connectionFactory.createConnection(username, password);
			connection.start();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue(processedQueueName);
			
			MessageConsumer messageConsumer = session.createConsumer(queue);
					
			System.out.println("entering loop");
			Message message = null;
			while ((message = messageConsumer.receive(100)) != null) {
				
				TextMessage textMessage = (TextMessage) message;
				
				System.out.println(textMessage.getStringProperty("Sender"));
				System.out.println(textMessage.getStringProperty("Recipient"));
				System.out.println(textMessage.getText());
				
				StringReader reader = new StringReader(textMessage.getText());

				JAXBContext jc = JAXBContext.newInstance("uk.me.paulgarner.cxfrest.models");
				Unmarshaller unmarshaller = jc.createUnmarshaller();
				JAXBElement<MailMessage> root = unmarshaller.unmarshal(new StreamSource(reader), MailMessage.class);  
				messages.add(root.getValue());
			}
			System.out.println("exited loop");

			session.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println("Exception: " + exception.getClass().toString()
					+ " - " + exception.getMessage());
		}

		System.out.println("getMessages out");

		return messages;
	}

	@GET
	@Path("/jtest")
	@Produces("application/json")
	public MailMessage getJsonTest() {
		MailMessage mailMessage = new MailMessage();
		
		mailMessage.setId("TEST");
		mailMessage.setFrom("Abcde");
		mailMessage.setTo("Zyxwv");
		mailMessage.setSubject("Testing");
		mailMessage.setBody("Test body");
		
		return mailMessage;
	}
	
	@GET
	@Path("/")
	@Produces("application/json")
	public List<MailMessage> getMessagesInJson() {

		System.out.println("getMessages in");

		List<MailMessage> messages = new ArrayList<MailMessage>();
		
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(queueUrl);

		try {
			Connection connection = connectionFactory.createConnection(username, password);
			connection.start();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue(processedQueueName);
			
			MessageConsumer messageConsumer = session.createConsumer(queue);
					
			System.out.println("entering loop");
			Message message = null;
			while ((message = messageConsumer.receive(100)) != null) {
				
				TextMessage textMessage = (TextMessage) message;
				
				System.out.println(textMessage.getStringProperty("Sender"));
				System.out.println(textMessage.getStringProperty("Recipient"));
				System.out.println(textMessage.getText());
				
				StringReader reader = new StringReader(textMessage.getText());

				JAXBContext jc = JAXBContext.newInstance("uk.me.paulgarner.cxfrest.models");
				Unmarshaller unmarshaller = jc.createUnmarshaller();
				JAXBElement<MailMessage> root = unmarshaller.unmarshal(new StreamSource(reader), MailMessage.class);  
				messages.add(root.getValue());
			}
			System.out.println("exited loop");

			session.close();
			connection.close();
		} catch (Exception exception) {
			System.out.println("Exception: " + exception.getClass().toString()
					+ " - " + exception.getMessage());
		}

		System.out.println("getMessages out");

		return messages;
	}

	@POST
	@Path("/")
	@Consumes("application/xml")
	@Produces("application/xml")
	public Response postdMessage(MailMessage message) {

		final long messageTTL = 7776000000L; // 90 days TTL

		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				queueUrl);

		try {
			Connection connection = connectionFactory.createConnection(
					username, password);
			connection.start();

			Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue(incomingQueueName);

			TextMessage textMessage = session.createTextMessage();
			
			JAXBContext jc = JAXBContext.newInstance("uk.me.paulgarner.cxfrest.models");
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			StringWriter writer = new StringWriter();
			
			marshaller.marshal(message, writer);

	        textMessage.setText(writer.toString());
			
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
