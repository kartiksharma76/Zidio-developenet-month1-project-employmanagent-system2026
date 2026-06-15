# Development Challenges & Resolutions

During the 28-day development lifecycle of the **NexusHR Enterprise** application, several technical and architectural challenges were encountered. This document outlines those challenges and how they were resolved.

---

## 1. Authentication Strategy: JWT vs. Session Management
**Challenge:** 
Initially, the backend was designed with JWT (JSON Web Tokens) for a stateless REST API. However, as the project shifted towards a Server-Side Rendered (SSR) approach using Thymeleaf, handling JWTs in browser cookies or local storage securely became cumbersome for simple page navigation.

**Resolution:**
We pivoted to Spring Security's native **Session-Based Authentication** (`formLogin`). We disabled the custom `JwtAuthenticationFilter` and allowed Spring to manage the `SecurityContext` via standard HTTP Sessions. This made Thymeleaf's `sec:authorize` tags work flawlessly without complex client-side token management.

## 2. LazyInitializationException in Hibernate
**Challenge:**
When fetching `Employee` records (for example, in the Leave or Performance modules), accessing the related `UserAuth` entity resulted in a `LazyInitializationException`. This happened because the JPA session was closed before the view (Thymeleaf) attempted to render the lazy-loaded fields.

**Resolution:**
Instead of globally enabling `spring.jpa.open-in-view=true` (which is an anti-pattern and causes performance issues), we utilized **`@EntityGraph`** in the Spring Data JPA repositories. This allowed us to explicitly instruct Hibernate to eagerly fetch the `UserAuth` relationship in a single SQL `JOIN` query when required, optimizing performance and fixing the error.

## 3. Role-Based Access Control (RBAC) UI Rendering
**Challenge:**
Implementing a single `dashboard.html` that caters to Admins, Managers, and Employees without creating massive code duplication or exposing sensitive data to the wrong roles. Furthermore, the controllers needed to restrict data access securely.

**Resolution:**
We extracted role-checking logic in the controllers to generate simple boolean flags (`isAdmin`, `isManager`, `isAdminOrManager`). 
- On the **Frontend**, we used Thymeleaf's `th:if="${isAdminOrManager}"` to conditionally render table columns (like the "Actions" column for Leave Approvals) and dashboard widgets.
- On the **Backend**, we implemented strict `if/else` checks. If an Employee attempts to fetch leaves, the repository only queries `findByEmployee()`. If an Admin/Manager queries it, it uses `findAll()`.

## 4. Real-Time Data Streaming for the Dashboard Graph
**Challenge:**
The client requested a "Real-time" company graph. Using WebSockets (STOMP/SockJS) for a simple dashboard chart felt over-engineered and would complicate the monolithic architecture.

**Resolution:**
We implemented a **Short-Polling mechanism**. We created a dedicated lightweight REST endpoint (`ApiDashboardController`) that aggregates department counts and leave statistics. On the frontend, `Chart.js` is updated seamlessly using a Javascript `setInterval` function that fetches this JSON endpoint every 5 seconds and calls `chart.update()`. This provided the "live" feel without the overhead of WebSockets.

## 5. Making the AI Chatbot Context-Aware
**Challenge:**
Connecting the application to the NVIDIA Llama-3.1 API was straightforward, but the AI had no knowledge of the actual company data, rendering its answers generic and unhelpful for a proprietary HR system.

**Resolution:**
We implemented **Dynamic System Prompt Injection**. Before calling the LLM via `RestTemplate`, the backend queries the database for critical real-time stats (Total Employees, Pending Leaves, etc.). This data is injected into the hidden `system` prompt array of the LLM request. As a result, the AI acts as if it is directly plugged into the HR database, providing accurate and contextual answers to the user's questions.
