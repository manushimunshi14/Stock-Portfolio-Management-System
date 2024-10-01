package GUI.Controller;

import GUI.Authentication.ViewCustomerProfit;
import GUI.Listener.ManagerListener;
import GUI.Listener.ViewCustomerListener;
import System.User.Customer;

public class ViewCustomerController extends Controller implements ViewCustomerListener{
    private ViewCustomerProfit viewCustomerProfitGUI;
    private Customer customer;

    ViewCustomerController(MainController mainController) {
        super(mainController);
        viewCustomerProfitGUI = new ViewCustomerProfit();
        viewCustomerProfitGUI.setViewCustomerListener(this);
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    protected void openPage() {
        viewCustomerProfitGUI.setWindowsSize(700, 650);
        if (viewCustomerProfitGUI.getFrame() == null) {
            viewCustomerProfitGUI.setFrame();
        }
        viewCustomerProfitGUI.initializeGUI();
    }

    public void setTitle(String title){
        if (viewCustomerProfitGUI.getFrame() == null) {
            viewCustomerProfitGUI.setFrame();
        }
        viewCustomerProfitGUI.setTitle(title);
    }

    @Override
    public Customer getCustomer(){
        return customer;
    }

    public void setManagerListener(ManagerListener managerListener) {
        viewCustomerProfitGUI.setManagerListener(managerListener);
    }
}
