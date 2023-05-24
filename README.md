# Bank Account API

This API allows users to manage their bank accounts and perform common operations such as deposit, withdrawal, and balance inquiry.

## Technologies Used

- Java 
- Spring Boot
- Maven

## Installation

1. Clone the project from the Git repository:

```
git clone https://github.com/Ali-chami/bankAccount-kata.git
```

2. Navigate to the project directory:

```
cd bankaccount
```

3. Build the project with Maven:

```
mvn clean install
```

4. Run the application:

```
mvn spring-boot:run
```

The API will be accessible at the following URL: `http://localhost:8080`

## Endpoints

- `POST /accounts`: Create a new bank account.
- `POST /accounts/{accountId}/deposit`: Perform a deposit to the specified account.
- `POST /accounts/{accountId}/withdrawal`: Perform a withdrawal from the specified account.
- `GET /accounts/{accountId}/balance`: Retrieve the balance of the specified account.
- `GET /accounts/{accountId}/transactions`: Retrieve the transactions made on the specified account.

## API Documentation

The Bank Account Application provides an OpenAPI documentation for the API endpoints. You can access the API documentation using the following URL:

- OpenAPI UI: `http://localhost:8080/swagger-ui.html`

The OpenAPI UI allows you to explore the available endpoints, view request/response examples, and interact with the API.

## Usage

1. Create an account:
    - Endpoint: `POST /accounts`
    - Request Body: None
    - Response: Account object

2. Make a deposit:
    - Endpoint: `POST /accounts/{accountId}/deposit`
    - Request Body: Amount
    - Response: None

3. Make a withdrawal:
    - Endpoint: `POST /accounts/{accountId}/withdrawal`
    - Request Body: Amount
    - Response: None

4. Get account balance:
    - Endpoint: `GET /accounts/{accountId}/balance`
    - Response: Account balance

5. Get account transactions:
    - Endpoint: `GET /accounts/{accountId}/transactions`
    - Response: List of transactions
    
## Project Structure

- `com.societegenerale.bankaccount.api`: Contains the controller classes exposing the API endpoints.
- `com.societegenerale.bankaccount.application`: Contains the application layer classes that handle the business logic.
- `com.societegenerale.bankaccount.domain`: Contains the domain model classes, such as Account and Transaction entities.
- `com.societegenerale.bankaccount.infrastructure`: Contains the infrastructure classes, such as repository implementations or utility classes.

## Contributing

Contributions to this project are welcome. If you want to make improvements, fix bugs, or add new features, feel free to submit a pull request.

Make sure to follow the best practices of software development and test your code appropriately before submitting a merge request.
