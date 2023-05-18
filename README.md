# Distributed-Grocery-Ordering-System (2021)

> Tools - Java, SpringBoot, JAVA RMI, MongoDB, MySQL, Kafka

Grocery Ordering System using MongoDB to store contents of the items (key-value pairs), MySQL to store user-authentication information and Kafka to process requests. Involves multiple-servers for showcasing fault-tolerance, that is if one server goes down, the requests would be forwarded to the other servers that are up and running.

## About
The system has a user facing experience on the CMD prompt. The users/clients have been pre-populated with cart entries. The project mainly focuses on structuring the backend aspect, so client-side functionalities have been left to a minimum for the scope of this project. Multiple clients can send their order details at the same time, which are then stored in a NoSQL (MongoDB) database. This database comprises the customer details and their orders. Moreover, a separate SQL database accounts for the authentication aspect. KAFKA has been used to provide messaging services.

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
![image](https://github.com/divitvasu/Distributed-Grocery-Ordering-System/assets/30820920/bae55521-066c-4a9c-9aea-2279c12ce8de)
![image](https://github.com/divitvasu/Distributed-Grocery-Ordering-System/assets/30820920/02149b1b-63ef-4a92-addf-ce0b5a6d1ed9)



## Shortcomings
This project could potentially be extended with added functionalities such as an user-interface, payment gateway, a database for facilitating logistics etc. However, this project has not delved into these added functionalities and has only focussed on the backend aspects, in the interest of time. User passwords could also be hashed and stored and various other security measures could be implemented.

## Contributors
@ParshvaTimbadia

## References and Resources
- [Kafka Setup](https://kafka.apache.org/quickstart)
- [Spring Boot Setup](https://docs.spring.io/spring-boot/docs/current/reference/html/getting-started.html)
- [Replication in MongoDB](https://www.mongodb.com/presentations/replication-election-and-consensus-algorithm-refinements-for-mongodb-3-2)
- [Leader Election and Raft Consensus](https://medium.com/geekculture/raft-consensus-algorithm-and-leader-election-in-mongodb-vs-coachroachdb-19b767c87f95)
