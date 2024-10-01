package System.User;

public class User {
    protected int id;
    protected String name;
    protected String username;
    protected int managerId;
    protected int passwordId;
    protected double balance;

    protected User(int id, String name, String username, int managerId, int passwordId) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.managerId = managerId;
        this.passwordId = passwordId;
    }

    protected User(String name, String username, double balance) {
        this.name = name;
        this.username = username;
        this.balance = balance;
    }

    protected User(int id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public int getPasswordId() {
        return passwordId;
    }

    public void setPasswordId(int passwordId) {
        this.passwordId = passwordId;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double balance) {
        System.out.println("Setting balance. Old balance: " + this.balance);
    this.balance = balance;
    System.out.println("New balance set. New balance: " + this.balance);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }
}
