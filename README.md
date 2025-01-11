🌟 Annapurna Hotel Booking System
📄 Overview
The Annapurna Hotel Booking System is a web application developed as a final year project (FYP) by Sawan Gurung. The system enables users to book rooms at Annapurna Hotel with secure login, room management, and email notifications.

It is built using Spring Boot for backend development and integrates with a MySQL database to store data about users, rooms, bookings, and more.

🏷️ Features
User Registration and Login: Users can create accounts and securely log in to the system.
Room Booking: Users can easily book available rooms based on their preferences.
JWT Authentication: Secure login and authentication using JSON Web Tokens (JWT).
Email Notification: Booking confirmations are sent via email.
Admin Panel: Admin users can manage rooms, view bookings, and check system health.
Swagger API Documentation: API documentation is automatically generated using Swagger.
⚙️ Tech Stack
Backend: Spring Boot 3.1.4
Database: MySQL 8.0.32
Frontend: API-driven (can be a separate frontend, e.g., React.js)
Security: Spring Security with JWT Authentication
Mailing: Spring Boot Starter Mail
API Documentation: Swagger (Springdoc OpenAPI)
Validation: Hibernate Validator
WebSockets: For potential future real-time communication features
Development Tools: Lombok for reducing boilerplate code
🛠️ Getting Started
Prerequisites
Ensure you have the following installed:

JDK 17 or later
MySQL 8.0.32 or later
Maven 3.6 or later
Setup
Clone the repository:

bash
Copy code
git clone https://github.com/your-username/annapurna-hotel-booking.git
Navigate to the project directory:

bash
Copy code
cd annapurna-hotel-booking
Set up MySQL database and configure the database connection in application.properties:

properties
Copy code
spring.datasource.url=jdbc:mysql://localhost:3306/hotel_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
Build the project:

bash
Copy code
mvn clean install
Run the application:

bash
Copy code
mvn spring-boot:run
🖥️ API Endpoints
GET /api/rooms – Get all available rooms
POST /api/rooms/book – Book a room (requires authentication)
GET /api/users/profile – Get user profile details (requires authentication)
POST /api/users/login – Login and get a JWT token
GET /actuator/health – Health check endpoint (for monitoring)
📦 Dependencies
Spring Boot: Main framework for backend development.
Spring Security: Handles user authentication and authorization.
Spring Data JPA: Manages database interactions.
MySQL: Database for storing user and booking data.
Spring Boot Starter Mail: For sending booking confirmation emails.
Swagger/OpenAPI: Automatic API documentation generation.
Spring Boot Devtools: Provides easier development and automatic restarts.
JWT: Secure authentication and session management.
🧪 Testing
Run unit and integration tests using Maven:

bash
Copy code
mvn test
📧 Mail Configuration
To enable email notifications, configure your email server settings in application.properties:

properties
Copy code
spring.mail.host=smtp.example.com
spring.mail.port=587
spring.mail.username=your-email@example.com
spring.mail.password=your-email-password
🔧 Optional Features
JWT Authentication: Secure authentication already implemented, customizable for your needs.
WebSocket: Potential for real-time features like chat or notifications (to be added in future updates).
🚀 Future Enhancements
Mobile app integration for easier access and room booking.
Real-time availability updates using WebSockets.
Payment gateway integration for booking payments.
👨‍💻 Authors
Sawan Gurung – Final Year Project Developer

📜 License
This project is licensed under the MIT License.