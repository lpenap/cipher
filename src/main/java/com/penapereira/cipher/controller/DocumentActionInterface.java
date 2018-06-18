package com.penapereira.cipher.controller;

import com.penapereira.cipher.model.document.Document;

public interface DocumentActionInterface {
    public enum ActionType {
        UPDATE, DELETE, ADD;
    }

    ActionType getAction();
    
    Document getDocument();
}
