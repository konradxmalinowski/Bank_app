import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Database database = new Database();
        database.connectToDatabase();

        Accounts accounts = new Accounts();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n--- Bank App Menu ---");
            System.out.println("1. Register (create account)");
            System.out.println("2. Login");
            System.out.println("3. Show all accounts");
            System.out.println("4. Transfer money");
            System.out.println("5. Delete account");
            System.out.println("6. Change account name");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.print("Enter account number: ");
                    String accNum = scanner.nextLine();
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter initial amount: ");
                    double money = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter currency: ");
                    String currency = scanner.nextLine();
                    System.out.print("Set password: ");
                    String password = scanner.nextLine();
                    database.addAccountToDatabase(accNum, name, money, currency, password);
                    break;
                case "2":
                    System.out.print("Enter account number: ");
                    String loginAccNum = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String loginPassword = scanner.nextLine();
                    database.login(loginAccNum, loginPassword);
                    break;
                case "3":
                    database.getAccounts();
                    break;
                case "4":
                    System.out.print("Enter sender account number: ");
                    String sender = scanner.nextLine();
                    System.out.print("Enter receiver account number: ");
                    String receiver = scanner.nextLine();
                    System.out.print("Enter sender password: ");
                    String senderPassword = scanner.nextLine();
                    System.out.print("Enter amount to transfer: ");
                    double transferAmount = Double.parseDouble(scanner.nextLine());
                    database.transferMoney(sender, receiver, senderPassword, transferAmount);
                    break;
                case "5":
                    System.out.print("Enter account owner's name: ");
                    String delName = scanner.nextLine();
                    System.out.print("Enter account number: ");
                    String delAccNum = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String delPassword = scanner.nextLine();
                    database.removeAccountFromDatabase(delName, delAccNum, delPassword);
                    break;
                case "6":
                    System.out.print("Enter account number: ");
                    String updAccNum = scanner.nextLine();
                    System.out.print("Enter old name: ");
                    String oldName = scanner.nextLine();
                    System.out.print("Enter new name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String updPassword = scanner.nextLine();
                    database.updateAccountName(oldName, newName, updPassword, updAccNum);
                    break;
                case "0":
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }
}