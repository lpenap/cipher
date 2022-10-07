package com.penapereira.cipher.controller;

import java.beans.PropertyChangeListener;

public interface DocumentChangeSupportInterface {
    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);
}
