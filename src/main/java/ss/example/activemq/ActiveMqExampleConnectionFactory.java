package ss.example.activemq;

import java.util.concurrent.ThreadPoolExecutor;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.broker.region.policy.RedeliveryPolicyMap;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.pool.PooledConnectionFactory;

public class ActiveMqExampleConnectionFactory { 

    public static final String QUEUE_NAME="ActiveMQ.QUEUE";
    
    public static ActiveMQConnectionFactory getConnectionFactor() {
        final PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setMaxConnections(3);
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory
                ("failover:(tcp://localhost:61616)?initialReconnectDelay=100&timeout=150000");
        connectionFactory.setNonBlockingRedelivery(true);
        connectionFactory.setRedeliveryPolicyMap(createRedeliverPolicy());
        connectionFactory.setAlwaysSessionAsync(true);
        connectionFactory.setRejectedTaskHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        pooledConnectionFactory.setConnectionFactory(connectionFactory);

        return connectionFactory;
    }

    private static RedeliveryPolicyMap createRedeliverPolicy() {
        final RedeliveryPolicyMap redeliveryPolicyMap = new RedeliveryPolicyMap();
        redeliveryPolicyMap.put(new ActiveMQQueue(">"), policy());
        return redeliveryPolicyMap;
    }

    private static RedeliveryPolicy policy() {
        final RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setBackOffMultiplier(2);
        redeliveryPolicy.setInitialRedeliveryDelay(2000l);
        redeliveryPolicy.setUseExponentialBackOff(true);
        redeliveryPolicy.setMaximumRedeliveries(20);
        redeliveryPolicy.setRedeliveryDelay(2000l);

        return redeliveryPolicy;
    }

}
