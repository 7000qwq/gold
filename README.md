# Gold Trading Record and Price Monitoring Website

## Project Overview
This project is a web application designed, developed, and deployed to support real-time monitoring of gold prices. The application provides functionalities to set price fluctuation alerts, record historical transactions, and manage user authentication, ensuring a seamless and secure experience for users involved in gold trading.

**Project URL:** [https://www.qwq7000.top](https://www.qwq7000.top)  
**GitHub Repository:** [https://github.com/7000qwq/gold](https://github.com/7000qwq/gold)

## Tech Stack
- **Backend:** SpringBoot (Java)
- **Database:** MySQL
- **Caching:** Redis
- **Messaging:** RabbitMQ
- **Real-Time Communication:** WebSocket
- **Deployment Tools:** Nginx, Docker, Raspberry Pi

## Key Features
### Identity Verification
- **JWT-Based Authentication:**
  - JWT tokens are generated during user login, utilizing ThreadLocal to store session information for secure and efficient operations.
  - User registration includes email-based identity verification to enhance system security and user experience.

### Real-Time Gold Price Retrieval and Status Updates
- Utilized Spring Task Scheduler for periodic retrieval of the latest gold prices.
- Employed HttpClient and Jsoup to fetch data from external sources.
- Leveraged RabbitMQ and WebSocket to push real-time data to the frontend for seamless updates and chart displays.

### Asynchronous Message Handling
- Implemented asynchronous email sending using RabbitMQ, reducing frontend waiting time.
- Decoupled backend modules via RabbitMQ to enhance message delivery reliability and improve system maintainability.

### Caching Mechanism
- Used Redis to store user-defined price alert strategies, significantly improving data access speed and overall system responsiveness.

### Project Deployment
- Configured Nginx for reverse proxying and deployed the application on a Raspberry Pi using Docker.
- Incorporated internal network penetration techniques and HTTPS tunnel mapping to enable public access through a personal domain.

## How It Works
1. **User Registration and Login:**
   - Users register with an email address and receive a verification link to complete the process.
   - Login sessions are managed using JWT tokens.

2. **Real-Time Price Monitoring:**
   - The backend periodically fetches the latest gold prices and updates the database.
   - Price updates are sent to the frontend in real-time using WebSocket.

3. **Price Alerts:**
   - Users can set high and low threshold prices for alerts.
   - The system sends email notifications when the latest price meets the defined thresholds.

4. **Historical Data Management:**
   - The application records and displays historical transaction data for user analysis.

5. **Scalability and Performance:**
   - Redis caching enhances data access speed.
   - RabbitMQ ensures efficient and reliable messaging between components.

## Deployment Instructions
1. **Setup Environment:**
   - Install Docker and Docker Compose.
   - Configure Nginx for reverse proxying.

2. **Database Configuration:**
   - Set up MySQL and Redis instances with appropriate configurations.

3. **Backend Deployment:**
   - Clone the repository from GitHub.
   - Build and run the SpringBoot application using Docker.

4. **Domain Configuration:**
   - Use an HTTPS tunneling service to map the Raspberry Pi’s internal network to a public domain.

## Future Enhancements
- Add support for additional precious metal price monitoring.
- Introduce user role-based access control for enhanced security.
- Implement data visualization dashboards for better analytical insights.

---

For any questions or contributions, please visit the [GitHub Repository](https://github.com/7000qwq/gold) or contact the project maintainer through the website.

