package System.Observer;

public interface Subject {
    void registerObserver(ProfitObserver o);
    void removeObserver(ProfitObserver o);
    void notifyObservers();
}
