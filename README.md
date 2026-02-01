# Galaxy BBS Project

A Java Servlet/JSP based Bulletin Board System (Galaxy^bbs).

## 🚀 Deployment Status

**Can this be deployed to Vercel?**  
❌ **No.** Vercel is designed for frontend frameworks (Next.js, React) and Serverless functions (Node.js, Python, Go). It does not support Java Servlet Containers (Tomcat) or persistent file-based databases (H2) out of the box.

**Where should I deploy it?**  
✅ **Railway**, **Render**, or **Fly.io**. These platforms support Docker containers, which is the best way to deploy this Java application.

---

## 🛠 How to Deploy (Recommended: Railway/Render)

This project includes a `Dockerfile` that automates the build and deployment process.

### Option 1: Deploy with Docker (Railway/Render)

1.  **Push this code to GitHub**.
2.  **Sign up** for [Railway](https://railway.app/) or [Render](https://render.com/).
3.  **Create a New Service** and select your GitHub repository.
4.  The platform will detect the `Dockerfile` and build your application automatically.

### Database Note for Cloud Deployment
This app is configured to use **Oracle SQL**.
*   **Production Solution**: Use a managed Oracle database (e.g., Oracle Cloud, AWS RDS).
    1.  Provision an Oracle database.
    2.  Set the following **Environment Variables** in your deployment settings:
        *   `DB_URL`: `jdbc:oracle:thin:@<your-db-host>:<port>:<sid>` (e.g., `jdbc:oracle:thin:@localhost:1521:xe`)
        *   `DB_USER`: `<your-db-username>`
        *   `DB_PASSWORD`: `<your-db-password>`

### Oracle JDBC Driver
*   You **MUST** add the Oracle JDBC Driver (`ojdbc8.jar` or similar) to the `WEB-INF/lib` directory.
*   Due to licensing, this driver is not included by default. Please download it from Oracle's website.

---

## 💻 Running Locally

### Prerequisites
*   Java JDK 17+
*   Apache Tomcat 10+
*   Oracle Database (XE or Enterprise)

### Steps
1.  **Driver**: Place `ojdbc8.jar` in `WEB-INF/lib`.
2.  **Schema**: Execute `docs/schema.sql` in your Oracle Database to create tables.
3.  **Compile**: Run `compile.bat` (Windows) **from the Tomcat webapp folder** (e.g. `c:\tomcat10\webapps\galaxy_test`). This fills `WEB-INF\classes` with servlet classes. If you develop in a different folder, copy the built `WEB-INF\classes` into your Tomcat `galaxy_test` webapp.
4.  **Start Server**: Ensure Tomcat is running and the project folder (`galaxy_test`) is in `webapps`.
5.  **Reload**: After compiling, restart Tomcat or reload the "Galaxy BBS" application in Tomcat Manager so it picks up the new classes.
6.  **Access**: Open `http://localhost:8080/galaxy_test` in your browser. Login: `http://localhost:8080/galaxy_test/auth/login`.

**If you get 404 on `/auth/login`:** The servlet is not loaded because `WEB-INF\classes` is empty or Tomcat hasn’t reloaded. Compile from the webapp folder (step 3), then restart/reload Tomcat (step 5). As a check, try `http://localhost:8080/galaxy_test/auth/login.jsp`; if that works but `/auth/login` does not, the app is deployed but the servlet classes are missing or not loaded.

### Troubleshooting

**"Address already in use: bind" (port 8080)**  
Another process is using port 8080. Do one of the following:
- **Find and stop the process:** In Command Prompt or PowerShell run `netstat -ano | findstr :8080` to see the PID (last column). Then run `taskkill /PID <number> /F` to stop it (often an old Tomcat or Java process).
- **Use a different port:** Edit Tomcat's `conf/server.xml`, find the Connector with `port="8080"`, change it to e.g. `8081`, then start Tomcat and use `http://localhost:8081/galaxy_test`.

**"Change your password" / "Password found in a data breach" pop-up**  
That message comes from **Google Chrome / Google Password Manager**, not from this app. Chrome warns when a password has been seen in a known breach. You can click OK and continue, or choose a different, stronger password. Account creation and login still work.

## 📂 Project Structure
*   `src/`: Java source code (Models, DAOs, Servlets).
*   `WEB-INF/views/`: JSP files (Frontend templates).
*   `assets/`: CSS, JS, and Images.
*   `docs/`: Database schema and documentation.
