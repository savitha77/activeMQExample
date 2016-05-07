package ss.example.activemq;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldProducer implements Runnable {
    transient int k = 0;

    Logger log = LoggerFactory.getLogger(HelloWorldProducer.class);

    public void run() {
        try {

            // Create a Connection
            Connection connection = ActiveMqExampleConnectionFactory.getConnectionFactor().createConnection();
            connection.start();

            // Create a Session
            Session session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);

            // session.recover();

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue(ActiveMqExampleConnectionFactory.QUEUE_NAME);

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            // Create a messages

            for (int j = 0; j < 10; j++) {
                log.debug("Sending the message ");
                TextMessage message = session.createTextMessage("HelloWorld -- " + k++);
                producer.send(message);
                Thread.sleep(3000l);
                session.commit();
            }

        } catch (Exception e) {
            log.error("Exception", e);
        }
    }

}
