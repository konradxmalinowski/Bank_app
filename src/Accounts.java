import java.util.ArrayList;

public class Accounts {
    private final ArrayList<Account> accounts;

    public Accounts() {
        accounts = new ArrayList<>();
    }

    public void displayAccounts() {
        for (Account account : accounts) {
            System.out.printf("""
                    Name: %s,
                    Account number: %s,
                    Money: %f,
                    Currency: %s""", account.getName(), account.getAccountNumber(), account.getMoney(),
                    account.getCurrency());
        }
    }
}
