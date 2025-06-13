package com.penapereira.cipher.util.patterns.observer;

import java.util.ArrayList;
import java.util.List;

public class AbstractSubject implements Subject {
    protected boolean hasChanged = false;
    protected List<Observer> observers;

    /**
     * Register an observer to the subject.
     * This method can be overridden by subclasses to provide specific
     * registration logic.
     */
    @Override
    public void registerObserver(Observer observer) {
        if (observers == null) {
            observers = new ArrayList<>();
        }
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        if (observers != null && observer != null) {
            observers.remove(observer);
        }
    }

    @Override
    public void notifyObservers(String message) {
        if (observers != null && !observers.isEmpty()) {
            for (Observer observer : observers) {
                observer.update(message);
            }
        }
    }

    protected void requestNotifyObservers() {
        if (hasChanged) {
            notifyObservers("State has changed");
            hasChanged = false; // Reset the change flag after notifying
        }
    }
    
    public void setChanged() {
        hasChanged = true;
    }
}
