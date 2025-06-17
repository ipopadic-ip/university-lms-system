# University Learning Management System

This is a full-stack web application developed using **Spring Boot** for the backend and **Angular** for the frontend. The system serves as a **Learning Management System (LMS)** for a university and functions as its official public website.

## 🎥 Demo Video

👉 [Click here to watch the demo video](https://youtu.be/zDmOwWpcnQM?si=o7hUJuqtPl0MFQIm)

---

## 🔐 Authentication & Authorization

- Authentication is implemented using **JWT (JSON Web Token)**.
- **Role-based access control** for:
  - Unregistered users
  - Students
  - Teachers
  - Student Services Staff
  - Commercialist
  - Administrators
- **Postman** was used for backend API testing.

## 🧰 Technologies Used

### Backend
- Spring Boot 3.4.4
- Java 24
- Spring Security + JWT (`io.jsonwebtoken`)
- MySQL Server (managed via MySQL Workbench)
- Apache Jena TDB (for RDF storage)
- Hibernate + JPA
- Lombok
- PDF & XML export/import
- iText PDF generator
- JAXB for XML (import/export)
- WebSocket + Spring Security Messaging
- Jackson for JSON binding

### Frontend
- Angular
- Bootstrap for Angular
- RxJS
- Angular Forms & Routing
- Angular HTTP Client

### Real-time Communication
- **WebSocket** used for collaborative document editing.
- Email is extracted from the JWT token (not passed manually).
- Real-time updates are visible to all users editing the document simultaneously.
- Shows who is currently editing the document.

---

## 🌐 Application Features

### Unregistered Users
- View public university website
  - Contact info, location, description, rector info
- View faculty pages
  - Contact info, location, description, dean info
- View study programs
  - List of courses, description, head of program
- View course syllabi
- View teaching materials
- Register an account

### Registered Users
- Login to the system
- Edit personal profile
- Logout

### Students
- View currently enrolled subjects
- View subject announcements
- View study history:
  - Passed courses
  - Number of exam attempts
  - Final scores and grades
  - GPA and total earned ECTS
- Register for exams (if registration is open)

### Teachers
- View subjects they are assigned to
- Edit syllabi for assigned courses
- Schedule topics/outcomes per lecture
- Define evaluation instruments (projects, tests, quizzes)
- Manage course announcements
- View enrolled students per course
- Search students by:
  - First name, last name, index number, enrollment year, GPA, etc.
- View detailed student information:
  - Basic info
  - GPA and total ECTS
  - Enrollment records
  - Passed/failed exams with grades and points
  - Disciplinary reports
  - Registered exams
  - Final thesis
- Submit grades for their courses (within 15 days of exam date)

### Student Services Staff
- Enroll students for an academic year
- Issue certificates and documents
- Create class/exam schedules
- Publish general announcements
- Issue or request textbooks from the university library
- Request office supplies

### Commercialist
- View all textbook and office supply requests submitted by student services staff  
- Order textbooks and office supplies based on submitted requisitions  
- Update inventory and availability tables once materials have been received  
- Ensure that ordered items are properly recorded and made available to staff and students

### Administrators
- Manage dictionaries and codebooks
- Manage registered users
- Manage study programs
- Manage university organizational structure
- Add new teachers and administrative staff

---

## 📦 General Features

- Export data about students and teachers as:
  - XML
  - PDF
- Import evaluation results using XML:
  - Provide file path
  - Or paste full XML into a form
- Validate uploaded XML files
- Export evaluation results as PDF

---

## 🧠 RDF Data Storage

- **Selected entity**: `StudyType` (`TipStudija`)
- Stored and queried using **Apache Jena TDB** (not Fuseki)
- All **CRUD operations** enabled
- Full-text search across all RDF properties
- RDF data is used **instead of relational DB** for this entity

---

## 📂 Document Management Component

- Upload documents with version history tracking
- **Collaborative editing** of documents:
  - Multiple users can edit at the same time
  - Real-time updates using **WebSocket**
  - Changes are broadcast live to all collaborators
  - The system shows who is currently editing

- Visual diff between document versions

---

## Author

**Ilija Popadic, Andrej Vukoje, Nikola Albulov**

## License

This project is licensed under the **Attribution License**.

You are free to use, modify, and share this code for personal purposes, **as long as proper credit is given**.  
That includes:

- Mentioning our full name **Ilija Popadic, Andrej Vukoje, Nikola Albulov** visibly in your project or documentation.
- Providing a working link to this GitHub Repository: [https://github.com/ipopadic-ip/UniDunav_Projekat](https://github.com/ipopadic-ip/UniDunav_Projekat)

Failure to provide visible credit is a violation of this license.

---

## 🏁 Getting Started

```bash
# Backend
cd back
./mvnw spring-boot:run

# Frontend
cd front
npm install
ng serve



