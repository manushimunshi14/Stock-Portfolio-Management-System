# CS611 - Final Project

# README FILE

---------------------------------------------------------------------------

## Submitted by -

---------------------------------------------------------------------------
1. Qinfeng (Frank) Li : ql2016@bu.edu, U84055774
2. Jingbo Wang : jw6347@bu.edu, U04536921
3. Manushi Munshi : manushi@bu.edu, U25855816
---------------------------------------------------------------------------

## Description of Classes

---------------------------------------------------------------------------
1. DatabaseManager.java - This class is responsible for connecting to the MySQL database and executing SQL queries in the application. It handles database configuration, connection management, and includes methods for executing both update and query operations with proper error handling.
2. ResultSetHandler.java - This interface defines a contract for all the classes which handle the task processing and interpreting of the result set received from a database query. 
3. DatabaseFetcher.java - This class facilitates  retrieval of information from the database. It encapsulates methods for querying and fetching data based on specific requirements, providing a convenient interface for interacting with the database.
4. DatabaseReader.java - This class provides a set of methods for executing SQL queries on the database and retrieving results as a list of rows, where each row is represented as a map of column names to their corresponding values. 
5. Profitdb.java - This class provides methods for interacting with the database to manage profit-related data for a user. It encapsulates the database operations associated with user profit management.
6. Requestdb.java - This class manages user requests in the financial system, handling signup requests and requests to create derivative account. It provides functionalities to fetch, delete, and process requests. 
7. Stockdb.java - This class is responsible for handling stock-related database operations, including retrieving stock details, historical market data, and managing stock entries. It supports tasks such as adding new stocks, updating prices, and deleting stocks from the system.
8. StockTransactdb.java - This class has methods to manage stock transactions within the financial system, recording buying and selling activities. It provides functionalities to retrieve transaction details for users, updating user stock holdings, etc.
9. Userdb.java - This class facilitates the management of user-related database operations, including fetching user details, creating new users, and obtaining user lists. 
10. DatabaseConfigReader.java - This class has functionality to read and process the database configuration file, extracting key-value pairs representing database connection details. 
11. BaseGUI.java - This is an abstract class which defines the basic structure for other GUI classes. It manages frame dimensions, initialization, and common window operations like setting title and closing the window.
12. GUIFunction.java - This interface defines a contract for all GUI classes. This placeComponent method serves as a blueprint for arranging GUI components within the specified panel. 
13. UserGUI.java - This class extends BaseGUI class, implements GUIFunction interface and serves as a base for user-specific GUI classes. It contains methods to initialize the GUI and place components on the window.
14. DerivativeUserPrompt.java - This class extends BaseGUI class and represents a window that prompts the user about the possibility of opening a derivative account. 
15. LoadingWindow.java - The LoadingWindow class extends BaseGUI class and is essentially a GUI window displaying a loading message and animated dots to indicate ongoing processing.
16. LoginGUI.java - This class extends BaseGUI class, implements GUIFunction interface and facilitates user authentication. It supports and handles the user signup and login functionalities.
17. ManagerGUI.java - This class extends UserGUI class and provides GUI functionality for manager role. It includes buttons for managing accounts, handling signup requests, modifying stock quantities, and tracking customer reports.
18. PromptGUI.java - This class extends BaseGUI class, implements GUIFunction interface and represents a GUI window for displaying prompts and messages on the screen for the users. 
19. SignupGUI.java - The SignupGUI class extends the LoginGUI and serves as the GUI for user signup and creation of new user accounts.
20. TimeSeriesChartPanel.java - The TimeSeriesChartPanel class extends JPanel and provides a graphical representation of a time series chart for stock history.
21. TransactionGUI.java - The TransactionGUI class extends the PromptGUI and represents a GUI window for handling transactions. It displays information related to transactions and takes necessary inputs. 
22. ViewCustomerProfit.java - This class extends BaseGUI class represents a GUI window for viewing customer profits and transaction details. 
23. WelcomeGUI.java - The WelcomeGUI class represents the welcome screen of the application. It extends JFrame and includes a company name label and logo.
24. AddStockController.java - This class extends Controller class and implements AddStockListener interface. It manages the addition of new stocks to the system. It coordinates with the AddStockGUI to get user input, validate the input, and add the new stock to the database.
25. Controller.java - The Controller abstract class serves as a foundation for various controllers in the GUI module. It is designed to be extended by specific controller classes within the application
26. CustomerController.java - The CustomerController class is a controller responsible for managing the interaction between the customer GUI (CustomerGUI) and the underlying system.
27. DerivativeUserPromptController.java - The DerivativeUserPromptController class handles user prompts related to derivative account actions. It defines the initiation of actions such as sending requests for creation of derivative account to managers.
28. HomepageController.java - The HomepageController class is responsible for managing the homepage of the application. It creates and displays the WelcomeGUI, which serves as the entry point for users.
29. LoginController.java - The LoginController class manages the login functionality, interacting with the LoginGUI and coordinating user authentication.
30. MainController.java - The MainController acts as a central hub for controlling the application flow. It instantiates and manages instances of various other controllers in the system.
31. ManagerController.java - The ManagerController class handles the manager's interface, connecting the ManagerGUI with the system.
32. PromptController.java - The PromptController class manages prompt messages and interactions, serving as a common controller for various prompts within the application.
33. SignupController.java - The SignupController class handles user sign-up requests, interacting with the SignupGUI. It ensures the validity of user-provided information, communicates with the AuthenticationService for sign-up validation, and facilitates the creation of sign-up requests stored in the database.
34. TransactionController.java - The TransactionController class interacts with the TransactionGUI class and manages various financial transactions, including stock purchases, sales, withdrawals, deposits, and price changes.
35. ViewCustomerController.java  - The ViewCustomerController class is responsible for displaying and managing the view of customer profit information. It utilizes the ViewCustomerProfit GUI and interacts with the underlying system to retrieve and display customer-related data. 
36. AddStockListener.java - This interface defines methods for handling stock addition actions.
37. CustomerListener.java - This interface extends several listener interfaces related to customer actions, such as logging out, viewing profits, and handling various financial transactions. 
38. CustomerProfitListener.java - This interface defines methods to retrieve customer account information and transaction history. It is designed to provide access to details about a customer's financial status and transaction records.
39. CustomerTransactionCompleteListener.java - This interface specifies a method for handling the completion of a customer transaction. Classes implementing this interface respond to the successful completion of financial transactions initiated by a customer.
40. DerivativeUserPromptListener.java - The DerivativeUserPromptListener interface is designed for classes handling derivative user prompts. It defines a method for confirming derivative-related actions initiated by the user.
41. HomepageListener.java - The HomepageListener interface is designed for classes handling actions related to the homepage. It defines a method for responding to the user pressing the login button on the homepage.
42. LoginListener.java - The LoginListener interface provides methods for handling user login actions. It includes methods for initiating login, checking login credentials, and responding to login requests. The interface also provides access to the associated LoginGUI for further customization or interaction.
43. LogoutListener.java - This provides methods for handling user logout action and includes method for initiating logout.
44. ManagerListener.java - The ManagerListener interface extends 2 listener interfaces and is designed for classes handling managerial actions. 
45. PromptListener.java - The PromptListener interface defines methods for handling prompts or messages in the GUI system.
46. ReloadListener.java - The ReloadListener interface extends the PromptListener interface and provides an additional method for reloading the window. 
47. SignupListener.java - The SignupListener interface defines methods related to user signup actions. It includes methods for initiating the signup process, handling signup requests, etc.
48. StockPriceChangeListener.java - The StockPriceChangeListener interface specifies a method for handling changes in stock prices.
49. StockSelectionListener.java - The StockSelectionListener interface defines a method for handling the selection of a stock.
50. StockTransactionCompleteListener.java - The StockTransactionCompleteListener interface provides a method for handling the completion of stock transactions.
51. TransactionListener.java - The TransactionListener interface extends the PromptListener interface and includes a method for confirming transaction-related actions. 
52. ViewCustomerListener.java - This interface defines a method for retrieving customer information.
53. BalanceView.java - This class is a GUI component that displays the user's balance information.
54. CheckOutStock.java - This class represents a module of the customer interface that allows users to check out available stocks. It provides a list of stocks, and when a user selects a stock, it displays detailed stock information.
55. CustomerDeposit.java - This  class is a GUI component representing a deposit button in the customer interface. It is associated with a CustomerListener and triggers the onDeposit method when clicked.
56. CustomerWithdraw.java - The CustomerWithdraw class represents the GUI for the customer module, providing a "Withdraw" button. It is associated with a CustomerListener and triggers the onWithdraw method when clicked. 
57. ViewPurchasedStock.java - The ViewPurchasedStock class is a graphical user interface component that allows customers to view their purchased stocks.
58. AddStockGUI.java - The AddStockGUI class represents a graphical user interface for adding a new stock. It includes text fields for the stock symbol, name, and current price, along with "Confirm" and "Cancel" buttons.
59. DerivativeUserAssigner.java - The DerivativeUserAssigner class provides a GUI for assigning derivative user roles. It contains an "Assign Derivative User" button that, when clicked, dynamically loads and displays a table containing information about potential derivative users.
60. PriceModifier.java - The PriceModifier class is a GUI component that allows a manager to modify stock prices. 
61. SignupRequestHandler.java - The SignupRequestHandler class provides a GUI for handling registration requests.
62. StockQuantityChanger.java - The StockQuantityChanger class provides a GUI for managing stocks. It includes a "Manage Stocks" button, which, when clicked, dynamically loads and displays options to either "Add New Stock" or "Delete A Stock." The "Add New Stock" and "Delete" buttons trigger events in the associated ManagerListener. 
63. TrackCustomerReport.java - The TrackCustomerReport class offers a GUI for tracking customer reports. It contains a "Track Customer Report" button that, when clicked, dynamically loads and displays a table with information about customers.
64. ButtonEditor.java - This class serves as a customized cell editor for JTable buttons in the GUI. It dynamically handles button clicks based on their labels, facilitating various actions such as approving/disapproving signup requests, promoting derivative users, deleting stocks, and viewing customer profit details.
65. ButtonRenderer.java - This class implements TableCellRenderer and customizes JButton rendering within JTable cells. It ensures proper rendering of button labels in table cells, enhancing the visual representation of interactive buttons.
66. GuiUtils.java - This class provides utility methods for creating various GUI components such as labels, text fields, buttons, and formatted fields. It also includes methods for generating JScrollPane instances for displaying stock details, transaction details, and customer account information, enhancing the overall GUI functionality.
67. ErrorCallback.java - The ErrorCallback interface defines a contract for handling errors in the GUI. It includes a single method, onBack(), which is invoked when an error occurs, providing a mechanism to navigate back or take appropriate corrective actions in response to the error.
68. PromptSetup.java - This class provides static methods for setting up and displaying prompt messages in the GUI.
69. RandomGenerator.java - The RandomGenerator class generates random values based on the current price, minimum percentage, and maximum percentage. 
70. StockCalculator.java - The StockCalculator class contains static methods for calculating stock-related metrics. 
71. TimerManager.java - The TimerManager class manages the scheduling of timer tasks. It includes methods to set the change rate of a timer task at fixed intervals and to stop the timer.
72. VolatilityCellRenderer.java - The VolatilityCellRenderer class is a custom table cell renderer for rendering numeric values in a JTable with different colors based on their sign.
73. PasswordAuthentication.java - The PasswordAuthentication class provides a secure and flexible password hashing and authentication mechanism.
Reference - https://stackoverflow.com/questions/2860943/how-can-i-hash-a-password-in-java/2861125#2861125
74. AuthenticationService.java - The AuthenticationService class manages user authentication and related functionalities, including user signup, login, role validation, and retrieving user information from the database.
75. NotificationService.java - The NotificationService class is used for notifying customers when they reach 10k realized profit. 
76. Account.java - This interface defines the common methods for different account types. These methods include money deposit, withdrawal and getting account balance. 
77. AccountFactory.java - This class defines method for creating instances of different types of accounts. It has a switch case to create a trading or a derivative account depending on the account type identifier. The DatabaseFetcher is utilized to fetch necessary data during the account creation process.
78. CustomerAccount.java - This abstract class implements the Account interface and is the base class for various types of accounts in the system. It provides methods for deposit, withdrawal, and balance retrieval. It also includes features such as a trading strategy, portfolio management, and profit tracking.
79. DerivativeAccount.java - This class extends the CustomerAccount class and defines a specific type of customer account to deal with derivative investments. 
80. DerivativeRequest.java - This class represents a request to create a derivative account for customers in the system. 
81. LoginResult.java - This class extends SignupResult class and represents a user login operation result. The attributes included are user ID, manager ID, user display name and password ID. 
82. SignupRequest.java - This class includes the user information needed to put in a signup request. The attributes are request number, password, username and display name. 
83. SignupResult.java - This class represents a user signup operation result. The attributes included are user ID, manager ID, user display name and password ID.
84. StockHolding.java - This class represents the stock holdings of a customer in the system.
85. TradingAccount.java - This class extends the CustomerAccount class and defines a specific type of customer account to handle trading operations.
86. TradingStrategy.java - This is an interface that defines method for trading strategy and executing trading operations. 
87. ProfitObserver.java - This interface defines the contract for objects that observe and receive updates on realized and unrealized profits.
88. Subject.java - This interface explains the structure for a subject that can be observed by ProfitObserver instances. It includes methods for registering, removing, and notifying observers.
89. Portfolio.java - This class represents the stock portfolio for a customer and includes methods to handle stocks, add/delete/retrieve stocks and manage transactions. 
90. PortfolioManager.java - This is a manager class to handle user portfolios. It connects with the database to update, add and retrieve customer or stock information. 
91. ProfitCalculator.java - This class defines methods to calculate realized and unrealized profits for customer accounts. 
92. ProfitDetails.java - This class stores and represents the different profits associated with a user/customer account. 
93. ProfitTracker.java - This class monitors and tracks user profit. If user's realized profit has crossed 10k, it offers option to create derivative account. 
94. Stock.java - This class represents a stock holding in the trading system. It includes stock attributes such as last update time of stock, stock id, stock name, stock symbol and current stock price. 
95. StockManager.java - This class serves as a manager class for all the stocks and defines methods for adding new stocks, deleting existing stocks or updating stock price. 
96. StockMarketHistory.java - This class represents the information about a stock's historical market data and attributes such as ID, last sale price, and the timestamp of the last update.
97. StockPriceChanger.java - This class is responsible for periodically updating the stock prices in the stock market using a timer. 
98. StockPurchaseDetails.java - This class represents the details of a stock purchase such as quantity, price etc. 
99. Tradable.java -  This interface defines methods for handling different trading strategies in the stock trading system. 
100. TransactionDetails.java - This class represents and stores information of a transaction including stock id, user id, quantity, stock symbol, price etc. 
101. StockTrading.java - The StockTrading class manages stock trading operations for a customer, allowing them to buy and sell stocks. It interacts with the database, portfolio, and profit calculator to handle transactions, update balances, and record trade details. 
102. Admin.java - This class extends the user class and defines a special user of the stock trading system - the admin. It includes methods to modify stock details, change prices, check user details, and obtain reports on stock market activities.
103. Customer.java - This class extends the user class and represents a customer user of the system. It includes methods for creating and managing customer accounts, retrieving portfolio details, and profit tracking.  
104. CustomerData.java - This class encapsulates customer-specific data, including the customer object instance and related profit information.
105. CustomerManager.java - This class is responsible for managing customer accounts. It facilitates creation of new accounts and retrieving customer information.
106. ManagerAccount.java - This class implements Account interface and defines a manager account for the system. 
107. User.java - This class represents a user of the stock trading system and defines various user attributes in the system such as user id, name, username, password id etc.  
108. TestStockSystem.java - This is a testing class to test the stock system in the java project. 
109. CustomerReport.java - This class represents a GUI component for generating customer reports in the system. 
110. App.java - This class includes the main method and invokes the system run. 

---------------------------------------------------------------------------

## Compile and Run Instructions

---------------------------------------------------------------------------

Step 1 - Unzip contents

### Intellij - Recommended

1. Open the folder in the IDE, and go to src/App.java and click on run button.

### Terminal (Not recommended)


1. Navigate to the unzipped folder
2. Go to the Portfolio_Stock_System/src folder
3. Run the following commands

javac *.java

java App

---------------------------------------------------------------------------

## Runtime Environment 

---------------------------------------------------------------------------

- Computer - Macbook
- Architecture - ARM64
- Memory - 16GB
- Java Version - java version "1.8.0_381"