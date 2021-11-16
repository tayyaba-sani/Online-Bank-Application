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

### Microservices
This project consist of 3 microservices.  
1) Accounts: This service will provide users to create/ delete accounts, debit/credit from their accounts, get account details also along with cards and loan details
 and check bank balance.  
2) Cards: This service will provide users to apply for cards, activate/deactivate their cards and get all card details.  
3) Loans: This service will provide users to apply for loans, get loan details and paid loan amount.  
4) Notification: This service will consumes all the messages from RabbitMQ and pushes necessary email notifications to the end users.

### Architecture Diagram
<img src="images/Architecture Diagram.png" >

### Technolgies used
1. 
