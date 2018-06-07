package com.penapereira.cipher.service.impl;

import com.penapereira.cipher.service.NotificationService;

public class NotificationServiceImpl implements NotificationService {

    protected static NotificationServiceImpl singletonInstance = null;

    protected NotificationServiceImpl() {}

    public static NotificationService instance() {
        if (singletonInstance == null) {
            singletonInstance = new NotificationServiceImpl();
        }
        return singletonInstance;
    }
}
