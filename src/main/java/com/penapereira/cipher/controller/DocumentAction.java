package com.penapereira.cipher.controller;

import com.penapereira.cipher.model.document.Document;
import lombok.Data;

@Data
public class DocumentAction implements DocumentActionInterface {

    private ActionType action;
    private Document document;

    public DocumentAction(ActionType action, Document document) {
        this.action = action;
        this.document = document;
    }
}
