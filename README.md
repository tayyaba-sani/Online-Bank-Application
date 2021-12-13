# Online-Bank-Application
This application provides users to access the following features:  
- Creating/deleting account  
- Checking account details and transaction history   
- Debit/credit from their account  
- Applying for Debit/Credit cards   
- Checking cards details  
- Activating/deactivating the cards  
- Applying for loan    
- Monitoring loans details and history  
- Receiveing email notification  

Full list of available REST endpoints could be found in Swagger UI, which could be called using following links:  
Accounts: **http://localhost:8080/swagger-ui/#/**  
Cards: **http://localhost:9000/swagger-ui/#/**  
Loans: **http://localhost:8090/swagger-ui/#/**


### Microservices  
This project consist of 4 microservices.  
1) Accounts: This service will provide users to create/ delete accounts, debit/credit from their accounts, get account details also along with cards and loan details
 and check bank balance.  
2) Cards: This service will provide users to apply for cards, activate/deactivate their cards and get all card details.  
3) Loans: This service will provide users to apply for loans, get loan details and paid loan amount.  
4) Notification: This service will consumes all the messages from RabbitMQ and pushes necessary email notifications to the end users.

### Architecture Diagram
<img src="images/Project Architecture Diagram.png">

### Technolgies used
1) Java 11  
2) Spring Boot 2.5.5  
3) Netflix Eureka Service Registry  
4) Netflix Eureka Service Client  
5) Spring Cloud API Gateway  
6) Spring Cloud Config Server  
7) Zipkin  
8) Spring Cloud Sleuth  
9) Open Feign  
10) RabbitMQ  
11) Prometheus  
12) Resilience4j
13) PostgreSQL  
14) ELK Stack  
15) Docker  
16) Swagger  

### Maven Project  
After cloning the project you can import it to your IDE, then run the "mvn clean install" command  

