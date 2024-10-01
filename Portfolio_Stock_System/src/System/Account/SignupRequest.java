package System.Account;

public class SignupRequest {
  private int requestNumber;
  private String displayName;
  private String userName;
  private String password;

  public SignupRequest(int requestNumber, String displayName, String userName, String password) {
    this.requestNumber = requestNumber;
    this.displayName = displayName;
    this.userName = userName;
    this.password = password;
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

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
