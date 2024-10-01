package System.Portfolio;

import Database.DatabaseManager;
import DatabaseReader.Requestdb;
import System.User.Customer;

public class ProfitTracker {
    /*
        Track and calculate profits
     */
    private DatabaseManager dbManager;
    private static final double DERIVATIVE_THRESHOLD = 10000.0;

    public boolean reachThreshold(double realizedProfit) {
        //TODO add notification
        return realizedProfit >= DERIVATIVE_THRESHOLD;
    }

    private void derivativeOption(Customer customer) {
        dbManager = new DatabaseManager();
        Requestdb requestdb = new Requestdb(dbManager);
        System.out.println("Congrats! You have make 10k, do you want to sent out derivative account creation request?");
        //TODO add request to request table
        // Update request table
        requestdb.addRequest(customer.getUsername(), customer.getName(), "IGNORE", 1);
        System.out.println("Request sent! Wait for approval.");
    }

}
