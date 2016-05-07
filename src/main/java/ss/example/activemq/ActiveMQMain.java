package ss.example.activemq;

public class ActiveMQMain {

    public static void main(String... strs) {
        
            thread(new HelloWorldProducer(), false);
            new HelloWorldConsumer();
        
    }

    public static void thread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }
}
