package PoolGame.observer;

/**
 * Our Subject.
 */
public interface ObservableSubject {
    public void attachObserver(Observer o);

    public void detachObserver(Observer o);

    public String notifyObservers();
}
