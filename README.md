# Clip Challenge

## Prerequisites

- Spring Boot
- JPA
- H2
- Maven 4
- Java 11

## Run tests

To run automated integration tests, you just need to execute the following command:
> ./mvnw test

## Run spring boot application
In order to run this project, execute the following:
> ./mvnw spring-boot:run

## Description

This application consist of the following:

- Controller package:  where the basic endpoint is defined.
- Repository package:  repository package for repository classes.
- Model package: where entities are stored.
- exception package: Classes related to the exception handling.
- Request package: objects that represent requests.
- Response package: object that represent responses.
- Service package: Classes that contain business logic.

The project contains a simple API that saves a payment in an in-memory database (for the sake of this example let's use
an in-memory-database). The challenge consists of completing as many of the following steps as possible:

1. Create a new endpoint to list all users that have a payment saved in the database (information about payments should
   be already filled).
2. Create a new endpoint so the API can support a disbursement process:
    - A disbursement process gets all transactions with status new and subtracts a fee of 3.5% per transaction.
    - It updates all transactions with status NEW to PROCESSED.
    - Returns a list of users and the amount they'll get -- Example - User_1 payment: 100, Disbursement: User_1:96.5 (
      Discount the fee)
3. Create an endpoint that returns a report per user:
    - Report:

```json
{
  "user_name": "user name",
  "payments_sum": "the sum of all payments (no matter what's the status)",
  "new_payments": "sum of all new payments",
  "new_payments_amount": "sum of the amount of each payment"
}
```

4. Add security for the disbursement process endpoint.

## Notes:

- The expected minimum is that you complete steps 1,2,3. 4 is optional  (Completing all is an extra).
- We want to see your skills and abilities to code so if at any moment you want to change or refactor anything go ahead.
- We are considering as reviewers that your code challenge is code-prod-quality, and it will review under this
  impression.
- Please initialize the directory with the challenge as a git repo so you can commit new features, and we can check on
  your thought process.
- Please upload the code-challenge to a git-repository and share the access with the reviewers that the recruitment team
  indicates. 