# 💳 Bank App

A simple yet powerful Java console application for managing bank accounts with MySQL database integration. Secure, user-friendly, and perfect for learning or small-scale banking simulations!

---

## 🚀 Features

- **Register new accounts** with password hashing (BCrypt)
- **Login** securely
- **View all accounts**
- **Transfer money** between accounts
- **Delete accounts**
- **Change account names**
- All sensitive operations require password authentication

---

## 🏗️ Project Structure

```
Bank app/
├── src/
│   ├── Main.java         # Entry point, menu logic
│   ├── Database.java     # Database operations (MySQL)
│   ├── Account.java      # Account model
│   ├── Accounts.java     # Accounts collection
│   ├── Utils.java        # Utility functions (password hashing)
│   └── org/mindrot/jbcrypt/BCrypt.java # BCrypt implementation
├── lib/
│   └── mysql-connector-j-9.3.0.jar # MySQL JDBC driver
```

---

## ⚙️ Requirements

- Java 11 or newer
- MySQL server (local or remote)
- MySQL JDBC driver (included in `lib/`)

---

## 🛠️ Setup & Usage

1. **Clone the repository**
2. **Set up MySQL database:**
   - You can quickly set up the database by importing the provided `accounts.sql` file using phpMyAdmin or the MySQL command line:
     ```sh
     mysql -u root -p < accounts.sql
     ```
   - This will create a database named `java_bank_app` and a table `accounts` with columns:
     - `Id` (INT, PRIMARY KEY, AUTO_INCREMENT)
     - `AccountNumber` (VARCHAR, UNIQUE)
     - `Name` (VARCHAR)
     - `Money` (INT)
     - `Currency` (VARCHAR)
     - `Password` (VARCHAR)
3. **Configure database credentials**
   - By default, the app connects to `root` user with no password. Adjust in `Database.java` if needed.
4. **Build and run the app:**
   - Compile all `.java` files and include the MySQL connector JAR in your classpath.
   - Example (from project root):
     ```sh
     javac -cp lib/mysql-connector-j-9.3.0.jar src/*.java src/org/mindrot/jbcrypt/BCrypt.java
     java -cp lib/mysql-connector-j-9.3.0.jar;src Main
     ```
5. **Follow the interactive menu** to manage accounts!

---

## 🔒 Security

- Passwords are hashed using BCrypt before storing in the database.
- All sensitive actions (login, transfer, delete, update) require password verification.

---

## 🤝 Contributing

Pull requests and suggestions are welcome! For major changes, please open an issue first to discuss what you would like to change.

---

## 📄 License

This project uses the open-source [BCrypt library](https://github.com/jeremyh/jBCrypt) by Damien Miller. See source files for license details.

---

## Screenshots
![Screenshot 2025-07-02 152356](https://github.com/user-attachments/assets/3b12a838-c18d-45c7-9dde-f3bccc98e2c6)


---

## 🙋 FAQ

**Q: Can I use a different database?**
A: Yes, but you will need to adjust the connection string and SQL queries in `Database.java`.

**Q: How are passwords stored?**
A: Passwords are never stored in plain text. They are hashed using BCrypt.

**Q: How do I reset a forgotten password?**
A: Currently, password reset is not implemented for security reasons.
