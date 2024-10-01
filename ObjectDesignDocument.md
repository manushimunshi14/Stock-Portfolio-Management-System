# CS611 - Final Project

# Object Design Document

---------------------------------------------------------------------------

## Submitted by -

---------------------------------------------------------------------------
1. Qinfeng (Frank) Li : ql2016@bu.edu, U84055774
2. Jingbo Wang : jw6347@bu.edu, U04536921
3. Manushi Munshi : manushi@bu.edu, U25855816

---------------------------------------------------------------------------

## UML -

---------------------------------------------------------------------------

![UML.png](UML.png)



---------------------------------------------------------------------------

## OO Design Overview -

---------------------------------------------------------------------------


The portfolio management system in this project is designed to provide the users a flexible, efficient and user-friendly platform to carry out stock trading activities.


The key system design choices include utilization of various design patterns, object-oriented principles and modular architectural design to enhance user experience and make the system efficient.

The modular architecture ensures separation of the three main components of the system – system design, GUI interface and database management. The major functionalities of the system are encapsulated in separate classes and packages to ensure proper code organization, code reusability and increase system efficiency.

The system design incorporates essential OO principles such as polymorphism, inheritance, abstraction and interfaces to ensure proper functioning of various components thus facilitating ease of understanding, maintenance, and future extensions. The incorporation of OO principles reduces redundant code thereby promoting code reusability, scalability and extendibility.

The system design is made efficient with incorporation of useful design patterns such as –

1.	Factory pattern – The AccountFactory class implements factory pattern by defining a method for creation of different types of customer accounts (trading and derivative) based on account type identifier. This promotes flexibility and extensibility in  the system by facilitating addition of new account types without making major changes to existing structure.
2.	Observer Pattern - ProfitObserver interface utilizes Observer pattern. The implementation of this interface is in the CustomerAccount class. Observers, such as profit trackers, are attached to customer accounts, enabling real-time updates of realized and unrealized profits.
3.	Decorator pattern  - This is implemented in order to dynamically attach additional responsibilities to the controllers for different GUI implementations, enhancing their functionality without modifying the base structure.
4.	Strategy Pattern – The various trading  strategies in the portfolio management system are encapsulated in the TradingStrategy interface. The CustomerAccount class contains a reference to a specific trading strategy, allowing dynamic switching between different strategies during trade execution without modifying the core account logic.


The database component of the system is implemented via a MySQL database. It is implemented in the system using the DatabaseManager, DatabaseFetcher, and related classes. This ensures data security and persistent data. 

The system design is bound to the GUI components via the use of controller and listener classes. The basic user interface is formed by the different GUI components which display information to the user and take in the user input.  They communicate with the system via the use of controllers and listeners to ensure proper system responses to user actions.


---------------------------------------------------------------------------

## Benefits of the Design -

---------------------------------------------------------------------------

1. Flexibility, Scalability and Extensibility - The modular OO design of the system facilitates easy extensions and modifications. We can add newer functionalities such as additional account types, trading strategies and GUI design without modifying the existing system on a large scale.  
2. Readability and Maintainability - This is maintained by encapsulation of code in clear class hierarchies and design patterns. Each class has a single responsibility which in turn improves the debugging and updating process by making the code readable and easy to understand.  
3. Enhanced User Experience - The user interface is handled by the GUI components which in turn communicate with the system via controllers and listeners to enable a user-friendly experience. 
4. Data Persistence and Security - The use of MYSQL ensures maintenance of data even after the system shuts down and thus promotes data security. 


