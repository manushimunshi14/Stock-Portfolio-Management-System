package System.Account;

public class SignupResult {
  private int userId;
  private int passwordId;
  private int managerId;

  public SignupResult(int userId, int passwordId, int managerId) {
    this.userId = userId;
    this.passwordId = passwordId;
    this.managerId = managerId;
  }

  public int getUserId() {
    return userId;
  }

  public int getPasswordId() {
    return passwordId;
  }

  public int getManagerId() {
    return managerId;
  }
}
