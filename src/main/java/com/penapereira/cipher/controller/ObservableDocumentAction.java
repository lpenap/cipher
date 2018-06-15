package com.penapereira.cipher.controller;

import com.penapereira.cipher.model.document.Document;
import lombok.Data;

@Data
public class ObservableDocumentAction implements ObservableDocumentActionInterface {

    private ActionType action;
    private Document document;

    public ObservableDocumentAction(ActionType action, Document document) {
        this.action = action;
        this.document = document;
    }
}
