# Student Management System

A simple **Java-based Student Management System** with a **basic web frontend** for login and separate dashboards for students and teachers.

## 📌 Features

* **Login Panel** with role selection (Student/Teacher)
* **Credential Validation** (username + password)
* **Role-based Redirection**

  * Student → StudentPanel.html (shows student details)
  * Teacher → TeacherPanel.html (teacher dashboard)
* **Backend REST API** with `/api/login`
* **Session Storage** for saving user state
* Clean and minimal **CSS Styling**

## 🏗️ Tech Stack

* **Backend:** Java, Spring Boot (or Servlet-based backend)
* **Frontend:** HTML, CSS, JavaScript (Vanilla)
* **Build Tool:** Maven

## 🚀 Setup Instructions

1. **Clone the Repository**

   ```bash
   git clone https://github.com/your-username/student-management-system.git
   cd student-management-system
   ```

2. **Build the Project**

   ```bash
   mvn clean install
   ```

3. **Run the Backend**

   ```bash
   mvn spring-boot:run
   ```

   (or deploy the WAR on Tomcat if using Servlets)

4. **Open Frontend**

   * Open `Login.html` in a browser.
   * Enter username, password, and select role.
   * On success → redirected to the correct dashboard.

## 📂 Project Structure

```
student-management-system/
├── src/
│   ├── main/
│   │   ├── java/        # Backend Java code (controllers, services)
│   │   ├── resources/   # application.properties
│   │   └── webapp/      # HTML, CSS, JS files
├── pom.xml              # Maven dependencies
└── README.md            # This file
```

## 🔑 API Details

* **POST /api/login**

  * Request:

    ```json
    {
      "username": "abc",
      "password": "123"
    }
    ```
  * Response (on success):

    ```json
    {
      "status": "success",
      "role": "STUDENT"
    }
    ```
  * Response (on failure):

    ```json
    {
      "status": "error",
      "message": "Invalid credentials"
    }
    ```

## 🎨 Frontend Sample (Login.html)

```html
<select id="role">
  <option value="STUDENT">Student</option>
  <option value="TEACHER">Teacher</option>
</select>
<input type="text" id="username" placeholder="Enter username">
<input type="password" id="password" placeholder="Enter password">
<button onclick="login()">Login</button>
<p id="message"></p>
```

## ✅ Future Improvements

* Add **password hashing**
* Add **role-based authorization** (server-side)
* Add **database integration** (MySQL/PostgreSQL)
* Improve **UI/UX** with better CSS and JS validation
* Add **Logout functionality**

---

Made with ❤️ for learning Java + Web Development.
