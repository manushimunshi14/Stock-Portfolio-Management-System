package System.Account;

public class LoginResult  extends SignupResult {
  private String displayName;

  public LoginResult(String displayName, int userId, int passwordId, int managerId) {
    super(userId, passwordId, managerId);
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return this.displayName;
  }
}
