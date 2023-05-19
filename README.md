# Distributed-Grocery-Ordering-System (2021)

> Tools - Java, SpringBoot, JAVA RMI, MongoDB, MySQL, Kafka

Grocery Ordering System using MongoDB to store contents of the items (key-value pairs), MySQL to store user-authentication information and Kafka to process requests. Involves multiple-servers for showcasing fault-tolerance, that is if one server goes down, the requests would be forwarded to the other servers that are up and running.

## About
The system has a user facing experience on the CMD prompt. The users/clients have been pre-populated with cart entries. The project mainly focuses on structuring the backend aspect, so client-side functionalities have been left to a minimum for the scope of this project. Multiple clients can send their order details at the same time, which are then stored in a NoSQL (MongoDB) database. This database comprises the customer details and their orders. Moreover, a separate SQL database accounts for the authentication aspect. KAFKA has been used to provide messaging services.

## Implementation Overview
Multiple clients can record their cart details into the datastore. Firstly, the client would be authenticated into the system, using a predefined customer details store. The client sends the request to the centralized server which we can call as the routing server or load balancer since its job is to forward the requests to the array of servers. Depending upon the load and occupancy of each server, the service would pass the request to a particular node which is free or can handle more requests at that instance. The load balancing here is carried out using the round-robin method. Thus, on a cyclical basis, client requests are forwarded to available servers. The round-robin method performs well when the servers have about similar computation and storage capacities. Multiple server replicas are made to exist, each containing the same remote methods. The client would be able to make a request to submit the cart-details. Here, an assumption has been made that the users are already registered into the system and so, eliminating the need for a user registration module. The user will not be allowed to proceed in case of an authentication failure, else the user will be asked to select the items for its cart. The list of items is fetched from the mongoDB server, as it contains the list of items, quantities, and price. Once the user fills its cart (currently pre-defined), the request is sent to the server to process. The servers in turn, upon receiving the requests, would create a thread for the request. The servers are multi-threaded for concurrency. The mongoDB store contains the inventory details for the goods available currently. For instance, if the user makes a request for 5 qty of “Wafers”, and if the qty is available, the server fulfils the order request and updates the respective transactions in the datastore. If the order quantity exceeds the current inventory stock, then the order does not get fulfilled and the client is sent back an error response. The status of the order is notified back to the client using a kafka topic. Replication can be set in mongoDB for the data-store to guarantee fault-tolerance and high-availability.

## Sample Execution (Windows)

- Download Kafka, put it in C: and navigate to this directory
- Command to start zookeeper service (Kafka needs Zookeeper to run): `.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties`
- Command to start KAFKA service: `.\bin\windows\kafka-server-start.bat .\config\server.properties`
- Command to create topic (response) in KAFKA: `.\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic <<topic_name>>`
- Command to list all topics in kafka: `.\bin\windows\kafka-topics.bat --list --zookeeper localhost:2181`
- Command to start consumer of kafka topic (response): `.\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic <<topic_name>> --from-beginning`
- Intall and setup MongoDB Community Sever (latest version)
- Intall and setup MySQL Community Server with Workbench (latest version)
- Now open the server side project, since the code has been divided into two seperate sub-projects (client and server). Start the server applications 1, 2,and 3 first.
- Then start the `groceryoderingSystemServerApplication`.
- Open the client project and run the `groceeryOrderingSystemApplication`.

## Database Structure

**MySQL (User & Auth)**
<p align="center">
<img src="https://github.com/divitvasu/Distributed-Grocery-Ordering-System/assets/30820920/bae55521-066c-4a9c-9aea-2279c12ce8de" alt="Image" width="600" height="275">
</p>

**MongoDB (Store)**
<p align="center">
<img src="https://github.com/divitvasu/Distributed-Grocery-Ordering-System/assets/30820920/02149b1b-63ef-4a92-addf-ce0b5a6d1ed9" alt="Image" width="600" height="300">
</p>

## Shortcomings
This project could potentially be extended with added functionalities such as an user-interface, payment gateway, a database for facilitating logistics etc. However, this project has not delved into these added functionalities and has only focussed on the backend aspects, in the interest of time. User passwords could also be hashed and stored and various other security measures could be implemented.

## Contributors
@ParshvaTimbadia

## References and Resources
- [Kafka Setup](https://kafka.apache.org/quickstart)
- [Spring Boot Setup](https://docs.spring.io/spring-boot/docs/current/reference/html/getting-started.html)
- [Replication in MongoDB](https://www.mongodb.com/presentations/replication-election-and-consensus-algorithm-refinements-for-mongodb-3-2)
- [Leader Election and Raft Consensus](https://medium.com/geekculture/raft-consensus-algorithm-and-leader-election-in-mongodb-vs-coachroachdb-19b767c87f95)
