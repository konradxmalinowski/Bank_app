public class Account {
    private double money;
    private String currency;
    private String name;
    private String accountNumber;
    private String password;

    public void Account(double money, String currency, String name, String accountNumber) {
        this.money = money;
        this.currency = currency;
        this.name = name;
        this.accountNumber = accountNumber;
    }

    public double getMoney() {
        return this.money;
    }

    public String getCurrency() {
        return this.currency;
    }

    public String getName() {
        return this.name;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

}
