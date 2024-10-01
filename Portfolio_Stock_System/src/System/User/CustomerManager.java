package System.User;

import System.Account.CustomerAccount;
import System.Portfolio.Portfolio;
import System.Stock.Stock;
import System.Stock.StockPurchaseDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomerManager {
    /*
        Approve and mange customer accounts
     */
    private List<Customer> customers;

    public CustomerManager() {
        this.customers = new ArrayList<>();
    }

    public void addNewCustomer(Customer customer){
        this.customers.add(customer);
    }

    public List<Customer> getAllCustomers(){
        return new ArrayList<>(this.customers);
    }

    public boolean validPurchase(Customer customer, Stock stock, Integer quantity){
        double requiredAmount = stock.getCurrPrice() * quantity;
        double customerBalance = customer.getBalance();
        return customerBalance >= requiredAmount;
    }

//    public void addStockToPortfolio(Customer customer,Stock stock, int quantity){
//        if (validPurchase(customer,stock,quantity)){
//            CustomerAccount account = customer.getAccount();
//            Portfolio portfolio = account.getPortfolio();
//            portfolio.addStock(stock,quantity,stock.getCurrPrice());
//        }
//    }

    public void removeStockFromPortfolio(Customer customer, Stock stock, int quantity){
        CustomerAccount account = customer.getAccount();
        Portfolio portfolio = account.getPortfolio();
        portfolio.removeStock(stock,quantity);
    }

//    public boolean sellStock(Customer customer, String symbol,int numberToSell){
//        Portfolio portfolio = customer.getAccount().getPortfolio();
//        Map<Stock, StockPurchaseDetails> ownStocks = portfolio.getStockTransactHistory(customer.getId());
//        for (Map.Entry<Stock,StockPurchaseDetails> entry : ownStocks.entrySet()){
//            Stock stock = entry.getKey();
//            if(stock.getSymbol().equals(symbol)){
//                StockPurchaseDetails details = entry.getValue();
//
//                if (details.getQuantity() > 0){
//                    int updateQuantity = details.getQuantity() - numberToSell;
//                    if (updateQuantity > 0){
//                        details.setQuantity(updateQuantity);
//                    }else if (updateQuantity == 0){
//                        ownStocks.remove(stock);
//                    }
//
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    public void updateCustomerBalance(Customer customer,CustomerAccount customerAccount,Stock stock, int quantity){
        if (validPurchase(customer,stock,quantity)){
            customerAccount.withdraw(stock.getCurrPrice() * quantity);
        }
    }




}
