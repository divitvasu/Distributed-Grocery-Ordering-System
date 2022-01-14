package com.GroceryOrderingSystem.server;

import com.GroceryOrderingSystem.communication.Key_Store_Int;
import com.GroceryOrderingSystem.communication.Key_Store_Int_Imp;
import com.GroceryOrderingSystem.communication.Store;
import com.GroceryOrderingSystem.model.CartItem;
import com.GroceryOrderingSystem.model.Product;
import com.GroceryOrderingSystem.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.logging.*;

@SpringBootApplication
public class Server_app3 implements CommandLineRunner {

    @Autowired
    private ProductRepository repo;
    private static ProductRepository staticRepo;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private static KafkaTemplate<String, String> staticKafkaTemplate;

    public static Logger lgm;           //static class variables for global access
    public static Handler fhl;
    public static int port_listen;

    private static Logger LOGGER = Logger.getLogger(String.valueOf(Server_app3.class));

    static                                //static block for formatter output and logger
    {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS.%1$tL %1$Tp %2$s%n%4$s: %5$s%n");
        lgm = Logger.getLogger((Server_app3.class.getClass().getName()));
    }

    public static void filing_logging() throws Exception{      //handling output of logger into a file

        fhl = new FileHandler("./Server_app3.log");
        SimpleFormatter simple = new SimpleFormatter();
        fhl.setFormatter(simple);
        lgm.addHandler(fhl);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Server_app3.class, args);
        filing_logging();

        port_listen = 7053;

        try{
            Key_Store_Int msObj = new Key_Store_Int_Imp();
            Registry registry = LocateRegistry.createRegistry(port_listen);
            registry.bind("Key_Store_Int_Imp", msObj);
            lgm.log(Level.INFO, "OBJECT AND REGISTRY BINDING SUCCESSFUL");

        }catch(Exception e){
            lgm.log(Level.SEVERE, "UNABLE TO BIND!");
            System.out.println(e);
        }
    }

    public static void checkMart(Store store_obj) {
        CartItem cartItem = new CartItem();
        String objString = store_obj.getMap().get("Item").substring(9);
        objString = objString.substring(0, objString.length() - 1);

        String[] objList = objString.split(",");
        for (String obj: objList) {
            switch (obj.split("=")[0]) {
                case "name":
                    cartItem.setName(obj.split("=")[1].substring(1, obj.split("=")[1].length() - 1));
                    break;
                case " quantity":
                    cartItem.setQuantity(Integer.parseInt(obj.split("=")[1]));
                    break;
                case " email":
                    cartItem.setEmail(obj.split("=")[1].substring(1, obj.split("=")[1].length() - 1));
                    break;
            }
        }

        String response = "";
        List<Product> allProducts = staticRepo.findAll();
        for (Product product: allProducts) {
            if (product.getName().equalsIgnoreCase(cartItem.getName())) {
                if (product.getQuantity() > cartItem.getQuantity()) {
                    int remaining = product.getQuantity() - cartItem.getQuantity();

                    staticRepo.save(new Product(product.getId(), product.getName(), remaining));

                    response = cartItem.getQuantity() + " nos. deducted from " + product.getName();
                    System.out.println(response);
                    SendResponse(cartItem.getEmail(), response);
                    return;
                }
                else {
                    response = "No sufficient quantity of " + product.getName();
                    System.out.println(response);
                    SendResponse(cartItem.getEmail(), response);
                    return;
                }
            }
        }
        response = "There is no such product as " + cartItem.getName();
        System.out.println(response);
        SendResponse(cartItem.getEmail(), response);
    }

    @Override
    public void run(String... args) throws Exception {
        staticRepo = repo;
        staticKafkaTemplate = kafkaTemplate;
    }

    private static void SendResponse(String email, String resMsg) {
        String topic = "response_" + email;
//        System.out.println("Response topic is: " + topic);

        staticKafkaTemplate.send(topic, resMsg);
    }
}
