package GUI.Controller;
import Database.DatabaseManager;
import DatabaseReader.Requestdb;
import GUI.Authentication.DerivativeUserPrompt;
import GUI.Listener.CustomerListener;
import GUI.Listener.DerivativeUserPromptListener;
import System.User.Customer;

public class DerivativeUserPromptController extends Controller implements DerivativeUserPromptListener {
    private DerivativeUserPrompt derivativeUserPromptGUI;
    private Customer customer;
    private DatabaseManager dbManager;
    DerivativeUserPromptController(MainController mainController) {
        super(mainController);
        derivativeUserPromptGUI = new DerivativeUserPrompt(this);
    }

    public void setCustomer(Customer customer){
        this.customer = customer;
    }


    @Override
    protected void openPage() {
        derivativeUserPromptGUI.setWindowsSize(400, 100);
        if (derivativeUserPromptGUI.getFrame() == null) {
            derivativeUserPromptGUI.setFrame();
        }
        derivativeUserPromptGUI.initializeGUI();
    }

    @Override
    public void confirm(){
        dbManager = new DatabaseManager();
        Requestdb requestdb = new Requestdb(dbManager);
        requestdb.addRequest(customer.getUsername(), customer.getName(),"IGNORE",1);
        System.out.println("Request sent, wait for manager confirm");
        derivativeUserPromptGUI.closeWindow();
    }

    public void setWindowSize(int wight, int height){
        derivativeUserPromptGUI.setWindowsSize(wight,height);
    }
}
