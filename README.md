# Online-Bank-Application
This Application provides users to access following features.  
- Create/Delete account.  
- Debit/Credit from their account.  
- Facility to check bank balance any time.  
- Apply for cards(Debit/Credit).  
- Activate/Deactivate the cards.  
- Facility to check cards details.  
- Apply for loan.  
- Keep a check on loans details.  
- Receive email notification.  

Full list of available REST endpoints could be found in Swagger UI, which could be called using following links:  
Accounts: **http://localhost:8080/swagger-ui/#/**  
Cards: **http://localhost:9000/swagger-ui/#/**  
Loans: **http://localhost:8090/swagger-ui/#/**


### Microservices  
This project consist of 3 microservices.  
1) Accounts: This service will provide users to create/ delete accounts, debit/credit from their accounts, get account details also along with cards and loan details
 and check bank balance.  
2) Cards: This service will provide users to apply for cards, activate/deactivate their cards and get all card details.  
3) Loans: This service will provide users to apply for loans, get loan details and paid loan amount.  
4) Notification: This service will consumes all the messages from RabbitMQ and pushes necessary email notifications to the end users.

### Architecture Diagram
<img src="images/Project Architecture Diagram.png" >

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
12) PostgreSQL  

### Maven Project  
After cloning the project you can import it to your IDE, then run the "mvn clean install" command  

