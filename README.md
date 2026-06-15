# NexusHR – AI Enabled Enterprise HR & Workforce Intelligence Platform

This project is a modern Full-Stack HR Management system that includes Authentication, Attendance, Leave Management, Payroll, Performance Reviews, AI predictions, and Real-Time Notifications.

## Technology Stack
- **Backend**: Java 17, Spring Boot 3, Spring Security (JWT), MySQL
- **Frontend**: React, TypeScript, Vite, Tailwind CSS
- **Features**: Authentication, WebSockets, AI Integration (Mocked), Role-based Access.

---

## Local Setup Instructions

### 1. Backend Setup
1. Ensure you have **MySQL** installed and running on port `3306`.
2. Create a database named `comp_emp_management`.
    ```sql
    CREATE DATABASE comp_emp_management;
    ```
3. Update the credentials in `Comp_Emp_Manage/src/main/resources/application.properties` if your MySQL password is not `Kartik@2005`.
4. Run the Spring Boot application using your IDE or Maven:
    ```bash
    cd Comp_Emp_Manage
    mvn spring-boot:run
    ```
5. The backend will start on `http://localhost:6161`.

### 2. Frontend Setup
1. Open a new terminal and navigate to the frontend folder.
    ```bash
    cd frontend
    ```
2. Install dependencies:
    ```bash
    npm install
    ```
3. Start the Vite development server:
    ```bash
    npm run dev
    ```
4. The frontend will start on `http://localhost:5173`. Open this URL in your browser.

---

## Project Structure (Weeks 1 - 3 Completed)
- **Authentication**: Secure Signup and Login using JWT and Argon2 hashing.
- **Attendance & Leaves**: Full APIs to check-in/out and apply for leaves.
- **Payroll & Performance**: Salary generation and rating module.
- **AI Analytics**: Employee Attrition prediction endpoints (`/api/ai`).
- **Live Notifications**: WebSockets via STOMP and mock Email/SMS.
- **Admin Dashboard**: Frontend visualizations and Dark mode support.

> **Note on Deployment**: The Week 4 containerization step was skipped per request, keeping the project streamlined for local execution.
