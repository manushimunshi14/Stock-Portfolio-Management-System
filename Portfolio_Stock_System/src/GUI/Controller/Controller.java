package GUI.Controller;

public abstract class Controller {
    protected MainController mainController;

    Controller(MainController mainController) {
        this.mainController = mainController;
    }
    protected abstract void openPage();
}
