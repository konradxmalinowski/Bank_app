import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class Database {
    private boolean isConnected = false;
    private Connection connection;
    private Statement statement;
    private final Utils utils = new Utils();

    private boolean checkIfTransferPossible(String accountNumber, double money) {
        if (!isConnected) {
            System.out.println("You must be connected to the database");
            return false;
        }

        if (accountNumber.trim().isEmpty()) {
            System.out.println("Account number and currency must not be empty");
            return false;
        }

        if (money <= 0) {
            System.out.println("Money must not be below or equal to 0");
            return false;
        }

        try {
            String query = "Select money from accounts WHERE accountNumber=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                System.out.println("Account not found");
                return false;
            }

            double accountMoney = resultSet.getDouble("Money");

            if (accountMoney - money < 0) {
                System.out.println("You have too little money");
                return false;
            } else {
                System.out.println("You can transfer money");
                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getPasswordFromDatabase(String accountNumber) {
        if (accountNumber.trim().isEmpty()) {
            System.out.println("Account number must not be empty");
        } else {
            try {
                String query = "SELECT Password from accounts where accountNumber = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, accountNumber);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getString("Password");

                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return "";
    }

    private boolean checkIfPasswordIsValid(String userPassword, String accountNumber) {
        String passwordFromDatabase = getPasswordFromDatabase(accountNumber);
        boolean isPasswordCorrect;

        if (passwordFromDatabase.isEmpty()) {
            System.out.println("Failed to get password from database");
            return false;
        }

        isPasswordCorrect = BCrypt.checkpw(userPassword, passwordFromDatabase);
        if (isPasswordCorrect) {
            System.out.println("Password is valid");
            return true;
        } else {
            System.out.println("Password is not valid");
            return false;
        }

    }

    private boolean checkIfAccountNumberExists(String accountNumber) {
        accountNumber = accountNumber.trim();

        if (accountNumber.trim().isEmpty()) {
            System.out.println("Account number must not be empty");
            return false;
        }

        try {
            String query = "SELECT AccountNumber FROM accounts WHERE AccountNumber = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, accountNumber);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                System.out.println("Account not found");
                return false;
            }

            return true;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/java_bank_app",
                    "root",
                    "");
            isConnected = true;
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database " + e.getMessage());
        }
    }

    public void addAccountToDatabase(String accountNumber, String name, double money, String currency,
                                     String password) {
        accountNumber = accountNumber.trim();
        name = name.trim();
        currency = currency.trim();
        password = password.trim();
        boolean hasAlreadyAccountExisted = checkIfAccountNumberExists(accountNumber);

        if (hasAlreadyAccountExisted) {
            System.out.println("Account already exists");
            return;
        }

        if (!isConnected) {
            System.out.println("You must be connected to the database");
            return;
        }

        if (accountNumber.trim().isEmpty() || currency.trim().isEmpty()) {
            System.out.println("Account number and currency must not be empty");
            return;
        }

        if (money < 0) {
            System.out.println("Money must not be below 0");
            return;
        }

        String hashedPassword = utils.hashPassword(password);

        try {
            String query = "INSERT INTO accounts (AccountNumber, Name, Money, Currency, Password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, accountNumber);
            preparedStatement.setString(2, name);
            preparedStatement.setDouble(3, money);
            preparedStatement.setString(4, currency);
            preparedStatement.setString(5, hashedPassword);

            int rowInserted = preparedStatement.executeUpdate();

            if (rowInserted > 0) {
                System.out.println("Account added successfully");
            } else {
                System.out.println("Failed to add new account");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void removeAccountFromDatabase(String name, String accountNumber, String password) {
        name = name.trim();
        accountNumber = accountNumber.trim();
        password = password.trim();
        boolean isPasswordValid = checkIfPasswordIsValid(password, accountNumber);
        boolean doesAccountExist = checkIfAccountNumberExists(accountNumber);

        if (!isConnected) {
            System.out.println("You must be connected to the database");
            return;
        }

        if (!doesAccountExist) {
            System.out.println("Account number does not exist");
            return;
        }

        if (accountNumber.trim().isEmpty() || name.trim().isEmpty() || password.trim().isEmpty()) {
            System.out.println("Account number and name must not be empty");
            return;
        }

        if (!isPasswordValid)
            return;

        try {
            String query = "DELETE FROM accounts WHERE AccountNumber=? AND Name=? ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, accountNumber);
            preparedStatement.setString(2, name);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Account removed successfully");
            } else {
                System.out.println("Failed to remove account");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateAccountName(String oldName, String newName, String password, String accountNumber) {
        oldName = oldName.trim();
        newName = newName.trim();
        password = password.trim();
        accountNumber = accountNumber.trim();
        boolean isPasswordValid;
        boolean doesAccountExist = checkIfAccountNumberExists(accountNumber);

        if (oldName.trim().isEmpty() || newName.trim().isEmpty() || password.trim().isEmpty()) {
            System.out.println("Old name, new name and password must not be empty");
            return;
        }

        isPasswordValid = checkIfPasswordIsValid(password, accountNumber);
        if (!isPasswordValid)
            return;

        if (!doesAccountExist) {
            System.out.println("Account number does not exist");
            return;
        }

        try {
            String query = "UPDATE accounts SET Name = ? WHERE Name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, oldName);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Account name has been updated successfully");
            } else {
                System.out.println("Failed to update account name");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void transferMoney(String senderAccountNumber, String receiverAccountNumber, String password, double money) {
        senderAccountNumber = senderAccountNumber.trim();
        receiverAccountNumber = receiverAccountNumber.trim();
        password = password.trim();

        if (senderAccountNumber.isEmpty() || receiverAccountNumber.isEmpty() || password.isEmpty()) {
            System.out.println("Sender, receiver and password must not be empty");
            return;
        }

        if (money <= 0) {
            System.out.println("Transfer amount must be greater than 0");
            return;
        }

        boolean doesAccountReceiverExist = checkIfAccountNumberExists(receiverAccountNumber);
        boolean doesAccountSenderExist = checkIfAccountNumberExists(senderAccountNumber);

        if (!doesAccountReceiverExist) {
            System.out.println("Receiver account number does not exist");
            return;
        }

        if (!doesAccountSenderExist) {
            System.out.println("Sender account number does not exist");
            return;
        }

        boolean isTransferPossible = checkIfTransferPossible(senderAccountNumber, money);
        if (!isTransferPossible)
            return;

        boolean isPasswordValid = checkIfPasswordIsValid(password, senderAccountNumber);
        if (!isPasswordValid)
            return;

        try {
            connection.setAutoCommit(false);

            String querySender = "UPDATE accounts SET Money = Money - ? WHERE AccountNumber = ?";
            String queryReceiver = "UPDATE accounts SET Money = Money + ? WHERE AccountNumber = ?";

            try (PreparedStatement preparedStatementSender = connection.prepareStatement(querySender);
                 PreparedStatement preparedStatementReceiver = connection.prepareStatement(queryReceiver)) {

                preparedStatementSender.setDouble(1, money);
                preparedStatementSender.setString(2, senderAccountNumber);
                int affectedRowsSender = preparedStatementSender.executeUpdate();

                preparedStatementReceiver.setDouble(1, money);
                preparedStatementReceiver.setString(2, receiverAccountNumber);
                int affectedRowsReceiver = preparedStatementReceiver.executeUpdate();

                if (affectedRowsSender > 0 && affectedRowsReceiver > 0) {
                    connection.commit();
                    System.out.printf("Money has been sent from %s to %s!\n", senderAccountNumber,
                            receiverAccountNumber);
                } else {
                    // cancel changes
                    connection.rollback();
                    System.out.printf("Failed to send money from %s to %s\n", senderAccountNumber,
                            receiverAccountNumber);
                }

            } catch (SQLException e) {
                // cancel changes
                connection.rollback();
                System.out.println("Transaction failed and has been rolled back: " + e.getMessage());
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error during money transfer", e);
        }
    }

    public void login(String accountNumber, String password) {
        accountNumber = accountNumber.trim();
        password = password.trim();

        if (accountNumber.isEmpty() || password.isEmpty()) {
            System.out.println("Account number and password must not be empty");
            return;
        }

        if (!isConnected) {
            System.out.println("You must be connected to the database");
            return;
        }

        boolean doesAccountExist = checkIfAccountNumberExists(accountNumber);
        if (!doesAccountExist) {
            System.out.println("Account does not exist");
            return;
        }

        boolean isPasswordValid = checkIfPasswordIsValid(password, accountNumber);
        if (isPasswordValid) {
            System.out.println("Login successful");
        } else {
            System.out.println("Login failed: invalid password");
        }
    }

    public void getAccounts() {
        if (!isConnected) {
            System.out.println("You must be connected to the database");
            return;
        }

        try {
            String query = "SELECT AccountNumber, Name, Money, Currency FROM accounts";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("All accounts:");
            while (resultSet.next()) {
                String accNum = resultSet.getString("AccountNumber");
                String name = resultSet.getString("Name");
                double money = resultSet.getDouble("Money");
                String currency = resultSet.getString("Currency");
                System.out.printf("AccountNumber: %s, Name: %s, Money: %.2f, Currency: %s\n", accNum, name, money,
                        currency);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}