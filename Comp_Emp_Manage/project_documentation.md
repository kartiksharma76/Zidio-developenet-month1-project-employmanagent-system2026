# NexusHR Enterprise - 28 Days Development Journey

This document serves as the comprehensive timeline and documentation for the development of the **Comprehensive Employee Management System (NexusHR)** over a 28-day sprint.

## Project Overview
NexusHR is a full-stack, enterprise-grade Employee Management System built using **Java, Spring Boot, Spring Security, MySQL, Thymeleaf, and Bootstrap**. It features Role-Based Access Control, AI-powered HR Insights, and core HR modules like Attendance, Leave, Payroll, and Performance Management.

---

## 🗓️ Phase 1: Foundation & Authentication (Days 1 - 7)
**Goal:** Set up the core architecture, database, and secure user access.

- **Day 1-2:** Initialized the Spring Boot project. Configured the `application.properties` to connect to the MySQL database.
- **Day 3-4:** Created the foundational Entity models: `Employee` and `UserAuth` with a One-to-One mapping. Designed the Repositories using Spring Data JPA.
- **Day 5-6:** Configured **Spring Security**. Implemented password encoding (`BCryptPasswordEncoder`) and set up custom UserDetailsService.
- **Day 7:** Developed the Frontend templates for Login and Registration using Thymeleaf and Bootstrap. Users can now securely register and log into the system.

## 🗓️ Phase 2: Dashboards & Role-Based Access (Days 8 - 14)
**Goal:** Build the user interface and restrict access based on roles.

- **Day 8-9:** Created the `WebDashboardController` and the base `layout.html` with a dynamic sidebar navigation.
- **Day 10-11:** Implemented **Role-Based Access Control (RBAC)**. Added dropdowns in registration for `ROLE_ADMIN`, `ROLE_MANAGER`, and `ROLE_EMPLOYEE`.
- **Day 12-13:** Segregated the Dashboard (`dashboard.html`). Admins see global metrics, Managers see team overviews, and Employees see personal stats.
- **Day 14:** Built the **Employee Directory** module, allowing users to view and search for colleagues across departments.

## 🗓️ Phase 3: Core HR Modules (Days 15 - 21)
**Goal:** Implement daily operational features for employees.

- **Day 15-16:** Developed the **Attendance Module**. Created the UI and backend logic for "Punch In" and "Punch Out" tracking timestamps.
- **Day 17-18:** Developed the **Leave Management Module**. Employees can apply for Sick, Casual, or Earned leaves.
- **Day 19-20:** Upgraded the Leave Module with Manager/Admin capabilities. Added the `Approve` (✅) and `Reject` (❌) functionality in the `WebLeaveController`.
- **Day 21:** Integrated the Attendance Quick Actions directly onto the Employee Dashboard for easier access.

## 🗓️ Phase 4: Advanced Features & AI Integration (Days 22 - 28)
**Goal:** Add Performance, Payroll, Real-time analytics, and AI.

- **Day 22-23:** Developed the **Performance Review Module**. Managers can submit 1-5 star ratings and feedback for employees.
- **Day 24-25:** Developed the **Payroll Module**. Admins can generate monthly payslips, and employees can view their salary history.
- **Day 26:** Implemented the **Real-Time Company Graph**. Integrated `Chart.js` on the Admin dashboard. Created a REST API (`/api/dashboard/stats`) that polls every 5 seconds to show live department distributions.
- **Day 27-28:** Built the **NexusHR AI Chatbot**. Connected the application to the **NVIDIA Llama-3.1 API**. The AI was injected with real-time database context (total employees, pending leaves) to answer HR queries intelligently.

---

## Technical Stack Used
- **Backend:** Java 17/21, Spring Boot 3.x, Spring Web, Spring Security, Spring Data JPA
- **Database:** MySQL 8
- **Frontend:** HTML5, Thymeleaf, Bootstrap 5, Chart.js, FontAwesome
- **AI Integration:** NVIDIA API (meta/llama-3.1-70b-instruct) via REST Client

