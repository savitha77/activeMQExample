package ss.example.activemq;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class HelloWorldConsumer implements MessageListener {

    Session session = null;
    
    Logger log = LoggerFactory.getLogger(HelloWorldConsumer.class);

    public HelloWorldConsumer() {
        try {
            Connection connection = ActiveMqExampleConnectionFactory.getConnectionFactor().createConnection();
            connection.start();
        // Create a Session
            session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue(ActiveMqExampleConnectionFactory.QUEUE_NAME);

            // Create a MessageConsumer from the Session to the Topic or Queue
            MessageConsumer consumer = session.createConsumer(destination);

            consumer.setMessageListener(this);
        } catch (JMSException e) {
             log.error("Exception ", e);
        }
    }

 
    /* (non-Javadoc)
     * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
     */
    public void onMessage(Message message) {
        if(message instanceof TextMessage) {
            
            try {
               log.debug("Message Received ->" + ((TextMessage)message).getText());
            } catch (JMSException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try {
            session.rollback();
        } catch (JMSException e) {
            log.error("Exception ", e);
        }
        throw new RuntimeException("RuntimeException  " + message);

    }

    
}
