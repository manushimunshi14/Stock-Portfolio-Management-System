package System.Account;

public class DerivativeRequest {
  private int requestNumber;
  private String displayName;
  private String userName;
  private double balance;

  public DerivativeRequest(int requestNumber, String displayName, String userName, double balance) {
    this.requestNumber = requestNumber;
    this.displayName = displayName;
    this.userName = userName;
    this.balance = balance;
  }

  public int getRequestNumber() {
    return this.requestNumber;
  }

  public void setRequestNumber(int requestNumber) {
    this.requestNumber = requestNumber;
  }

  public String getDisplayName() {
    return this.displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getUserName() {
    return this.userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public double getBalance() {
    return this.balance;
  }

  public void setPassword(double balance) {
    this.balance = balance;
  }
}
