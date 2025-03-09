# 🏦 CaixaBank Java Backend Challenge - Real Estate Auctionator 🏡

**Category:** Software  
**Subcategory:** Java Backend  
**Difficulty:** Medium  

---

## 🌐 Background

This challenge simulates a real estate auction system for CaixaBank. The backend is built in Java and offers several functionalities, including:  
- User authentication and registration using JWT.  
- Extraction (scraping) of property data directly from Idealista.  
- An automatic mortgage application system based on user financial data.  
- An auction system for properties that uses RabbitMQ to manage concurrent bids.

The goal is to evaluate your skills in secure API development, external data extraction, business rule implementation, and asynchronous message processing using queues.

In addition to the README, you must read and follow this file: [RealEstate_App_info](https://cdn.nuwe.io/challenge-asset-files/CB-Round3/RealEstate_App_Info.pdf)
---

## 📂 Repository Structure

Basic structure of the repository to be taken as a reference, but may vary depending on the proposed solution.

```bash
caixabank-backend-java-realestate
├── src
│   └──main
│       ├── java
│       │   └── com
│       │       └── round3
│       │           └── realestate
│       │               ├── config           # Spring Security, RabbitMQ, and other configurations
│       │               ├── controller       # REST API endpoints
│       │               ├── entity           # JPA entities
│       │               ├── messaging        # Message publishing/consuming classes (BidMessage, BidPublisher, etc.)
│       │               ├── payload          # Request and response payload classes
│       │               ├── repository       # Spring Data repositories
│       │               └── security         # JWT and UserDetails implementations
│       └── resources
│           ├── application.properties         # Application configuration
│           └── ...
├── Dockerfile                                 # Docker build instructions
├── docker-compose.yml
├── README.md
└── ...
```

---

## 🎯 Tasks

1. **Task 1: Dockerfile & Health Check**  
2. **Task 2: Login and register with JWT**  
3. **Task 3: Obtaining property data directly from Idealista**  
4. **Task 4: Automatic mortgage application system**
5. **Task 5: Bidding system for a property**  

**Please read the entire README carefully, as well as the detailed guides to fully understand the requirements of the challenge.**

---

## 📑 Detailed Information About Tasks

### Task 1: Dockerfile & Health Check

This task is only intended to ensure that the correction is being executed correctly.

A health check endpoint is provided and a test is passed to validate that the API is reachable, which ensures that the container with the app has been deployed correctly and the connection to the database has been made.

Before doing the first push, you should make sure that the app works correctly, as all other tasks will be tested by attacking the endpoint generated by this container on port 3000. You can do this by executing:

```bash
docker-compose up -d
```

---

### Task 2: Login and register with JWT
**Objective:**  
Implement secure user registration and login endpoints using JSON Web Tokens (JWT) for authentication.

**Detailed Requirements:**

- **Registration (`POST /api/auth/register`):**  
  - Accept user details such as username, email, and password.
  - Validate that the email is unique.
  - Encrypt the password (e.g., using BCrypt) before storing it in the database.
  - Set new users to `unemployed` by default.
  - Return a success message once the user is registered.

- **Login (`POST /api/auth/login`):**  
  - Accept user credentials (username/email and password).
  - Validate the credentials against the stored data.
  - If authentication is successful, generate a JWT that includes at least the user ID as the subject.
  - The JWT should have an expiration time defined in the application properties.
  - Return the JWT in the response. For example: `{ "success": true, "token": "<JWT_TOKEN>" }`.

- **Session check (`GET /api/user/me`)**:
  - Returns the user id, username and email.
  - Used to verify that the tokens are working correctly.
  ```json
  { "id": 1, "username": "nuwe", "email": "nuwe@example.com" }
  ```

- **Security Considerations:**  
  - Ensure that the JWT is used to protect all endpoints that require authentication.
  - Implement a JWT filter that validates incoming tokens and loads user details accordingly.

---

### Task 3: Obtaining property data from Idealista

**Objective:**  
Implement a web scraping endpoint that extracts property details from Idealista based on a provided URL.

**Detailed Requirements:**

- **Endpoint (`POST /api/scrape`):**  
  - Accept a JSON payload with:
    - `"url"`: The URL of the property on Idealista.
    - `"store"`: A Boolean flag indicating whether the scraped property should be saved in the database.
  
- **Data Extraction:**  
  - Use a library like Jsoup to connect to the provided URL.
  - Extract the following details:
    - **Property Type:** Extracted from the title as the first word (e.g., "Chalet", "Apartamento"). Stored as "name" in the database.
    - **Full Title:** The complete title of the property as it appears on the page.
    - **Location:** The property’s location (could be a street name, neighborhood, or city).
    - **Price:** The price, ensuring it is converted to a standard numerical format(Decimal).
    - **Size:** The area of the property (in m²).
    - **Rooms:** The number of rooms, extracted from text like "4 hab.".
  - If the data for any of these fields is not available, handle it gracefully (e.g., set to an empty string or a default value).

- **Storage Behavior:**  
  - If `"store"` is `true`, create a new property record in the database with the extracted data.
  - Set the property’s `availability` field to `"Available"` by default.
  
- **Response:**  
  - Return a JSON object that includes the extracted data and a flag indicating whether the property was saved.

---

### Task 4: Automatic mortgage application system

**Objective:**  
Implement a mortgage application system that evaluates if an authenticated user qualifies for a mortgage based on their financial information.

**Detailed Requirements:**

- **Employment Data:**  
  - Create an endpoint (`POST /api/employment`) where authenticated users can submit or update their employment data, including:
    - **Contract Type:** Values like "indefinite", "temporary", or `NULL` (default at registration).
    - **Salary:** The gross annual salary.
  - On submission, automatically calculate the net monthly salary using Spanish tax brackets:
    - 0 – 12,450€: 19% retention  
    - 12,450 – 20,199€: 24% retention  
    - 20,200 – 35,199€: 30% retention  
    - 35,200 – 59,999€: 37% retention  
    - 60,000 – 299,999€: 45% retention  
    - Above 299,999€: 50% retention (new bracket as per requirement)
  - Based on the presence of employment data, update the user’s employment status (e.g., from "unemployed" to "employed").

- **Mortgage Request:**  
  - Implement an endpoint (`POST /api/mortgage`) that allows an authenticated user to request a mortgage.
  - The system should:
    - Validate whether the user has a job. If not, returns an error.
    - Retrieve the selected property’s price from the database.
    - Calculate the total property cost as the property price plus 15% extras (to cover VAT and additional fees).
    - Use the user’s net monthly salary (calculated previously) and their contract type to set an allowed payment threshold (30% for indefinite contracts; 15% for temporary contracts).
    - Calculate the monthly payment using a fixed annual interest rate of 2% and the provided term (minimum 15 years). Use the standard amortization formula:
      - Payment = P * r / (1 - (1 + r)^(-n))
      - Where P = total cost, r = monthly interest rate, n = total number of months.
    - Determine whether the calculated monthly payment is within the allowed percentage of the user’s net monthly salary.
    - If the monthly payment exceeds the threshold, reject the application with an appropriate error message; otherwise, approve it.
    - If approved, save the mortgage details in the database and associate the mortgage with the user and the property.

- **User dashboard:**
  - Implement an endpoint (`GET /api/user/dashboard`) that allows an authenticated user to request personal data.
  - It must show employment data and mortgages.

- **Response:**  
  - Return a JSON response indicating whether the mortgage was approved, the calculated monthly payment, and (if approved) the mortgage ID.

---

### Task 5: Bidding system for a property

**Objective:**  
Implement an auction system where properties can be bid upon using RabbitMQ to manage concurrent bids.

**Detailed Requirements:**

- **Auction Creation:**  
  - Implement an endpoint (`POST /api/auction/create`) that creates a new auction for a property.  
  - The auction must include:
    - Property reference (must exist in the database).
    - Start and end times (ISO 8601 formatted).
    - Starting price.
    - Minimum bid increment.
    - Initial current highest bid set to the starting price.
  
- **Placing Bids:**  
  - Implement an endpoint (`POST /api/auction/{auctionId}/bid`) where authenticated users can place bids.
  - When a bid is placed:
    - Verify that the auction exists and is still open.
    - Create a bid message with the auction ID, user ID, bid amount, and timestamp.
    - Publish the bid message to a RabbitMQ queue.
    - The bid message is then asynchronously processed by a consumer that updates the auction's current highest bid if applicable.
  
- **Auction Details:**  
  - Implement an endpoint (`GET /api/auction/{auctionId}`) to retrieve auction details, including all submitted bids.
  
- **Closing Auctions:**  
  - Implement an endpoint (`PATCH /api/auction/{auctionId}/close`) that:
    - Closes the auction by setting its status to "closed".
    - Processes all bids to determine the winning bid (the highest bid).
    - Updates the property’s availability to "Unavailable".
    - Returns a JSON response containing the winning bid amount and the winning user's ID.

**Concurrency & RabbitMQ:**  
- The bidding system uses RabbitMQ to handle high volumes of bids concurrently.  
- Bids are enqueued and then processed asynchronously by a message listener, ensuring that race conditions are minimized.
- You may implement a delay in the consumer (for testing purposes) to observe how messages accumulate in the queue.
- Names for RabbitMQ configuration:
  - `bid.queue`
  - `bid.exchange`
  - `bid.routingkey`

---

## 💫 Guides

### 📋 Endpoints Table

| **Endpoint**                           | **Method** | **Auth Required** | **Possible Status Codes**                | **Response Example**                                                                                                                                                                                                                                                                                                                                                                                           |
|----------------------------------------|------------|-------------------|------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `/api/health`                          | GET        | No                | 200 OK         | `"API is working"`                                                                                                                                                                                                                                                                                                                                                                                           |
| `/api/auth/register`                   | POST       | No                | 200 OK, 400 Bad Request                  | `{ "success": true, "message": "User successfully registered" }`                                                                                                                                                                                                                                                                                                                                             |
| `/api/auth/login`                      | POST       | No                | 200 OK, 401 Unauthorized                 | `{ "accessToken": "eyJhbGciOiJIUzUxMiJ9...", "tokenType": "Bearer" }`                                                                                                                                                                                                                                                                                                                                         |
| `/api/user/me`                         | GET        | Yes               | 200 OK, 401 Unauthorized                 | `{ "id": 1, "username": "nuwe", "email": "nuwe@example.com" }`                                                                                                                                                                                                                                                                                                                                               |
| `/api/user/dashboard`                  | GET        | Yes               | 200 OK, 401 Unauthorized                 | `{ "employmentData": { ... }, "mortgages": [ ... ] }`                                                                                                                                                                                                                                                                                                                                                         |
| `/api/scrape`                          | POST       | No                | 200 OK, 500 Internal Server Error        | `{ "data": { "type": "Casa", "fullTitle": "Casa o chalet independiente en venta en Las Rozas", "location": "Campoo-Los Valles, Cantabria", "price": "680.000", "size": "361 m²", "rooms": "9 hab.", "url": "https://www.idealista.com/inmueble/104498284/" }, "saved": true }` (or `"saved": false` if not stored)                                                                       |
| `/api/employment`                      | POST       | Yes               | 200 OK, 400 Bad Request                  | `{ "employmentData": { "id": 1, "contract": "indefinite", "salary": 1000000, "netMonthly": 43674.7, "employmentStatus": "employed" }, "message": "Employment data updated successfully" }`                                                                                                                                                                                                       |
| `/api/employment`                      | PATCH      | Yes               | 200 OK, 400 Bad Request                  | `{ "employmentData": { "id": 2, "contract": "temporary", "salary": 50000, "netMonthly": 2983.09, "employmentStatus": "employed" }, "message": "Employment data updated successfully.", "success": true }`                                                                                                                                                                                               |
| `/api/mortgage`                        | POST       | Yes               | 200 OK, 400 Bad Request                  | `{ "approved": true, "mortgageId": 1, "netMonthly": 43674.7, "monthlyPayment": 3956.01, "allowedPercentage": "30.0%", "message": "Mortgage approved.", "numberOfMonths": 240 }` <br> *(Alternatively, if rejected, return a JSON with `"approved": false` and an appropriate error message.)*                                                                                     |
| `/api/auction/create`                  | POST       | No                | 200 OK, 400 Bad Request                  | `{ "success": true, "auctionId": 1, "message": "Auction created successfully." }`                                                                                                                                                                                                                                                                                                                            |
| `/api/auction/{auctionId}`             | GET        | No                | 200 OK, 400 Bad Request                  | `{ "auction": { "id": 1, "property": { "id": 1, "name": "Casa", "location": "Campoo-Los Valles, Cantabria", "price": 680000, "size": "361 m²", "rooms": "9 hab.", "availability": "Unavailable" }, "startTime": "2025-03-07T10:00:00", "endTime": "2025-03-07T11:00:00", "status": "open", "minIncrement": 1000, "startingPrice": 680000, "currentHighestBid": 680000, "bids": [ ... ] } }` |
| `/api/auction/{auctionId}/bid`         | POST       | Yes               | 200 OK, 400 Bad Request, 401 Unauthorized  | `{ "success": true, "message": "Bid submitted successfully." }` <br> *(Errors: e.g., bid too low, auction closed, or authentication errors.)*                                                                                                                                                                                              |
| `/api/auction/{auctionId}/close`       | PATCH      | No                | 200 OK, 400 Bad Request                  | `{ "success": true, "message": "Auction closed successfully.", "winningBid": 510000, "winningUserId": 1 }`                                                                                                                                                                                                                                   |

---

### More information

The [application.properties](src/main/resources/application.properties) file contains the configuration necessary for the correct functioning of the application. 

**The backend tests will simulate the interaction of a user directly with the API running in a container and exposed on port 3000**

---

## 📤 Submission

1. Solve the proposed tasks.
2. Continuously push the changes you have made.
3. Wait for the results.
4. Click submit challenge when you have reached your maximum score.

## 📊 Evaluation

The final score will be given according to whether or not the objectives have been met.

In this case, the challenge will be evaluated on 1450 (1050 for backend tasks and 400 for code quality) points which are distributed as follows:

### Backend

- **Task 1**: 50 points
- **Task 2**: 200 points
- **Task 3**: 250 points
- **Task 4**: 250 points
- **Task 5**: 300 points
- **Code quality**: 400 points

## ❓ Additional information

**Q1: Can I change anything in the app?**

A1: Yes, as it is a hackathon and the application is dockerised, you are free to modify anything within the project, except for the functions that are already predefined and files like: `docker-compose.yml` and `application.properties`.
If you modify `application.properties`, be sure to return it to its original state before pushing the project.
The `Dockerfile` is given configured to be fully functional.

**Q2: Can I add resources that are not in pom.xml?**

A2: Yes, you can add new resources if necessary, but keep in mind that everything needed to develop it has already been added.

**Q3: Is it completely necessary to do the Dockerfile configuration first?**

A3: Yes. To ensure the integrity of the correction, a Dockerised environment is the safest way to go. The `Dockerfile` is given configured to be fully functional so it is only necessary to push the code and check that the score appears in the first task.

**Q4: I scored 0 for task 1, what's wrong?**

A4: There is probably a misconfiguration in one of the following files:
- `Dockerfile`
- `application.properties`
It is also possible that your code does not compile and the app container does not deploy and the API connection fails.

**Q5: What is the latest version of README?**

A5: The most recent version will always be the one that appears on the platform. In case there is something to correct in the readme, you can see the updated version on the NUWE website.