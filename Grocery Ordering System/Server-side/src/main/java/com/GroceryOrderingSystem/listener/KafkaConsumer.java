package com.GroceryOrderingSystem.listener;

import com.GroceryOrderingSystem.communication.Forward_accept_Int;
import com.GroceryOrderingSystem.communication.Key_Store_Int;
import com.GroceryOrderingSystem.model.CartItem;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.*;

@Service
public class KafkaConsumer extends Forward_accept_Int_Imp {

    private static Logger lgm;           //static class variables for global access
    public static Handler fhl;

    private static final String server_addr = "127.0.0.1";
    private static final int server_port1 = 7051;
    private static final int server_port2 = 7052;
    private static final int server_port3 = 7053;

    private static Registry registry1;
    private static Registry registry2;
    private static Registry registry3;

    private static Key_Store_Int send_data1;
    private static Key_Store_Int send_data2;
    private static Key_Store_Int send_data3;

    private static boolean ServerStatus1 = true;
    private static boolean ServerStatus2 = true;
    private static boolean ServerStatus3 = true;

    private static int count_servers = 0;          // registry objects
    private static int ServerNo = 1;
    private static boolean isFirst = true;

    static                                //static block for formatter output and logger
    {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS.%1$tL %1$Tp %2$s%n%4$s: %5$s%n");
        lgm = Logger.getLogger((KafkaConsumer.class.getClass().getName()));
    }

    public KafkaConsumer() throws Exception {
    }

    public static void refresh_registry() throws Exception{ // function to connect to all server's remote objects

        registry1 = LocateRegistry.getRegistry("127.0.0.1", server_port1);    // bind to the registry provided by the
        try {
            send_data1 = (Key_Store_Int) registry1.lookup("Key_Store_Int_Imp");   //server
            ServerStatus1 = true;
        } catch (Exception e) {
            ServerStatus1 = false;
        }

        registry2 = LocateRegistry.getRegistry("127.0.0.1", server_port2);    // bind to the registry provided by the
        try {
            send_data2 = (Key_Store_Int) registry2.lookup("Key_Store_Int_Imp");   //server
            ServerStatus2 = true;
        } catch (Exception e) {
            ServerStatus2 = false;
        }


        registry3 = LocateRegistry.getRegistry("127.0.0.1", server_port3);    // bind to the registry provided by the
        try {
            send_data3 = (Key_Store_Int) registry3.lookup("Key_Store_Int_Imp");   //server
            ServerStatus3 = true;
        } catch (Exception e) {
            ServerStatus3 = false;
        }
    }

    @KafkaListener(topics = "channel", groupId = "group_id", containerFactory = "kafkaListenerFactory")
    public void consumeJson(CartItem cartItem) throws Exception {
        System.out.println("Consumed Json: " + cartItem.toString());
        System.out.println("Server Status: " + ServerStatus1 + " " + ServerStatus2 + " " + ServerStatus3);

        refresh_registry();

        Forward_accept_Int accept = new Forward_accept_Int_Imp();    // create registry for all servers to access

        if (isFirst) {
            Registry registry_coordinator = LocateRegistry.createRegistry(7055);  //all forwarded requests come here
            registry_coordinator.bind("Forward_Accept", accept);                   // through this interfaces remote method
            lgm.log(Level.INFO, "OBJECT TO ACCEPT REQUESTS BOUND SUCCESSFULLY");
            isFirst = false;
        }

        System.out.println("Waiting for servers to expose remote methods...");
//        TimeUnit.SECONDS.sleep(10);                       // wait for all servers to come up
//            refresh_registry();
        System.out.println("Waiting for requests...");

        count_servers = 3;

        if (ServerStatus1) {
            ServerNo = 1;
            put_pair("Item", cartItem.toString(), send_data1);
        } else if (ServerStatus2) {
            ServerNo = 2;
            put_pair("Item", cartItem.toString(), send_data2);
        } else {
            ServerNo = 3;
            put_pair("Item", cartItem.toString(), send_data3);
        }
    }

    public static void put_pair(String key, String val, Key_Store_Int obj) throws Exception{

        ArrayList<String> response;
        ArrayList<String> requests = new ArrayList<>();             //function to send the key-value pair to the server
        requests.add("put");
        requests.add(key);
        requests.add(val);
        System.out.println("Sending Put req" + requests);
        lgm.log(Level.INFO,"PUT-request sent to the server "+ requests);

        response = obj.remote_put_pair(requests, ServerNo);       //log necessary information in the logger and accept response

        System.out.println("Server message: " + response);
        lgm.log(Level.INFO,"Server response "+ response);

        if (response.get(0).startsWith("ERR")) {
            if (ServerNo == 1) {
                if (ServerStatus2) {
                    System.out.println("Sending message to Server-2");
                    ServerNo = 2;
                    put_pair(key, val, send_data2);
                } else if (ServerStatus3) {
                    System.out.println("Sending message to Server-3");
                    ServerNo = 3;
                    put_pair(key, val, send_data3);
                }
            }
            else if (ServerNo == 2) {
                if (ServerStatus3) {
                    System.out.println("Sending message to Server-3");
                    ServerNo = 3;
                    put_pair(key, val, send_data3);
                } else if (ServerStatus1) {
                    System.out.println("Sending message to Server-1");
                    ServerNo = 1;
                    put_pair(key, val, send_data1);
                }
            }
            else if (ServerNo == 3) {
                if (ServerStatus1) {
                    System.out.println("Sending message to Server-1");
                    ServerNo = 1;
                    put_pair(key, val, send_data1);
                } else if (ServerStatus2) {
                    System.out.println("Sending message to Server-2");
                    ServerNo = 2;
                    put_pair(key, val, send_data2);
                }
            }
        }
    }

}

class Forward_accept_Int_Imp extends UnicastRemoteObject implements Forward_accept_Int {

    public ArrayList temp;
    ArrayList response_to_caller;
    ArrayList helper;

    public static Logger lgm;           //static class variables for global access
    public static Handler fhl;
    public static Logger LOGGER = Logger.getLogger(String.valueOf(KafkaConsumer.class));

    static                                //static block for formatter output and logger
    {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS.%1$tL %1$Tp %2$s%n%4$s: %5$s%n");
        lgm = Logger.getLogger((KafkaConsumer.class.getClass().getName()));
    }

    public static void filing_logging() throws Exception{      //handling output of logger into a file

        fhl = new FileHandler("./Coordinator.log");
        SimpleFormatter simple = new SimpleFormatter();
        fhl.setFormatter(simple);
        lgm.addHandler(fhl);

    }

    public Forward_accept_Int_Imp() throws Exception {
        super();
        filing_logging();
    }

    public ArrayList remote_forward(ArrayList request) throws Exception{
        temp = request;                                 // accept the forwarded request from the server
        while (helper == null){
            helper = response_to_caller;               // wait for the main() function to set the response
        }                                              // to be returned to the client
        helper = null;

        return this.response_to_caller;
    }

    public ArrayList getter() throws Exception{
        return this.temp;
    }

    public void setter() throws Exception {
        this.temp = null;
        this.response_to_caller = null;
    }

    public void setter_response(ArrayList resp) throws Exception {
        this.response_to_caller = resp;
    }

}

