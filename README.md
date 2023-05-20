# Distributed-Grocery-Ordering-System (2021)

> Tools - Java, SpringBoot, JAVA RMI, MongoDB, MySQL, Kafka

Grocery Ordering System using MongoDB to store contents of the available products (key-value pairs), MySQL to store user-authentication information and Kafka to process requests. Involves multiple-servers for showcasing fault-tolerance, that is if one server goes down, the requests would be forwarded to the other servers that are up and running.

## About
The system has a user facing experience on the CMD prompt. The users/clients have been pre-populated with cart entries. The project mainly focuses on structuring the backend aspect, so client-side functionalities have been left to a minimum. Multiple clients can send their order details at the same time, which are then queried against a NoSQL (MongoDB) database for checking satisfiability from the inventory. Confirmed orders are then sent to a Kafka queue to be ultimately stored in a separate MySQL database. Moreover, a separate SQL database accounts for the authentication aspect.

## Implementation Overview
Multiple clients can record their cart details into the datastore. Firstly, the client would be authenticated into the system, using a predefined customer details store. The client sends the request to the centralized server which can be referred to as the routing server/load-balancer/coordinator since its job is to forward the requests to the array of servers. Depending upon the availability of each server, the service would pass the request to a particular node which is free or can handle more requests at that instance. The load balancing here is carried out using the round-robin method. Thus, on a cyclical basis, client requests are forwarded to available servers. The round-robin method performs well when the servers have about similar computation and storage capacities. Multiple server replicas are made to exist, each containing the same remote methods. The client would be able to make a request to submit the cart-details. Here, an assumption has been made that the users are already registered into the system and so, eliminating the need for a user registration module. The user will not be allowed to proceed in case of an authentication failure, else the user will be asked to select the items for its cart. The list of items is fetched from the mongoDB server, as it contains the list of items, quantities, and price. Once the user fills its cart (currently pre-defined), the request is sent to the server to process. The servers in turn, upon receiving the requests, would create a thread for the request. The servers are multi-threaded for concurrency. The mongoDB store contains the inventory details for the goods available currently. For instance, if the user makes a request for 5 qty of “Wafers”, and if the qty is available, the server fulfils the order request and updates the respective transactions in the datastore. If the order quantity exceeds the current inventory stock, then the order does not get fulfilled and the client is sent back an error response, else if the order can be satisfied, it is sent to the kafka topic for further processing and storing in a database table. Replication can be set in mongoDB for the data-store to guarantee fault-tolerance and high-availability.

## Architecture
<p align="center">
<img src="https://github.com/divitvasu/Distributed-Grocery-Ordering-System/assets/30820920/82bebb2f-5f76-4b8d-8204-8679bfbce608" alt="Image" width="600" height="300">
</p>

## Sample Execution (Windows)
- Download Kafka, put it in C: and navigate to this directory
- Command to start zookeeper service (Kafka needs Zookeeper to run): `.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties`
- Command to start KAFKA service: `.\bin\windows\kafka-server-start.bat .\config\server.properties`
- Command to create topic (orders) in KAFKA: `.\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic <<topic_name>>`
- Command to list all topics in kafka: `.\bin\windows\kafka-topics.bat --list --zookeeper localhost:2181`
- Command to start consumer of kafka topic (orders): `.\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic <<topic_name>> --from-beginning`
- Intall and setup MongoDB Community Sever (latest version)
- Intall and setup MySQL Community Server with Workbench (latest version)
- Now open the server side project, since the code has been divided into two seperate sub-projects (client and server). Start the server applications 1, 2,and 3 first.
- Then start the `groceryoderingSystemServerApplication`.
- Open the client project and run the `groceeryOrderingSystemApplication`.

**Server-Side App Execution**
Showing three servers and one central server running and showing the central server's log after processing the client's request. `Server status: true true true` here implies that all three servers were available for handling the request.
<p align="center">
<img src="https://github.com/divitvasu/Distributed-Grocery-Ordering-System/assets/30820920/00a110d1-3906-4433-8ea9-f64195db6e4f" alt="Image" width="600" height="150">
</p>

<p align="center">
<img src="https://github.com/divitvasu/Distributed-Grocery-Ordering-System/assets/30820920/3fc35f43-f952-4291-af27-4002807ef234" alt="Image" width="600" height="150">
</p>

**Client-Side App Execution**
Showing client app running with a response from the server, after client sent the cart details for fulfillment to the server
<p align="center">
<img src="https://github.com/divitvasu/Distributed-Grocery-Ordering-System/assets/30820920/9825a5ee-4e2c-4712-b8d0-ce9261035b49" alt="Image" width="600" height="150">
</p>

## Database Structure
**MySQL (User & Auth)**
<p align="center">
<img src="https://github.com/divitvasu/Distributed-Grocery-Ordering-System/assets/30820920/adb1e548-375e-4def-8b47-5f366a740c98" alt="Image" width="550" height="250">
</p>

**MySQL (Confimed Orders)**
<p align="center">
<img src="https://github.com/divitvasu/Distributed-Grocery-Ordering-System/assets/30820920/a2aac413-d874-459d-8529-4eae2d75f4ed" alt="Image" width="550" height="250">
</p>

**MongoDB (Products)**
<p align="center">
<img src="https://github.com/divitvasu/Distributed-Grocery-Ordering-System/assets/30820920/02149b1b-63ef-4a92-addf-ce0b5a6d1ed9" alt="Image" width="600" height="300">
</p>

## Shortcomings and Future Scope
This project is more aimed towards demonstrating the learnings imbibed from coursework in distributed systems, thus, is not extensive in nature. This project could potentially be extended with added functionalities such as an user-interface, payment gateway, a database for facilitating logistics, and a proper frontend etc. Also, Redis could been used for caching. User passwords could also be hashed and stored and various other security measures could be implemented. This project currently works with a fixed number of servers, however, that could be improved to be made dynamic. Say, a new server node could be automatically deployed, when the network load or requests cross a predefined threshold. A gateway could also be used for effectively routing the client requests to servers. A server could be made to send a busy response to the coordinator when the resources, requests cross a certain threshold. Another improvement would be to deal with the possible single point of failure, the coordinator node, by using a master/slave architecture for added redundancy. However, this project has not delved into these added functionalities and has only focussed on the critical backend aspects, in the interest of time.

Overall, to summarize, the server module can take in requests from the client module, process and fetch the availability of the order items from a data-store through distributed replicas of the server. The client need not know which replica of the server it is being served by.

## Contributors
@ParshvaTimbadia

## References and Resources
- [Kafka Setup](https://kafka.apache.org/quickstart)
- [Spring Boot Setup](https://docs.spring.io/spring-boot/docs/current/reference/html/getting-started.html)
- [Replication in MongoDB](https://www.mongodb.com/presentations/replication-election-and-consensus-algorithm-refinements-for-mongodb-3-2)
- [Leader Election and Raft Consensus](https://medium.com/geekculture/raft-consensus-algorithm-and-leader-election-in-mongodb-vs-coachroachdb-19b767c87f95)
